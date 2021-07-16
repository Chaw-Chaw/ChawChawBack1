package okky.team.chawchaw.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class RequestUserVo {

    Long id;
    String name;
    String country;
    String language;
    String hopeLanguage;
    String order;
    String socialUrl;
    String imageUrl;

}
