package okky.team.chawchaw.utils.message;

public enum ResponseUserMessage {

    U400("로그인 실패"),
    U401("비정상적인 로그인 접근"),
    U402("회원가입 필요"),
    U403("로그아웃 실패"),
    U404("비밀번호가 일치하지 않음"),
    U405("해당 이메일이 존재함"),
    U406("조회 결과가 존재하지 않음"),
    U407("프로필 업로드 실패"),
    U408("프로필 제거 실패");

    private final String message;

    ResponseUserMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}
