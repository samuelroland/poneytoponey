package poneytoponey;

import java.time.Instant;
import java.util.UUID;
import java.io.Serializable; //D2

public class Message {

    private static final long serialVersionUID = 1L; // D2 // lui aussi en sérialisable pour pouvoir etre mis en bytes
                                                     // dans le disque

    private String texte;
    private long senderTimestamp;
    private Integer index;
    private String author;
    public boolean isImportant; // M1
    private Instant timestamp; // D1
    private final UUID uuid; // D1

    public Message(String texte, long senderTimestamp, Integer index, String author) {
        this.texte = texte;
        this.senderTimestamp = senderTimestamp;
        this.index = index;
        this.author = author;
        this.isImportant = false; // M1
        this.uuid = UUID.randomUUID(); // D1;
    }

    public String getTexte() {
        return this.texte;
    }

    public long getSenderTimestamp() {
        return this.senderTimestamp;
    }

    public Integer getIndex() {
        return this.index;
    }

    public String getAuthor() {
        return this.author;
    }

    // M1
    public boolean getIsImportant() {
        return this.isImportant;
    }

    // M1
    public void setIsImportant(boolean bool) {
        this.isImportant = bool;
    }

    // D1
    public UUID getUuid() {
        return this.uuid;
    }
}
