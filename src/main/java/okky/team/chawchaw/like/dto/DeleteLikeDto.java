package okky.team.chawchaw.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteLikeDto {

    private Long userFromId;
    @NotNull
    private Long userId;

}
