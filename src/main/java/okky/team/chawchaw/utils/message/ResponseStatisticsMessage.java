package okky.team.chawchaw.utils.message;

public enum ResponseStatisticsMessage {

    S400("조회 결과가 존재하지 않음");

    private final String message;

    ResponseStatisticsMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}
