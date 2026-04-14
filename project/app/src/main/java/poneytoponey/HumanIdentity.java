package poneytoponey;

import java.rmi.RemoteException;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.text.View;

import java.sql.Timestamp;

public class HumanIdentity implements Identity {

    private String username;
    private Map<String, Identity> knownParticipants; // cache
    private Map<UUID, Chat> chats; // ajoutés dans la liste par createChat ?

    private List<View> views;
    private Registry remoteRegistry;

    public HumanIdentity(String user) {
        this.username = user;
        this.views = new ArrayList<>();
        this.chats = new HashMap<>();
        this.knownParticipants = new HashMap<>();
        try {
            this.remoteRegistry = LocateRegistry.getRegistry(poneytoponey.App.PORT);
            Identity stub = (Identity) UnicastRemoteObject.exportObject(this, 0);

            this.remoteRegistry.bind(user, stub);
        } catch (AlreadyBoundException e) {
            System.err.println("A username already exists in the network...");
            System.err.println(e.getMessage());
        } catch (RemoteException e) {
            System.err.println("Cannot connect to RMI registry on port " + poneytoponey.App.PORT);
            System.err.println(e.getMessage());
        }
    }

    public void approveChat(UUID chatID) throws RemoteException {
        Chat chat = chats.get(chatID);
        if (chat != null) {
            chat.setApproved(true);
            // utilisation de remote registry pour notifier
            Identity remote = findRemoteFromChat(chatID);
            if (remote != null) {
                remote.remoteApproveBackChat(chatID);
            }
            // notifyViewsChatOpened(chatID);
        }
    }

    private void subscribeViewForChatEvent(View view) {
        this.views.add(view);
    }

    private void sendMessage(UUID chatID, String text) throws RemoteException {
        Identity remote = findRemoteFromChat(chatID);
        Chat chat = chats.get(chatID);
        if (chat != null && chat.getApproved() && text != null) {
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            if (remote != null) {
                remote.sendMessage(chatID, text, ts);
            }
            Message m = new Message(text, ts, chat.messages.size(), this.username);
            chat.messages.add(m);
            // notifyViewsMessage(chatID,m);
        }

    }

    private void closeChat(UUID chatID) throws RemoteException {
        Identity remote = findRemoteFromChat(chatID);
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
            chat = new Chat();
            chats.put(chat.getUuid(), chat);
        }

        chat.setApproved(true);

        for (View view : views) {
            view.start(this);
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

    public void remoteSendMessageInChat(UUID chatId) {
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
