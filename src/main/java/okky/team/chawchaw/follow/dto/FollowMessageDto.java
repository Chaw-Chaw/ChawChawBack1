package okky.team.chawchaw.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import okky.team.chawchaw.follow.FollowType;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowMessageDto implements Serializable {

    FollowType followType;
    String name;
    LocalDateTime regDate;

}
