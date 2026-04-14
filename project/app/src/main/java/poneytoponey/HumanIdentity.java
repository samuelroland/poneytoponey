package poneytoponey;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import java.rmi.server.UnicastRemoteObject;

public class HumanIdentity implements Identity {

    private String username;
    private Map<String, Identity> knownParticipants; // cache or remote Identity indexed by username
    private Map<UUID, Chat> chats; // ajoutés dans la liste par createChat ?

    private List<View> views;
    private Registry remoteRegistry;

    public HumanIdentity(String user) {
        this.username = user;
        this.views = new ArrayList<>();
        this.chats = new HashMap<>();
        this.knownParticipants = new HashMap<>();
        try {
            // Try joining the network by publishing the current object to the RMI registry
            this.remoteRegistry = LocateRegistry.getRegistry(poneytoponey.App.PORT);
            // We have to publish this object fist before binding it to the registry
            // Note: the port 0 lets the java RMI systems choose a random client port
            Identity stub = (Identity) UnicastRemoteObject.exportObject(this, 0);
            this.remoteRegistry.bind(user, stub);
        } catch (AlreadyBoundException e) {
            System.err.println(e.getMessage());
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
    }

    public String getUsername() {
        return this.username;
    }

    public Collection<Chat> getChats() {
        return this.chats.values();
    }

    public List<String> listParticipantsUsername() {
        // We only have Identity objects published under their username for now
        // If we publish other types of objects, we may need to prefix them and remove
        // these prefixes here after filter.
        try {
            return Arrays.asList(this.remoteRegistry.list());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Chat createChat(String recipient) throws RemoteException, Exception {
        if (!listParticipantsUsername().contains(recipient)) {
            throw new Exception("This participant doesn't exist in the network !");
        }
        Chat chat = new Chat(recipient);
        Identity remote = (Identity) this.remoteRegistry.lookup(recipient);
        remote.remoteAskForChat(this.username, chat.getUuid());
        return chat;
    }

    private Identity getRemoteIdentityFromChat(UUID chatID) {
        String otherUsername = this.chats.get(chatID).getOtherUsername();
        return knownParticipants.get(otherUsername);
    }

    public void approveChat(UUID chatID) throws RemoteException {
        Chat chat = chats.get(chatID);
        if (chat != null) {
            chat.setApproved(true);
            // utilisation de remote registry pour notifier
            Identity remote = getRemoteIdentityFromChat(chatID);
            if (remote != null) {
                remote.remoteApproveBackChat(chatID);
            }
            // notifyViewsChatOpened(chatID);
        }
    }

    public void subscribeViewForChatEvent(View view) {
        this.views.add(view);
    }

    private void sendMessage(UUID chatID, String text) throws RemoteException {
        Identity remote = getRemoteIdentityFromChat(chatID);
        Chat chat = chats.get(chatID);
        if (chat != null && chat.getApproved() && text != null) {
            Message m = chat.insertNewMessage(text, this.username);
            if (remote != null) {
                remote.remoteSendMessageInChat(chatID, m.getTexte(), m.getSenderTimestamp());
            }
            // notifyViewsMessage(chatID,m);
        }

    }

    private void closeChat(UUID chatID) throws RemoteException {
        Identity remote = getRemoteIdentityFromChat(chatID);
        if (chats.containsKey(chatID)) {
            chats.remove(chatID);
        }
        if (remote != null) {
            remote.remoteCloseChat(chatID);
            // chat.show(chatclose()) //mais comment savoir quel chat
        }
    }

    // ----- Identity -----
    public void remoteAskForChat(String author, UUID chatId) {
        Identity authorIdentity = knownParticipants.get(author);

        for (View view : views) {
            view.showChatRequest(author);
        }
    }

    public void remoteApproveBackChat(UUID chatId) {
        Chat chat = chats.get(chatId);

        if (chat == null) {
            String otherUsername = this.chats.get(chatId).getOtherUsername();
            chat = new Chat(otherUsername);
            chats.put(chat.getUuid(), chat);
        }

        chat.setApproved(true);

        for (View view : views) {
            // view.start(this);
        }
    }

    public void remoteRefuseChat(UUID chatId) {
        Chat chat = chats.get(chatId);

        if (chat == null) {
            return; // à revoir
        }

        for (View view : views) {
            view.showChatRefuse(this.username);
        }

    }

    public void remoteSendMessageInChat(UUID chatId, String text, long senderTimestamp) {
        Chat chat = chats.get(chatId);

        if (chat == null) {
            return; // à revoir
        }

        for (View view : views) {
            view.showChatMessage(this.username); // j'ai l'impression que cette méthode manque au diagramme de classe...
        }

    }

    public void remoteCloseChat(UUID chatId) {
        Chat chat = chats.get(chatId);

        if (chat == null) {
            return; // à revoir
        }

        for (View view : views) {
            view.showChatClose(this.username);
        }

        chats.remove(chatId);
        chat.setApproved(false); // pertinent à faire? ou aucun sens ?
        // est ce que y a d'autres à faire ? détruire Chat?
    }
}
