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
    LocalDateTime regDate;
    Long views;
    Integer likes;

    public UserCardDto(Long id, String name, String imageUrl, String content, String repCountry, String repLanguage, String repHopeLanguage, LocalDateTime regDate, Integer likes) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.content = content;
        this.repCountry = repCountry;
        this.repLanguage = repLanguage;
        this.repHopeLanguage = repHopeLanguage;
        this.regDate = regDate;
        this.likes = likes;
    }
}
