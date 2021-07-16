package okky.team.chawchaw.user.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDetailsDto {

    Long id;
    String name;
    String imageUrl;
    String content;
    String country;
    String language;
    String hopeLanguage;
    String socialUrl;
    LocalDateTime days;
    Long views;
    Long follows;

}
