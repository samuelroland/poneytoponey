package poneytoponey;

public class Message {

    private String texte;
    private long senderTimestamp;
    private Integer index;
    // c'est bien integer ou int ?
    private String author;
    public boolean isImportant;

    public Message(String texte, long senderTimestamp, Integer index, String author) {
        this.texte = texte;
        this.senderTimestamp = senderTimestamp;
        this.index = index;
        this.author = author;
        this.isImportant = false;
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

    public boolean getIsImportant() {
        return this.isImportant;
    }

    public void setIsImportant(boolean bool){
        this.isImportant = bool;
    }
}
