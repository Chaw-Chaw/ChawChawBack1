package okky.team.chawchaw.admin.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class DeleteProfileImageDto {

    @NotNull
    private Long userId;

}
