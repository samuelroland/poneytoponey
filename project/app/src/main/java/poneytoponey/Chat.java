package poneytoponey;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Chat {

    private UUID uuid;
    private boolean approved;
    private List<Message> messages;
    private String otherUsername; // the username of the remote client

    public Chat(String otherUsername) {
        this.uuid = UUID.randomUUID();
        this.approved = false;
        this.messages = new ArrayList<>();
        this.otherUsername = otherUsername;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getOtherUsername() {
        return this.otherUsername;
    }

    public boolean getApproved() {
        return this.approved;
    }

    public void setApproved(boolean t) { // est ce que ca dérange si j'ajoute ca
        this.approved = t;
    }

    public Message insertNewMessage(String text, String author) {
        long ts = System.currentTimeMillis();
        Message m = new Message(text, ts, messages.size(), author);
        messages.add(m);
        return m;
    }

    public List<Message> getMessages() {
        return this.messages;
    }

}
