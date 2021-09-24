package okky.team.chawchaw.like;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.like.dto.CreateLikeDto;
import okky.team.chawchaw.like.dto.DeleteLikeDto;
import okky.team.chawchaw.like.dto.LikeMessageDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final LikeMessageRepository likeMessageRepository;

    @Override
    @Transactional(readOnly = false)
    public LikeMessageDto addLike(CreateLikeDto createLikeDto) {

        if (!likeRepository.isLike(createLikeDto.getUserFromId(), createLikeDto.getUserId())) {
            likeRepository.saveByUserFromIdAndUserToId(createLikeDto.getUserFromId(), createLikeDto.getUserId());
            LikeMessageDto result = new LikeMessageDto(LikeType.LIKE, createLikeDto.getUserFromName(), LocalDateTime.now().withNano(0));
            likeMessageRepository.save(result, createLikeDto.getUserId());
            return result;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public LikeMessageDto deleteLike(DeleteLikeDto deleteLikeDto) {

        if (likeRepository.isLike(deleteLikeDto.getUserFromId(), deleteLikeDto.getUserId())) {
            likeRepository.removeByUserFromIdAndUserToId(deleteLikeDto.getUserFromId(), deleteLikeDto.getUserId());
            LikeMessageDto result = new LikeMessageDto(LikeType.UNLIKE, deleteLikeDto.getUserFromName(), LocalDateTime.now().withNano(0));
            likeMessageRepository.save(result, deleteLikeDto.getUserId());
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
