package okky.team.chawchaw.user.dto;

import lombok.*;

import java.util.List;
import java.util.Set;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestUserVo {

    Long id;
    String email;
    String password;
    String name;
    Set<String> country;
    Set<String> language;
    Set<String> hopeLanguage;
    String content;
    String school;
    String web_email;
    String facebookUrl;
    String instagramUrl;
    String imageUrl;
    String repCountry;
    String repLangugae;
    String repHopeLangugae;

}
