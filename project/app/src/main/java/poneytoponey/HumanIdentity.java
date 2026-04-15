package poneytoponey;

import java.net.InetAddress;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import java.rmi.server.UnicastRemoteObject;

public class HumanIdentity implements Identity {

    private String username;
    private Map<String, Identity> knownParticipants; // cache or remote Identity indexed by username
    private Map<UUID, Chat> chats; // ajoutés dans la liste par createChat ?
    private Directory directory;
    private List<View> views;
    private Registry ourLocalRegistry;

    public HumanIdentity(String user, Directory directory) {
        this.directory = directory;
        this.username = user;
        this.views = new ArrayList<>();
        this.chats = new HashMap<>();
        this.knownParticipants = new HashMap<>();
        try {
            // Try joining the network by publishing the current object to the our local RMI
            // registry
            ourLocalRegistry = LocateRegistry.createRegistry(poneytoponey.App.PORT);
            // We have to publish this object fist before binding it to the registry
            // Note: the port 0 lets the java RMI systems choose a random client port
            Identity stub = (Identity) UnicastRemoteObject.exportObject(this, 0);
            ourLocalRegistry.bind(user, stub);
            try {
                InetAddress ip = InetAddress.getLocalHost();
                this.directory.join(new Entry(username, ip.getHostAddress()));
            } catch (Exception e) {
                System.err.println("Cannot get current local IP address");
                return;
            }
        } catch (AlreadyBoundException e) {
            System.err.println(e.getMessage());
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
    }

    public String getUsername() {
        return this.username;
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
            Identity remote = getRemoteIdentityFromChat(chatID);
            if (remote != null) {
                remote.remoteApproveBackChat(chatID);
            }
            // notifyViewsChatOpened(chatID);
        }
    }

    public void refuseChat(UUID oldChatID) throws RemoteException {
        Chat chat = chats.get(oldChatID);
        if (chat != null) {
            Identity remote = getRemoteIdentityFromChat(oldChatID);
            if (remote != null) {
                remote.remoteRefuseChat(oldChatID);
            }
        }
    }

    public void subscribeViewForChatEvent(View view) {
        this.views.add(view);
    }

    public void sendMessage(UUID chatID, String text) throws RemoteException {
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

    public void closeChat(UUID chatID) throws RemoteException {
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
    public void remoteAskForChat(String author, UUID chatID) throws RemoteException, NotBoundException {
        knownParticipants.put(author, (Identity) remoteRegistry.lookup(author));
        chats.put(chatID, new Chat(author, chatID)); // save the non approved chat
        for (View view : views) {
            view.showChatRequest(author);
        }
    }

    public void remoteApproveBackChat(UUID chatID) throws RemoteException {
        Chat chat = chats.get(chatID);

        if (chat != null) {
            String otherUsername = chat.getOtherUsername();
            chats.put(chat.getUuid(), chat);
            chat.setApproved(true);
            for (View view : views) {
                view.showChatApprobation(otherUsername);
            }
        } else {
            throw new RemoteException("The chat with ID " + chatID + " doesn't exist on client '" + username
                    + "' and cannot be approved. It was either closed before or never requested...");
        }
        // TODO: do we agree we should just throw a RemoteException right ?? there is
        // not chat to approve this is an error.
    }

    public void remoteRefuseChat(UUID chatID) {
        Chat chat = chats.get(chatID);

        if (chat == null) {
            return; // à revoir
        }

        for (View view : views) {
            view.showChatRefuse(this.username);
        }

    }

    public void remoteSendMessageInChat(UUID chatID, String text, long senderTimestamp) {
        Chat chat = chats.get(chatID);

        if (chat == null) {
            return; // à revoir
        }

        for (View view : views) {
            view.showChatMessage(this.username); // j'ai l'impression que cette méthode manque au diagramme de classe...
        }

    }

    public void remoteCloseChat(UUID chatID) {
        Chat chat = chats.get(chatID);

        if (chat == null) {
            return; // à revoir
        }

        for (View view : views) {
            view.showChatClose(this.username);
        }

        chats.remove(chatID);
        chat.setApproved(false); // pertinent à faire? ou aucun sens ?
        // est ce que y a d'autres à faire ? détruire Chat?
    }

    // ------ getters/setters for chats (to use it in shellview ----
    public Map<UUID, Chat> getChats() {
        return this.chats;
    }

    public void addToChats(UUID uuid, Chat chat) {
        this.chats.put(uuid, chat);
    }

    public void removeTOChats(UUID uuid) {
        this.chats.remove(uuid);
    }

    // -----pour trouver le uuid du récipient : chercher dans la liste de tous les
    // chats que l'on en vérifiant la condition chat.username == l'username
    // recherché-----
    public UUID findUuidByUsername(String recipientUsername) {
        if (recipientUsername == null) {
            return null;
        }
        for (Chat chat : this.chats.values()) {
            if (chat.getOtherUsername().equals(recipientUsername)) {
                return chat.getUuid();
            }
        }
        return null;
    }
}
