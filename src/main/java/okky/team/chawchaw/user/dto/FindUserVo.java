package okky.team.chawchaw.user.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
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
    @NotNull
    Boolean isFirst;
    List<Long> exclude = new ArrayList<>();

}
