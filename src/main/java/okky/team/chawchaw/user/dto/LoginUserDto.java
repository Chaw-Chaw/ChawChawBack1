package okky.team.chawchaw.user.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDto {

    String provider;
    String email;
    String password;
    String kakaoToken;
    String facebookId;
    String facebookToken;

}
