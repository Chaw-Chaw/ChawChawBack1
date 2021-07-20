package okky.team.chawchaw.user.dto;

import lombok.*;

import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserVo {

    String email;
    String password;
    String name;
    List<String> country;
    List<String> language;
    List<String> hopeLanguage;
    String content;
    String school;
    String web_email;
    String order;
    String facebookUrl;
    String instagramUrl;
    String imageUrl;

}
