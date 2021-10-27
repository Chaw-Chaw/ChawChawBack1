package okky.team.chawchaw.utils.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultResponseVo<S, T> {

    private S status;
    private String message;
    private T data;

    public DefaultResponseVo(S status, String message) {
        this.status = status;
        this.message = message;
    }

    public static<S, T> DefaultResponseVo<S, T> res(final S status) {
        return res(status, null);
    }

    public static<S, T> DefaultResponseVo<S, T> res(final S status, final T t) {
        return DefaultResponseVo.<S, T>builder()
                .status(status)
                .message(status.toString())
                .data(t)
                .build();
    }

}
