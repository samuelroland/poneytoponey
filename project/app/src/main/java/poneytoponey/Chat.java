package poneytoponey;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Chat {

    protected UUID uuid;
    protected boolean approved;
    protected List<Message> messages;

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

}
