package okky.team.chawchaw.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    GUEST("ROLE_GUEST", "1"),
    USER("ROLE_USER", "2"),
    ADMIN("ROLE_ADMIN", "3");

    private final String key;
    private final String value;
}
