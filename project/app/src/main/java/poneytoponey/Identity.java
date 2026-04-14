package poneytoponey;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface Identity extends Remote {

    public void remoteAskForChat(String author, UUID chatId) throws RemoteException;

    public void remoteApproveBackChat(UUID chatId) throws RemoteException;

    public void remoteRefuseChat(UUID chatId) throws RemoteException;

    public void remoteSendMessageInChat(UUID chatId) throws RemoteException;

    public void remoteCloseChat(UUID chatId) throws RemoteException;
}
