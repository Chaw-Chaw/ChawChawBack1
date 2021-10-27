package okky.team.chawchaw.utils.message;

public enum ResponseBlockMessage {

    B400("이미 차단된 대상"),
    B401("차단되지 않은 대상");

    private final String message;

    ResponseBlockMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() { return message; }

}
