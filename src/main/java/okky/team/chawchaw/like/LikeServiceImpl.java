package okky.team.chawchaw.like;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.like.dto.CreateLikeDto;
import okky.team.chawchaw.like.dto.DeleteLikeDto;
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
    public LikeMessageDto addLike(CreateLikeDto createLikeDto) {

        UserEntity userFrom = userRepository.findById(createLikeDto.getUserFromId()).orElseThrow(() -> new UsernameNotFoundException("not found user"));
        UserEntity userTo = userRepository.findById(createLikeDto.getUserId()).orElseThrow(() -> new UsernameNotFoundException("not found user"));

        if (!likeRepository.isLike(userFrom.getId(), userTo.getId())) {
            likeRepository.save(new LikeEntity(userFrom, userTo));
            LikeMessageDto result = new LikeMessageDto(LikeType.LIKE, userFrom.getName(), LocalDateTime.now().withNano(0));
            likeMessageRepository.save(result, userTo.getId());
            return result;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public LikeMessageDto deleteLike(DeleteLikeDto deleteLikeDto) {

        UserEntity userFrom = userRepository.findById(deleteLikeDto.getUserFromId()).orElseThrow(() -> new UsernameNotFoundException("not found user"));
        UserEntity userTo = userRepository.findById(deleteLikeDto.getUserId()).orElseThrow(() -> new UsernameNotFoundException("not found user"));

        if (likeRepository.isLike(userFrom.getId(), userTo.getId())) {
            likeRepository.removeByUserFromAndUserTo(userFrom, userTo);
            LikeMessageDto result = new LikeMessageDto(LikeType.UNLIKE, userFrom.getName(), LocalDateTime.now().withNano(0));
            likeMessageRepository.save(result, userTo.getId());
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
