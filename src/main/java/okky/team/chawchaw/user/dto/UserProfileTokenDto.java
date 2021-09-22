package okky.team.chawchaw.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileTokenDto {

    UserProfileDto profile;
    TokenDto token;
    List<Long> blockIds;

}
