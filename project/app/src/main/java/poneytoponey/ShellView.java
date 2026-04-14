package poneytoponey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class ShellView implements View {
    private UUID currentChat;
    private String currentChatRecipient;
    private boolean joinedNetwork;
    private HumanIdentity identity;
    private Scanner scanner = new Scanner(System.in);

    // TODO: a refactor is needed here to avoid having 2 list of Chat !
    // It seems we need to have easy access to recipient -> Chat here, but easy
    // access of Uuid -> Chat in HumanIdentity.
    // Maybe we should create getters/setter on HumanIdentity to keep the source of
    // truth over there
    // and create a mapping recipient -> Uuid here for ease of access and an
    // O(log(N)) complexity to access a chat.
    // This would also delete this attribute below
    private Map<String, Chat> chats = new HashMap<>();

    private void join(String username) {
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Username cannot be empty.");
            return;
        }

        if (joinedNetwork) {
            System.out.println("Already joined as " + identity.getUsername() + ".");
            return;
        }

        this.identity = new HumanIdentity(username.trim());
        this.identity.subscribeViewForChatEvent(this);
        this.joinedNetwork = true;
        System.out.println("Joined network as " + username.trim() + ".");
    }

    public void showHelp() {
        System.out.println("Available commands:"
                + "\n  join <username>       - join the network as a user"
                + "\n  list                  - list known participants"
                + "\n  chat <recipient>      - create or switch to a chat"
                + "\n  switch <recipient>    - switch to an existing chat"
                + "\n  send <message>        - send a message to the active chat"
                + "\n  history               - show chat history of current chat"
                + "\n  close <recipient>     - close a chat with a recipient"
                + "\n  refuse <recipient>    - refuse a chat request or discard a chat"
                + "\n  status                - show current chat status"
                + "\n  help                  - show this help text"
                + "\n  exit | quit           - leave the shell");
    }

    private void createSwitchChat(String recipient) {
        if (!joinedNetwork) {
            System.out.println("You must join the network first.");
            return;
        }

        if (recipient == null || recipient.trim().isEmpty()) {
            System.out.println("Recipient cannot be empty.");
            return;
        }

        recipient = recipient.trim();
        Chat chat = chats.get(recipient);

        if (chat == null) {
            try {
                chat = identity.createChat(recipient);
            } catch (Exception e) {
                System.out.println("Unable to create chat with " + recipient + ": " + e.getMessage());
                return;
            }
            chats.put(recipient, chat);
            System.out.println("Created chat with " + recipient + ".");
        } else {
            System.out.println("Switched to existing chat with " + recipient + ".");
        }

        this.currentChat = chat.getUuid();
        this.currentChatRecipient = recipient;
    }

    private void closeChat(String recipient) {
        if (recipient == null || recipient.trim().isEmpty()) {
            System.out.println("Recipient cannot be empty.");
            return;
        }

        recipient = recipient.trim();
        Chat chat = chats.remove(recipient);

        // TODO : Actually close the chat, waiting for imple
        // Block sychronised ?

        if (chat == null) {
            System.out.println("No chat found with " + recipient + ".");
            return;
        }

        if (recipient.equals(currentChatRecipient)) {
            currentChat = null;
            currentChatRecipient = null;
        }

        System.out.println("Closed chat with " + recipient + ".");
    }

    private void approveChat(String recipient) {
        recipient = recipient.trim();
        if (recipient == null || recipient.isEmpty()) {
            System.out.println("Recipient cannot be empty.");
            return;
        }

        if (chats.get(recipient) != null) {
            Chat existingChat = chats.get(recipient);
            existingChat.setApproved(true);
            currentChat = existingChat.getUuid();
            currentChatRecipient = existingChat.getOtherUsername();
            System.out.println("Approved chat with " + recipient + ".");
        } else {
            System.out.println("No requested chat with " + recipient + ", you cannot approve an non existant chat.");
        }
    }

    private void refuseChat(String recipient) {
        if (recipient == null || recipient.trim().isEmpty()) {
            System.out.println("Recipient cannot be empty.");
            return;
        }

        recipient = recipient.trim();
        if (chats.containsKey(recipient)) {
            chats.remove(recipient);
            if (recipient.equals(currentChatRecipient)) {
                currentChat = null;
                currentChatRecipient = null;
            }
            System.out.println("Refused chat with " + recipient + ".");
        } else {
            System.out.println("No chat request or chat found for " + recipient + ".");
        }
    }

    private void sendMessage(String text) {
        if (!joinedNetwork) {
            System.out.println("You must join the network first.");
            return;
        }

        if (currentChat == null || currentChatRecipient == null) {
            System.out.println("No active chat selected. Use chat <recipient> first.");
            return;
        }

        if (text == null || text.trim().isEmpty()) {
            System.out.println("Cannot send an empty message.");
            return;
        }

        Chat chat = chats.get(currentChatRecipient);
        if (chat == null) {
            System.out.println("Current chat is no longer available.");
            currentChat = null;
            currentChatRecipient = null;
            return;
        }

        chat.insertNewMessage(text.trim(), identity.getUsername());
        System.out.println("Sent message to " + currentChatRecipient + ": " + text.trim());
    }

    private void listParticipants() {
        if (!joinedNetwork) {
            System.out.println("You must join the network first.");
            return;
        }

        List<String> participants = identity.listParticipantsUsername();
        if (participants.isEmpty()) {
            System.out.println("No participants found.");
            return;
        }

        System.out.println("Known participants:");
        for (String participant : participants) {
            System.out.println("  - " + participant);
        }
    }

    private void showPrompt() {
        System.out.print("P2P> ");
    }

    private void waitWithShellPrompt() {
        while (true) {
            showPrompt();
            String line = scanner.nextLine();
            parseCommand(line);
        }
    }

    private void parseCommand(String line) {
        if (line == null || line.trim().isEmpty()) {
            return;
        }

        String[] tokens = line.trim().split(" ", 2);
        String command = tokens[0].toLowerCase();
        String argument = tokens.length > 1 ? tokens[1] : null;

        switch (command) {
            case "join" -> join(argument);
            case "list" -> listParticipants();
            case "chat" -> createSwitchChat(argument);
            case "switch" -> {
                if (argument == null || argument.trim().isEmpty()) {
                    System.out.println("Usage: switch <recipient>");
                } else if (!chats.containsKey(argument.trim())) {
                    System.out.println("No existing chat with " + argument.trim() + ".");
                } else {
                    currentChatRecipient = argument.trim();
                    currentChat = chats.get(currentChatRecipient).getUuid();
                    System.out.println("Switched to chat with " + currentChatRecipient + ".");
                }
            }
            case "send" -> sendMessage(argument);
            case "history" -> showHistory();
            case "close" -> closeChat(argument);
            case "approve" -> approveChat(argument);
            case "refuse" -> refuseChat(argument);
            case "status" -> {
                if (!joinedNetwork) {
                    System.out.println("Not joined.");
                } else if (currentChatRecipient == null) {
                    System.out.println("Joined as " + identity.getUsername() + ". No active chat.");
                } else {
                    System.out.println(
                            "Joined as " + identity.getUsername() + ". Active chat with " + currentChatRecipient + ".");
                }
            }
            case "help" -> showHelp();
            case "exit", "quit" -> {
                System.out.println("Goodbye.");
                System.exit(0);
            }
            default -> System.out.println("Unknown command: " + command + ". Type help for available commands.");
        }
    }

    @Override
    public void start() {
        System.out.println("Welcome to the PoneyToPoney peer-to-peer system !");
        System.out.print("Please choose a username to join the network: ");
        String username = scanner.nextLine();
        join(username);

        showHelp();
        waitWithShellPrompt();
    }

    @Override
    public void showChatRequest(String from) {
        System.out.println("Incoming chat request from " + from + ".");
        showPrompt();
    }

    @Override
    public void showChatClose(String from) {
        System.out.println("Chat closed by " + from + ".");
        showPrompt();
    }

    @Override
    public void showChatApprobation(String from) {
        System.out.println("Chat request approved by " + from + ".");
        showPrompt();
    }

    @Override
    public void showChatRefuse(String from) {
        System.out.println("Chat refused by " + from + ".");
        showPrompt();
    }

    @Override
    public void showChatMessage(String from) {
        System.out.println("New message available from " + from + ".");
        showPrompt();
    }

    private void showHistory() {
        if (currentChatRecipient == null) {
            System.out.println("No active chat. Use chat <recipient> to select a chat.");
        } else {
            Chat chat = chats.get(currentChatRecipient);
            if (chat == null) {
                System.out.println("Current chat is no longer available.");
            } else {
                List<Message> messages = chat.getMessages();
                if (messages.isEmpty()) {
                    System.out.println("No messages in chat with " + currentChatRecipient + ".");
                } else {
                    System.out.println("History for chat with " + currentChatRecipient + ":");
                    for (Message message : messages) {
                        System.out.println("  [" + message.getAuthor() + "] " + message.getTexte());
                    }
                }
            }
        }
    }

}
