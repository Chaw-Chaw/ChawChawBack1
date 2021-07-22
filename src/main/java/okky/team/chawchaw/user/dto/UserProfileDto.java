package okky.team.chawchaw.user.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    String imageUrl;
    String content;
    List<String> country;
    List<String> language;
    List<String> hopeLanguage;
    String facebookUrl;
    String instagramUrl;

}
