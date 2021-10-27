package okky.team.chawchaw.utils.message;

public enum ResponseMailMessage {

    E400("대학생용 이메일이 아닙니다."),
    E401("인증번호 검증 실패"),
    E402("인증번호 시간이 만료됨");

    private final String message;

    ResponseMailMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}
