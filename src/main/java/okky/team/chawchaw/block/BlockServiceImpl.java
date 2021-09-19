package okky.team.chawchaw.block;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.block.dto.BlockUserDto;
import okky.team.chawchaw.block.dto.CreateBlockDto;
import okky.team.chawchaw.block.dto.DeleteBlockDto;
import okky.team.chawchaw.block.exception.ExistBlockException;
import okky.team.chawchaw.block.exception.NotExistBlockException;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlockServiceImpl implements BlockService {

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;
    private final BlockRedisRepository blockRedisRepository;

    @Override
    @Transactional(readOnly = false)
    public Long createBlock(CreateBlockDto createBlockDto) {
        if (!blockRepository.existsByUserFromIdAndUserToId(createBlockDto.getUserFromId(), createBlockDto.getUserId())) {
            UserEntity userFrom = userRepository.findById(createBlockDto.getUserFromId()).orElseThrow(() -> new UsernameNotFoundException("not found user"));
            UserEntity userTo = userRepository.findById(createBlockDto.getUserId()).orElseThrow(() -> new UsernameNotFoundException("not found user"));
            BlockEntity block = blockRepository.save(new BlockEntity(userFrom, userTo));
            updateSession(userFrom.getEmail());
            return block.getId();
        }
        else {
            throw new ExistBlockException();
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteBlock(DeleteBlockDto deleteBlockDto) {
        if (blockRepository.existsByUserFromIdAndUserToId(deleteBlockDto.getUserFromId(), deleteBlockDto.getUserId())) {
            blockRepository.deleteByUserFromIdAndUserToId(deleteBlockDto.getUserFromId(), deleteBlockDto.getUserId());
            updateSession(deleteBlockDto.getUserFromEmail());
        }
        else {
            throw new NotExistBlockException();
        }
    }

    @Override
    public List<BlockUserDto> findBlockUsers(Long userFromId) {
        return blockRepository.findAllByUserFromId(userFromId);
    }

    @Override
    public void createSession(String email) {
        Set<Long> userIds = new HashSet<>();
        userIds.addAll(blockRepository.findAllByUserFromEmail(email));
        userIds.addAll(blockRepository.findAllByUserToEmail(email));
        blockRedisRepository.save(userIds, email);
    }

    @Override
    public void updateSession(String email) {
        if (blockRedisRepository.isBlock(email)) {
            Set<Long> userIds = new HashSet<>();
            userIds.addAll(blockRepository.findAllByUserFromEmail(email));
            userIds.addAll(blockRepository.findAllByUserToEmail(email));
            blockRedisRepository.update(userIds, email);
        }
    }

    @Override
    public void deleteSession(String email) {
        blockRedisRepository.delete(email);
    }
}
