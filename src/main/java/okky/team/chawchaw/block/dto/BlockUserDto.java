package okky.team.chawchaw.block.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockUserDto {

    Long userId;
    String name;
    String imageUrl;

}
