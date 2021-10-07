package okky.team.chawchaw.admin.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter @Setter
@AllArgsConstructor
@Builder
public class FindUserDto {

    String name;
    String country;
    String language;
    String hopeLanguage;
    String school;
    String order;
    String sort;
    @NotNull
    Integer pageNo;

}
