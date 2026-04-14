package poneytoponey;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Chat {

    private UUID uuid;
    private boolean approved;
    private List<Message> messages;

    public Chat() {
        this.uuid = UUID.randomUUID();
        this.approved = false;
        this.messages = new ArrayList<>();
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public boolean getApproved() {
        return this.approved;
    }

    public void setApproved(boolean t) { // est ce que ca dérange si j'ajoute ca
        this.approved = t;
    }

}
