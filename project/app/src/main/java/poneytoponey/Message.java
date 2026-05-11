package poneytoponey;

import java.util.UUID;

public class Message {

    private String texte;
    private long senderTimestamp; // D1
    private String author;
    private boolean isImportant; // M1
    private final UUID uuid; // D1

    public Message(String texte, long senderTimestamp, String author, boolean isImportant) {
        this.texte = texte;
        this.senderTimestamp = senderTimestamp;
        this.author = author;
        this.isImportant = isImportant; // M1
        this.uuid = UUID.randomUUID(); // D1;
    }

    public String getTexte() {
        return this.texte;
    }

    public Long getSenderTimestamp() {
        return this.senderTimestamp;
    }

    public String getAuthor() {
        return this.author;
    }

    // M1
    public Boolean getIsImportant() {
        return this.isImportant;
    }

    // D1
    public UUID getUuid() {
        return this.uuid;
    }
}
