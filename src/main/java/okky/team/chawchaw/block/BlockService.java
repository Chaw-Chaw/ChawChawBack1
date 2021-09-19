package okky.team.chawchaw.block;

import okky.team.chawchaw.block.dto.BlockSessionDto;
import okky.team.chawchaw.block.dto.BlockUserDto;
import okky.team.chawchaw.block.dto.CreateBlockDto;
import okky.team.chawchaw.block.dto.DeleteBlockDto;

import java.util.List;
import java.util.Set;

public interface BlockService {

    Long createBlock(CreateBlockDto createBlockDto);
    void deleteBlock(DeleteBlockDto deleteBlockDto);

    List<BlockUserDto> findAllByUserFromId(Long userFromId);

    Set<Long> findUserId(String email);
    List<BlockSessionDto> findSessionDto(String email);
    List<BlockSessionDto> findSessionDtoWithYou(String email);
    void createSession(String email);
    void updateSession(String email);
    void deleteSession(String email);

}
