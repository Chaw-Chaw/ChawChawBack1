package okky.team.chawchaw.user.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class UserCardDto {

    Long id;
    String name;
    String imageUrl;
    String content;
    String repCountry;
    String repLanguage;
    String repHopeLanguage;
    LocalDateTime days;
    Long views;
    Integer likes;

    public UserCardDto(Long id, String name, String imageUrl, String content, String repCountry, String repLanguage, String repHopeLanguage, LocalDateTime days, Integer likes) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.content = content;
        this.repCountry = repCountry;
        this.repLanguage = repLanguage;
        this.repHopeLanguage = repHopeLanguage;
        this.days = days;
        this.likes = likes;
    }
}
