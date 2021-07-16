package okky.team.chawchaw.follow;

import okky.team.chawchaw.user.UserEntity;

public interface FollowService {

    /**
     * 팔로우
     * @param userFrom
     * @param userTo
     * @return 성공여부(boolean)
     */
    public Boolean addFollow(UserEntity userFrom, Long userTo);

    /**
     * 언팔로우
     * @param userFrom
     * @param userTo
     * @return 성공여부(boolean)
     */
    public Boolean deleteFollow(UserEntity userFrom, Long userTo);

}
