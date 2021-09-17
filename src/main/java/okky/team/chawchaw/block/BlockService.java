package okky.team.chawchaw.block;

import okky.team.chawchaw.block.dto.CreateBlockDto;
import okky.team.chawchaw.block.dto.DeleteBlockDto;

public interface BlockService {

    Long createBlock(CreateBlockDto createBlockDto);
    void deleteBlock(DeleteBlockDto deleteBlockDto);

}
