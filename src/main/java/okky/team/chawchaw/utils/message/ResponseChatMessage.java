package okky.team.chawchaw.utils.message;

public enum ResponseChatMessage {

    C400("방 이동 실패"),
    C401("채팅방 존재함"),
    C402("채팅방 존재하지 않음"),
    C403("채팅방 이미지 업로드 실패");

    private final String message;

    ResponseChatMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}
