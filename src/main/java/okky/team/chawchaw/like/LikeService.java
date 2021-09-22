package okky.team.chawchaw.like;

import okky.team.chawchaw.like.dto.LikeMessageDto;
import okky.team.chawchaw.user.UserEntity;

import java.util.List;

public interface LikeService {

    /**
     * 팔로우
     * @param userFrom
     * @param userTo
     * @return FollowDto
     */
    LikeMessageDto addLike(UserEntity userFrom, Long userTo);

    /**
     * 언팔로우
     * @param userFrom
     * @param userTo
     * @return FollowDto
     */
    LikeMessageDto deleteLike(UserEntity userFrom, Long userTo);

    List<LikeMessageDto> findMessagesByUserId(Long userId);

    Boolean isLike(Long userFrom, Long userTo);

}
