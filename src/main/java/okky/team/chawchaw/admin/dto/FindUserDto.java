package okky.team.chawchaw.admin.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter @Setter
@AllArgsConstructor
@Builder
public class FindUserDto {

    @Length(max = 20)
    String name;
    @Length(max = 255)
    String country;
    @Length(max = 2)
    String language;
    @Length(max = 2)
    String hopeLanguage;
    @Length(max = 20)
    String school;
    String order;
    String sort;
    @NotNull
    Integer pageNo;

}
