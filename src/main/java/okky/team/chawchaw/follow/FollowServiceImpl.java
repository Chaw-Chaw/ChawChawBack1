package okky.team.chawchaw.follow;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.follow.dto.FollowMessageDto;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowServiceImpl implements FollowService{

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final FollowMessageRepository followMessageRepository;

    @Override
    @Transactional(readOnly = false)
    public FollowMessageDto addFollow(UserEntity userFrom, Long userTo) {

        UserEntity user = userRepository.findById(userTo).orElseThrow(() -> new UsernameNotFoundException("not found user"));

        if (!followRepository.isFollow(userFrom.getId(), userTo)) {
            FollowEntity follow = followRepository.save(new FollowEntity(userFrom, user));
            FollowMessageDto result = new FollowMessageDto(FollowType.FOLLOW, userFrom.getName(), follow.getRegDate());
            followMessageRepository.save(result, userTo);
            return result;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public FollowMessageDto deleteFollow(UserEntity userFrom, Long userTo) {

        UserEntity user = userRepository.findById(userTo).orElseThrow(() -> new UsernameNotFoundException("not found user"));

        if (followRepository.isFollow(userFrom.getId(), userTo)) {
            followRepository.removeByUserFromAndUserTo(userFrom, user);
            FollowMessageDto result = new FollowMessageDto(FollowType.UNFOLLOW, user.getName(), LocalDateTime.now().withNano(0));
            followMessageRepository.save(result, userTo);
            return result;
        }
        return null;
    }

    @Override
    public List<FollowMessageDto> findMessagesByUserId(Long userId) {

        List<FollowMessageDto> result = followMessageRepository.findMessagesByUserId(userId);
        followMessageRepository.deleteMessagesByUserId(userId);
        return result;
    }

    @Override
    public Boolean isFollow(Long userFrom, Long userTo) {

        return followRepository.isFollow(userFrom, userTo);
    }
}
