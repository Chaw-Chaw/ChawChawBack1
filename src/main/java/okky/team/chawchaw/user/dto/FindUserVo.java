package okky.team.chawchaw.user.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    String school;
    Integer pageNo;
    List<Long> exclude = new ArrayList<>();

}
