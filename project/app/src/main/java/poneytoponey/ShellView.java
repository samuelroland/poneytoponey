package poneytoponey;

import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.UUID;

public class ShellView implements View {
    private UUID currentChat;
    private boolean joinedNetwork;
    private HumanIdentity identity;

    private void join(String username) {

    }

    private void showHelp() {

    }

    private void createSwitchChat(String recipient) {

    }

    private void closeChat(String recipient) {

    }

    private void refuseChat(String recipient) {

    }

    private void sendMessage(String text) {

    }

    private void listParticipants() {

    }

    private void waitWithShellPrompt() {
    }

    private void parseCommand() {
    }

    @Override
    public void start() {
        System.out.println("Welcome to the PoneyToPoney peer-to-peer system !");

        // TODO: move this code inside join() when the commands parsing system work
        // TODO: make sure that no other command can be run when we didn't joined the
        // network

        Scanner scanner = new Scanner(System.in);
        System.out.print("Please choose a username to join the network: ");
        String username = scanner.nextLine();

        try {
            this.identity = new HumanIdentity(username);
        } catch (RemoteException e) {
            System.err.println("A username already exists in the network...");
            System.err.println(e.getMessage());
        }
        scanner.close();
    }

    @Override
    public void showChatRequest(String from) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'showChatRequest'");
    }

    @Override
    public void showChatClose(String from) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'showChatClose'");
    }

    @Override
    public void showChatRefuse(String from) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'showChatRefuse'");
    }

}
