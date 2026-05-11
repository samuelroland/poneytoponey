package poneytoponey;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import crypto.KeyPair;
import crypto.RSA;

public class HumanIdentity implements Identity {

    private String username;
    private Map<UUID, Chat> chats;
    private Directory directory;
    private final KeyPair keyPair;
    private List<View> views;
    private Registry ourLocalRegistry;
    private String IDENTITY_BIND = "identity";
    private final ScheduledExecutorService watchAcks = Executors.newSingleThreadScheduledExecutor();    // D1

    public HumanIdentity(String user, Directory directory) {
        this.directory = directory;
        this.username = user;
        // Generate a new keypair or take an existing pair
        if (KeyPair.aPairExists()) {
            this.keyPair = KeyPair.load();
        } else {
            this.keyPair = generateKeyPair();
            this.keyPair.persistToFile();
        }
        if (System.getProperty("java.rmi.server.hostname") == null) {
            try {
                System.setProperty("java.rmi.server.hostname", resolveRmiHostname());
            } catch (UnknownHostException | SocketException e) {
                System.err.println("Cannot detect local RMI hostname: " + e.getMessage());
            }
        }
        this.views = new ArrayList<>();
        this.chats = new HashMap<>();
        try {
            // Try joining the network by publishing the current object to the our local RMI
            // registry
            ourLocalRegistry = LocateRegistry.createRegistry(poneytoponey.App.PORT);
            // We have to publish this object fist before binding it to the registry
            Identity stub = (Identity) UnicastRemoteObject.exportObject(this, poneytoponey.App.PORT);
            ourLocalRegistry.bind(IDENTITY_BIND, stub);
            try {
                this.directory.join(username, keyPair);
                // Register a hook to run at shutdown to make sure we leave() the directory
                // before quitting the app when running Ctrl+c!
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    leave();
                }));
            } catch (Exception e) {
                System.err.println(
                        "Cannot join network, either because your IP was already used or your username is already taken: "
                        + e.getMessage());
                System.exit(2);
            }
        } catch (AlreadyBoundException e) {
            System.err.println(e.getMessage());
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
    }

    public void leave() {
        System.out.println("Goodbye.");
        try {
            for (Chat chat : chats.values()) {
                closeChat(chat.getUuid());
            }
            this.directory.leave(keyPair);
        } catch (Exception e) {
            System.err.println("Failed to leave sorry, but byebye: " + e.getMessage());
        }
    }

    public String getUsername() {
        return this.username;
    }

    public List<String> listParticipantsUsername() {
        // We can extract the username of all members in the network by listing entries
        // on the directory and only keeping usernames
        try {
            return this.directory.list().stream()
                    .map(entry -> entry.username())
                    .toList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Optional<PublicKey> getParticipantPublicKey(String username) {
        try {
            Optional<Entry> maybeEntry = this.directory.list().stream()
                    .filter(entry -> entry.username().equals(username))
                    .findFirst();
            if (maybeEntry.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(maybeEntry.get().publicKey());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Identity getRemoteIdentityFromUsername(String recipient) throws Exception {
        Optional<Entry> maybeEntry = this.directory.list().stream().filter(entry -> entry.username().equals(recipient))
                .findFirst();
        if (maybeEntry.isEmpty()) {
            throw new Exception("This participant doesn't exist in the network !");
        }
        Entry entry = maybeEntry.get();
        String distantIP = entry.ip();

        Registry remoteRegistry = LocateRegistry.getRegistry(distantIP, App.PORT);
        return (Identity) remoteRegistry.lookup(IDENTITY_BIND);
    }

    public Chat createChat(String recipient) throws RemoteException, Exception {
        Identity remote = getRemoteIdentityFromUsername(recipient);
        Chat chat = new Chat(recipient);
        chats.put(chat.getUuid(), chat);
        remote.remoteAskForChat(this.username, chat.getUuid());
        return chat;
    }

    private Identity getRemoteIdentityFromChat(UUID chatID) throws Exception {
        String otherUsername = this.chats.get(chatID).getOtherUsername();
        return getRemoteIdentityFromUsername(otherUsername);
    }

    public void approveChat(UUID chatID) throws RemoteException, Exception {
        Chat chat = chats.get(chatID);
        if (chat != null) {
            chat.setApproved(true);
            Identity remote = getRemoteIdentityFromChat(chatID);
            if (remote != null) {
                remote.remoteApproveBackChat(chatID);
            }

        }
    }

    public void refuseChat(UUID oldChatID) throws RemoteException, Exception {
        Chat chat = chats.get(oldChatID);
        if (chat.getApproved() == true || chat != null) {
            Identity remote = getRemoteIdentityFromChat(oldChatID);
            chats.remove(oldChatID);
            if (remote != null) {
                remote.remoteRefuseChat(oldChatID);
            }
        }
    }

    public void subscribeViewForChatEvent(View view) {
        this.views.add(view);
    }

    // M1
    public void sendMessage(UUID chatID, String text, boolean prio) throws RemoteException, Exception {
        Identity remote = getRemoteIdentityFromChat(chatID);
        Chat chat = chats.get(chatID);
        if (chat != null && chat.getApproved() && text != null) {
            Message m = chat.insertNewMessage(text, this.username);
            chat.registerPendingAck(m.getUuid());
            if (remote != null) {
                remote.remoteSendMessageInChat(chatID, m.getTexte(), m.getSenderTimestamp(), prio);
            }
        }
    }

    public void closeChat(UUID chatID) throws RemoteException, Exception {
        Identity remote = getRemoteIdentityFromChat(chatID);
        if (chats.containsKey(chatID)) {
            chats.remove(chatID);
        }
        if (remote != null) {
            remote.remoteCloseChat(chatID);
        }
    }

    // ----- Identity -----
    public void remoteAskForChat(String author, UUID chatID) throws RemoteException, NotBoundException {
        chats.put(chatID, new Chat(author, chatID)); // save the non approved chat
        for (View view : views) {
            view.showChatRequest(author);
        }

        Thread timeout = new Thread(() -> {
            try {
                Thread.sleep(20_000);
                Chat chat = chats.get(chatID);
                if (chat != null && !chat.getApproved()) {
                    refuseChat(chatID);
                }
            } catch (Exception e) {
                System.err.println("Failed to auto-refuse chat request from " + author + ": " + e.getMessage());
            }
        });
        timeout.setDaemon(true);
        timeout.start();
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

        if (chat == null || chat.getApproved() == true) {
            return; // à revoir
        }
        chats.remove(chatID); // delete the chat as cannot do anything with it !

        for (View view : views) {
            view.showChatRefuse(chat.getOtherUsername());
        }

    }

    public void remoteSendMessageInChat(UUID chatID, String text, long senderTimestamp, boolean prio) {
        Chat chat = chats.get(chatID);

        if (chat == null) {
            return; // à revoir
        }

        Message msg;
        if (prio) {     // M1
            msg = chat.insertNewMessage("[IMPORTANT] " + text, chat.getOtherUsername());
            msg.setIsImportant(true);
        } else {
            msg = chat.insertNewMessage(text, chat.getOtherUsername());
        }

        for (View view : views) {
            view.showChatMessage(msg);
        }
    }

    public void remoteCloseChat(UUID chatID) {
        Chat chat = chats.get(chatID);

        if (chat == null) {
            return; // à revoir
        }

        for (View view : views) {
            view.showChatClose(chat.getOtherUsername());
        }

        chats.remove(chatID);
        chat.setApproved(false);
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

    private static String resolveRmiHostname() throws SocketException, UnknownHostException {
        var interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (!networkInterface.isUp() || networkInterface.isLoopback() || networkInterface.isVirtual()) {
                continue;
            }

            var addresses = networkInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
                    return address.getHostAddress();
                }
            }
        }

        return InetAddress.getLocalHost().getHostAddress();
    }

    // D1
    @Override
    public void remoteAcknowledgeMessage(UUID chatId, UUID messageId) throws RemoteException {
        Chat chat = chats.get(chatId);
        if (chat == null) {
            return;
        }
        chat.receiveAck(messageId);
    }

    // D1
    public void startWatchAcks() {
        watchAcks.scheduleAtFixedRate(() -> {
            for (Chat chat : chats.values()) {
                if (chat.getApproved() && chat.hasTimedOutAck()) {
                    autoDisconnect(chat);
                }
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

    // D1
    private void autoDisconnect(Chat chat) {
        chat.setApproved(false);
        chats.remove(chat.getUuid());
        for (View view : views) {
            view.showChatClose(chat.getOtherUsername());
        }
        try {
            directory.removeUser(chat.getOtherUsername(), keyPair);
        } catch (Exception e) {
            System.err.println("[WATCHDOG] Impossible de désinscrire " + chat.getOtherUsername() + " : " + e.getMessage());
        }
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    private static KeyPair generateKeyPair() {
        try {
            return new RSA().generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate identity key pair", e);
        }
    }

    // D1
    public void stopWatchAcks() {
        watchAcks.shutdown();
        try {
            if (!watchAcks.awaitTermination(3, TimeUnit.SECONDS)) {
                watchAcks.shutdownNow();
            }
        } catch (InterruptedException e) {
            watchAcks.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
