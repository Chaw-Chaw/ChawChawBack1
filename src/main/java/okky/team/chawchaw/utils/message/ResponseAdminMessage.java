package okky.team.chawchaw.utils.message;

public enum ResponseAdminMessage {

    A401("이미 차단된 대상"),
    A402("차단되지 않은 대상"),
    A403("프로필 업로드 실패"),
    A404("프로필 제거 실패");

    private final String message;

    ResponseAdminMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() { return message; }

}
