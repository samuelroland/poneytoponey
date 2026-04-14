package poneytoponey;

public interface View {
    void start();

    void showChatRequest(String from);

    void showChatClose(String from);

    void showChatRefuse(String from);

    void showChatMessage(String from);
}
