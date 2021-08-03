package okky.team.chawchaw.user.dto;

import lombok.*;

import java.util.Set;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    private Long id;
    private Set<String> country;
    private Set<String> language;
    private Set<String> hopeLanguage;
    private String content;
    private String facebookUrl;
    private String instagramUrl;
    private String repCountry;
    private String repLanguage;
    private String repHopeLanguage;

}
