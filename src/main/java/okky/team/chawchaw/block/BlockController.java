package okky.team.chawchaw.block;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.block.dto.CreateBlockDto;
import okky.team.chawchaw.block.dto.DeleteBlockDto;
import okky.team.chawchaw.block.exception.ExistBlockException;
import okky.team.chawchaw.block.exception.NotExistBlockException;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.message.ResponseBlockMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;

    @PostMapping("/users/block")
    public ResponseEntity createBlock(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                      @Valid @RequestBody CreateBlockDto createBlockDto) {

        createBlockDto.setUserFrom(principalDetails.getId());

        try {
            blockService.createBlock(createBlockDto);
        } catch (ExistBlockException e) {
            return new ResponseEntity(DefaultResponseVo.res(ResponseBlockMessage.EXIST_BLOCK, false), HttpStatus.OK);
        }
        return new ResponseEntity(DefaultResponseVo.res(ResponseBlockMessage.CREATE_BLOCK_SUCCESS, true), HttpStatus.OK);
    }

    @DeleteMapping("/users/block")
    public ResponseEntity deleteBlock(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                      @Valid @RequestBody DeleteBlockDto deleteBlockDto) {

        deleteBlockDto.setUserFrom(principalDetails.getId());

        try {
            blockService.deleteBlock(deleteBlockDto);
        } catch (NotExistBlockException e) {
            return new ResponseEntity(DefaultResponseVo.res(ResponseBlockMessage.NOT_EXIST_BLOCK, false), HttpStatus.OK);
        }
        return new ResponseEntity(DefaultResponseVo.res(ResponseBlockMessage.DELETE_SUCCESS, true), HttpStatus.OK);
    }

}
