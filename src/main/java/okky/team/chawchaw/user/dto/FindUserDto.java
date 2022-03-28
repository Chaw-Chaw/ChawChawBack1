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
public class FindUserDto {

    @Length(max = 20)
    private String name;
    @Length(max = 255)
    private String country;
    @Length(max = 2)
    private String language;
    @Length(max = 2)
    private String hopeLanguage;
    private String sort;
    @Length(max = 20)
    private String school;
    @NotNull
    private Boolean isFirst;
    private List<Long> exclude = new ArrayList<>();
}
