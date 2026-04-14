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

    public HumanIdentity(String user) {
        this.username = user;
        this.views = new ArrayList<>();
        this.chats = new HashMap<>();
        this.knownParticipants = new HashMap<>();
    }

    public void approveChat(UUID chatID) throws RemoteException {
        Chat chat = chats.get(chatID);
        if (chat != null) {
            chat.setApproved(true);
            // utilisation de remote registry pour notifier
            Identity remote = findRemoteFromChat(chatID);
            if (remote != null) {
                remote.remoteapproveBackChat(chatID);
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
}