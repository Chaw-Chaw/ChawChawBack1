package okky.team.chawchaw.follow;

import okky.team.chawchaw.follow.dto.FollowMessageDto;
import okky.team.chawchaw.user.UserEntity;

import java.util.List;

public interface FollowService {

    /**
     * 팔로우
     * @param userFrom
     * @param userTo
     * @return FollowDto
     */
    FollowMessageDto addFollow(UserEntity userFrom, Long userTo);

    /**
     * 언팔로우
     * @param userFrom
     * @param userTo
     * @return FollowDto
     */
    FollowMessageDto deleteFollow(UserEntity userFrom, Long userTo);

    List<FollowMessageDto> findMessagesByUserId(Long userId);

    Boolean isFollow(Long userFrom, Long userTo);

}
