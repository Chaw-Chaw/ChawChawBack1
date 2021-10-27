package okky.team.chawchaw.user.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindUserVo {

    @Length(max = 20)
    String name;
    @Length(max = 255)
    String country;
    @Length(max = 2)
    String language;
    @Length(max = 2)
    String hopeLanguage;
    String sort;
    @Length(max = 20)
    String school;
    @NotNull
    Boolean isFirst;
    List<Long> exclude = new ArrayList<>();

}
