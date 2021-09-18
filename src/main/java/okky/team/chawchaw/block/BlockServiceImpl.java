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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlockServiceImpl implements BlockService {

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = false)
    public Long createBlock(CreateBlockDto createBlockDto) {
        if (!blockRepository.existsByUserFromIdAndUserToId(createBlockDto.getUserFrom(), createBlockDto.getUserId())) {
            UserEntity userTo = userRepository.findById(createBlockDto.getUserId()).orElseThrow(() -> new UsernameNotFoundException("not found user"));
            UserEntity userFrom = userRepository.findById(createBlockDto.getUserFrom()).orElseThrow(() -> new UsernameNotFoundException("not found user"));
            BlockEntity block = blockRepository.save(new BlockEntity(userFrom, userTo));
            return block.getId();
        }
        else {
            throw new ExistBlockException();
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteBlock(DeleteBlockDto deleteBlockDto) {
        if (blockRepository.existsByUserFromIdAndUserToId(deleteBlockDto.getUserFrom(), deleteBlockDto.getUserId())) {
            blockRepository.deleteByUserFromIdAndUserToId(deleteBlockDto.getUserFrom(), deleteBlockDto.getUserId());
        }
        else {
            throw new NotExistBlockException();
        }
    }

    @Override
    public List<BlockUserDto> findBlockUsers(Long userFromId) {
        return blockRepository.findAllByUserFromId(userFromId);
    }
}
