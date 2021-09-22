package okky.team.chawchaw.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import okky.team.chawchaw.like.LikeType;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeMessageDto implements Serializable {

    LikeType likeType;
    String name;
    LocalDateTime regDate;

}
