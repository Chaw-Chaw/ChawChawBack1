package okky.team.chawchaw.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteLikeDto {

    private Long userFromId;
    private Long userId;

}
