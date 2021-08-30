package okky.team.chawchaw.user.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    private Long id;
    @NotNull
    private Set<String> country;
    @NotNull
    private Set<String> language;
    @NotNull
    private Set<String> hopeLanguage;
    @NotNull
    @Length(max = 2000)
    private String content;
    @NotNull
    @Length(max = 255)
    private String facebookUrl;
    @NotNull
    @Length(max = 255)
    private String instagramUrl;
    @NotBlank
    @Length(max = 255)
    private String repCountry;
    @NotBlank
    @Length(max = 2)
    private String repLanguage;
    @NotBlank
    @Length(max = 2)
    private String repHopeLanguage;

}
