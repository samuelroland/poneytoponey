package poneytoponey;

import java.rmi.Remote;
import java.util.UUID;

public interface Identity extends Remote {

    public void remoteAskForChat(String author, UUID chatId);

    public void remoteApproveBackChat(UUID chatId);

    public void remoteRefuseChat(UUID chatId);

    public void remoteSendMessageInChat(UUID chatId);

    public void remoteCloseChat(UUID chatId);
}
