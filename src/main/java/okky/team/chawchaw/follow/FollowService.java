package okky.team.chawchaw.follow;

import okky.team.chawchaw.user.UserEntity;

public interface FollowService {

    /**
     * 팔로우
     * @param userFrom
     * @param userTo
     * @return followId(Long)
     */
    public Long addFollow(UserEntity userFrom, Long userTo);

    /**
     * 언팔로우
     * @param userFrom
     * @param userTo
     */
    public void deleteFollow(UserEntity userFrom, Long userTo);

}
