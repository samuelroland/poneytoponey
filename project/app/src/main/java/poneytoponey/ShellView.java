package poneytoponey;

import java.util.UUID;

public class ShellView implements View {
    private UUID currentChat;
    private boolean joinedNetwork;

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
    public void start(HumanIdentity humanIdentity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'start'");
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
