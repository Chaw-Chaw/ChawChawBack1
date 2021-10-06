package okky.team.chawchaw.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteBlockDto {

    @NotNull
    private Long userFromId;
    private String userFromEmail;
    @NotNull
    private Long userToId;

}
