package poneytoponey;

public interface View {
    void start(HumanIdentity humanIdentity);

    void showChatRequest(String from);

    void showChatClose(String from);

    void showChatRefuse(String from);
}
