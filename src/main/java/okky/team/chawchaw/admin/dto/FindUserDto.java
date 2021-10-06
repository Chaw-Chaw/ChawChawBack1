package okky.team.chawchaw.admin.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
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
