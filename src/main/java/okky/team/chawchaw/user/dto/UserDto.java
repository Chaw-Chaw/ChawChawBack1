package okky.team.chawchaw.user.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String web_email;
    private String school;
    private String imageUrl;
    private String content;
    private String country;
    private String language;
    private String hopeLanguage;
    private String socialUrl;
    private LocalDateTime regDate;
    private Long views;

}
