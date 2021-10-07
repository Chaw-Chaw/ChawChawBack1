package okky.team.chawchaw.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCardDto {

    Long id;
    String name;
    String school;
    String email;
    String repCountry;
    String repLanguage;
    String repHopeLanguage;
    Integer likes;
    Long views;
    LocalDateTime regDate;

}
