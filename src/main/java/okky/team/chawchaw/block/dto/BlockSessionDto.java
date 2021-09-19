package okky.team.chawchaw.block.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockSessionDto implements Serializable {

    Long userId;
    LocalDateTime regDate;

}
