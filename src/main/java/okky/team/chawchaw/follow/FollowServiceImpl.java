package okky.team.chawchaw.follow;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public Long addFollow(UserEntity userFrom, Long userTo) {

        UserEntity user = userRepository.findById(userTo).orElseThrow(() -> new UsernameNotFoundException("not found user"));

        FollowEntity result = followRepository.save(new FollowEntity(userFrom, user));

        return result.getId();
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteFollow(UserEntity userFrom, Long userTo) {

        UserEntity user = userRepository.findById(userTo).orElseThrow(() -> new UsernameNotFoundException("not found user"));

        followRepository.removeByUserFromAndUserTo(userFrom, user);

    }
}
