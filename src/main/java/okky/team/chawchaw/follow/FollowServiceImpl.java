package okky.team.chawchaw.follow;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowServiceImpl implements FollowService{

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Override
    @Transactional(readOnly = false)
    public Boolean addFollow(UserEntity userFrom, Long userTo) {

        UserEntity user = userRepository.findById(userTo).orElseGet(null);

        if (user != null) {
            followRepository.save(new FollowEntity(userFrom, user));
            return true;
        }else
            return false;

    }

    @Override
    @Transactional(readOnly = false)
    public Boolean deleteFollow(UserEntity userFrom, Long userTo) {

        UserEntity user = userRepository.findById(userTo).orElseGet(null);

        if (user != null) {
            followRepository.removeByUserFromAndUserTo(userFrom, user);
            return true;
        }else
            return false;

    }
}
