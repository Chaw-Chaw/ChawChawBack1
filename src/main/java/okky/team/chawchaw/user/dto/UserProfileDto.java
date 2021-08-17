package okky.team.chawchaw.user.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto implements Serializable {

    private Long id;
    private String name;
    private String school;
    private String imageUrl;
    private String content;
    private String repCountry;
    private String repLanguage;
    private String repHopeLanguage;
    private List<String> country;
    private List<String> language;
    private List<String> hopeLanguage;
    private String facebookUrl;
    private String instagramUrl;

}
