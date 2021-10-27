package okky.team.chawchaw.utils.message;

public enum ResponseLikeMessage {

    L400("이미 좋아요 대상"),
    L401("좋아요 하지 않은 대상");

    private final String message;

    ResponseLikeMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}
