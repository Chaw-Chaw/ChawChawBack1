package okky.team.chawchaw.admin.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindUserDto {

    @Length(max = 20)
    private String name;
    @Length(max = 255)
    private String country;
    @Length(max = 2)
    private String language;
    @Length(max = 2)
    private String hopeLanguage;
    @Length(max = 20)
    private String school;
    private String order;
    private String sort;
    @NotNull
    private Integer pageNo;
}
