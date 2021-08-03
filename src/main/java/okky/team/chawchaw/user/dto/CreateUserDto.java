package okky.team.chawchaw.user.dto;

import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {

    private String email;
    private String password;
    private String name;
    private String web_email;
    private String school;
    private String imageUrl;
    private String provider;

}
