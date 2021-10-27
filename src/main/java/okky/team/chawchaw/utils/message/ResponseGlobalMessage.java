package okky.team.chawchaw.utils.message;

public enum ResponseGlobalMessage {

    G200("성공"),
    G400("해당 아이디가 존재하지 않음"),
    G401("자기 자신 선택 불가능"),
    G402("DB 오류"),
    G403("차단한 또는 차단된 유저"),
    G404("다른 곳에서 접속함"),
    G405("파일 타입이 잘못됨"),
    G406("요청 파라미터가 잘못됨");

    private final String message;

    ResponseGlobalMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}
