package okky.team.chawchaw.block;

import okky.team.chawchaw.block.dto.BlockUserDto;
import okky.team.chawchaw.block.dto.CreateBlockDto;
import okky.team.chawchaw.block.dto.DeleteBlockDto;

import java.util.List;

public interface BlockService {

    Long createBlock(CreateBlockDto createBlockDto);
    void deleteBlock(DeleteBlockDto deleteBlockDto);

    List<BlockUserDto> findBlockUsers(Long userFromId);

}
