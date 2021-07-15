package okky.team.chawchaw.social.Dto;

import lombok.Getter;

@Getter
public class KakaoAccessTokenDto {
    private String token_type;
    private String access_token;
    private String refresh_token;
    private String expires_in;
    private String refresh_token_expires_in;
    private String scope;
}
