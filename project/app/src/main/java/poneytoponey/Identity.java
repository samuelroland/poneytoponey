package poneytoponey;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface Identity extends Remote {

    public void remoteAskForChat(String author, UUID chatId) throws RemoteException, NotBoundException;

    public void remoteApproveBackChat(UUID chatId) throws RemoteException;

    public void remoteRefuseChat(UUID chatId) throws RemoteException;

    public void remoteSendMessageInChat(UUID chatId, String text, long sender) throws RemoteException;

    public void remoteCloseChat(UUID chatId) throws RemoteException;
}
