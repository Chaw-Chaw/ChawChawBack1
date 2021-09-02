package okky.team.chawchaw.follow;

import okky.team.chawchaw.user.UserEntity;

import java.util.List;

public interface FollowService {

    /**
     * 팔로우
     * @param userFrom
     * @param userTo
     * @return Boolean
     */
    Boolean addFollow(UserEntity userFrom, Long userTo);

    /**
     * 언팔로우
     * @param userFrom
     * @param userTo
     * @return Boolean
     */
    Boolean deleteFollow(UserEntity userFrom, Long userTo);

    Boolean isFollow(Long userFrom, Long userTo);

}
