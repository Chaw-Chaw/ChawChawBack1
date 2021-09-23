package okky.team.chawchaw.like;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.block.BlockService;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.like.dto.CreateLikeDto;
import okky.team.chawchaw.like.dto.DeleteLikeDto;
import okky.team.chawchaw.like.dto.LikeMessageDto;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.exception.PointMyselfException;
import okky.team.chawchaw.utils.message.ResponseLikeMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("like")
public class LikeController {

    private final LikeService likeService;
    private final BlockService blockService;
    private final SimpMessageSendingOperations messagingTemplate;

    @PostMapping("")
    public ResponseEntity createLike(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                     @RequestBody CreateLikeDto createLikeDto) {

        createLikeDto.setUserFromId(principalDetails.getId());

        blockService.validBlockUser(principalDetails.getId(), createLikeDto.getUserId());

        if (principalDetails.getId().equals(createLikeDto.getUserId()))
            throw new PointMyselfException();

        LikeMessageDto result = likeService.addLike(createLikeDto);

        if (result != null) {
            messagingTemplate.convertAndSend("/queue/like/" + createLikeDto.getUserId(), result);
            return new ResponseEntity(DefaultResponseVo.res(ResponseLikeMessage.CREATED_SUCCESS, true), HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity(DefaultResponseVo.res(ResponseLikeMessage.EXIST_LIKE, false), HttpStatus.OK);
        }
    }

    @DeleteMapping("{userId}")
    public ResponseEntity deleteLike(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                     @PathVariable Long userId) {


        LikeMessageDto result = likeService.deleteLike(new DeleteLikeDto(principalDetails.getId(), userId));

        if (result != null) {
            messagingTemplate.convertAndSend("/queue/like/" + userId, result);
            return new ResponseEntity(DefaultResponseVo.res(ResponseLikeMessage.DELETE_SUCCESS, true), HttpStatus.OK);
        }
        else {
            return new ResponseEntity(DefaultResponseVo.res(ResponseLikeMessage.NOT_EXIST_LIKE, false), HttpStatus.OK);
        }
    }

}
