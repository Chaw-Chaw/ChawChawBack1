package okky.team.chawchaw.user.dto;

import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindUserVo {

    String name;
    String country;
    String language;
    String hopeLanguage;
    String order;

}
