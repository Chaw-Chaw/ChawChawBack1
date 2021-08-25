package okky.team.chawchaw.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDto {

    String provider;
    String email;
    String password;
    String kakaoToken;
    String facebookToken;

}
