package poneytoponey;

import java.sql.Timestamp;

public class Message {

    private String texte;
    private Timestamp senderTimestamp;
    private Integer index;
    // c'est bien integer ou int ?
    private String author;

    public Message(String texte, Timestamp senderTimestamp, Integer index, String author) {
        this.texte = texte;
        this.senderTimestamp = senderTimestamp;
        this.index = index;
        this.author = author;
    }

    public String getTexte() {
        return this.texte;
    }

    public Timestamp getSenderTimestamp() {
        return this.senderTimestamp;
    }

    public Integer getIndex() {
        return this.index;
    }

    public String getAuthor() {
        return this.author;
    }
}
