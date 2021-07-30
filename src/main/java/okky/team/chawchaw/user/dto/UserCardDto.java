package okky.team.chawchaw.user.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCardDto {

    Long id;
    String imageUrl;
    String content;
    String repCountry;
    String repLanguage;
    String repHopeLanguage;
    LocalDateTime days;
    Long views;
    Integer follows;

}
