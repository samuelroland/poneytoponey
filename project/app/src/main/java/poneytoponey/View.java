package poneytoponey;

public interface View {
    void start(String directoryHost);

    void showChatRequest(String from);

    void showChatClose(String from);

    void showChatRefuse(String from);

    void showChatApprobation(String from);

    void showChatMessage(String from);
}
