package okky.team.chawchaw.admin.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class UploadProfileImageDto {

    @NotNull
    private MultipartFile file;
    @NotNull
    private Long userId;

}
