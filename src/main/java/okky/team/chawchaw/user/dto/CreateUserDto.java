package okky.team.chawchaw.user.dto;

import lombok.*;
import okky.team.chawchaw.user.UserEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {

    @NotBlank
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$")
    private String email;
    @Pattern(regexp = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")
    private String password;
    @Length(max = 20)
    private String name;
    @NotBlank
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@([-_.]?[0-9a-zA-Z])+\\.ac\\.kr$")
    private String web_email;
    @NotBlank
    @Length(max = 20)
    private String school;
    private String imageUrl;
    @NotNull
    @Length(max = 8)
    private String provider;

    public UserEntity dtoToUserEntity() {
        return UserEntity.builder()
                .email(email)
                .password(password)
                .name(name)
                .web_email(web_email)
                .school(school)
                .imageUrl(imageUrl)
                .build();
    }

}
