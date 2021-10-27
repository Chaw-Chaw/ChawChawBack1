package okky.team.chawchaw.utils.message;

public enum ResponseTokenMessage {

    T400("Access Token 만료"),
    T401("Access Token 형식이 잘못됨"),
    T402("Refresh Token 존재하지 않음"),
    T403("사용할 수 없는 Refresh Token"),
    T404("Refresh Token 만료"),
    T405("Refresh Token 형식이 잘못됨");

    private final String message;

    ResponseTokenMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}
