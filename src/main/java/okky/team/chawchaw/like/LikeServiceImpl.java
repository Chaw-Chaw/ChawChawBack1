package okky.team.chawchaw.like;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.like.dto.LikeMessageDto;
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
public class LikeServiceImpl implements LikeService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final LikeMessageRepository likeMessageRepository;

    @Override
    @Transactional(readOnly = false)
    public LikeMessageDto addLike(UserEntity userFrom, Long userTo) {

        UserEntity user = userRepository.findById(userTo).orElseThrow(() -> new UsernameNotFoundException("not found user"));

        if (!likeRepository.isLike(userFrom.getId(), userTo)) {
            LikeEntity follow = likeRepository.save(new LikeEntity(userFrom, user));
            LikeMessageDto result = new LikeMessageDto(LikeType.LIKE, userFrom.getName(), LocalDateTime.now().withNano(0));
            likeMessageRepository.save(result, userTo);
            return result;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public LikeMessageDto deleteLike(UserEntity userFrom, Long userTo) {

        UserEntity user = userRepository.findById(userTo).orElseThrow(() -> new UsernameNotFoundException("not found user"));

        if (likeRepository.isLike(userFrom.getId(), userTo)) {
            likeRepository.removeByUserFromAndUserTo(userFrom, user);
            LikeMessageDto result = new LikeMessageDto(LikeType.UNLIKE, user.getName(), LocalDateTime.now().withNano(0));
            likeMessageRepository.save(result, userTo);
            return result;
        }
        return null;
    }

    @Override
    public List<LikeMessageDto> findMessagesByUserId(Long userId) {

        List<LikeMessageDto> result = likeMessageRepository.findMessagesByUserId(userId);
        likeMessageRepository.deleteMessagesByUserId(userId);
        return result;
    }

    @Override
    public Boolean isLike(Long userFrom, Long userTo) {

        return likeRepository.isLike(userFrom, userTo);
    }
}
