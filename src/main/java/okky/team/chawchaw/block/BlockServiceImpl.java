package okky.team.chawchaw.block;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.block.dto.BlockSessionDto;
import okky.team.chawchaw.block.dto.BlockUserDto;
import okky.team.chawchaw.block.dto.CreateBlockDto;
import okky.team.chawchaw.block.dto.DeleteBlockDto;
import okky.team.chawchaw.block.exception.BlockedUserException;
import okky.team.chawchaw.block.exception.ExistBlockException;
import okky.team.chawchaw.block.exception.NotExistBlockException;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public List<BlockUserDto> findAllByUserFromId(Long userFromId) {
        return blockRepository.findAllByUserFromId(userFromId);
    }

    @Override
    public Set<Long> findUserId(String email) {
        return blockRedisRepository.findAllByEmail(email).stream().map(x -> x.getUserId()).collect(Collectors.toSet());
    }

    @Override
    public List<BlockSessionDto> findSessionDto(String email) {
        return blockRepository.findSessionDtoByUserFromEmail(email);
    }

    @Override
    public List<BlockSessionDto> findSessionDtoWithYou(String email) {
        List<BlockSessionDto> blockUsers = new ArrayList<>();

        blockRepository.findSessionDtoByUserFromEmail(email).stream().filter(x -> !blockUsers.contains(x.getUserId())).forEach(blockUsers::add);
        blockRepository.findSessionDtoByUserToEmail(email).stream().filter(x -> !blockUsers.contains(x.getUserId())).forEach(blockUsers::add);

        return blockUsers;
    }

    @Override
    public void createSession(String email) {

        List<BlockSessionDto> blockUsers = findSessionDtoWithYou(email);

        blockRedisRepository.save(blockUsers, email);
    }

    @Override
    public void updateSession(String email) {
        if (blockRedisRepository.isBlock(email)) {

            List<BlockSessionDto> blockUsers = findSessionDtoWithYou(email);

            blockRedisRepository.update(blockUsers, email);
        }
    }

    @Override
    public void deleteSession(String email) {
        blockRedisRepository.delete(email);
    }

    @Override
    public void validBlockUser(Long userFromId, Long userToId) {
        if (blockRepository.existsByUserIds(userFromId, userToId))
            throw new BlockedUserException();
    }
}
