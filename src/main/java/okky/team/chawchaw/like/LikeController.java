package okky.team.chawchaw.like;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.block.BlockService;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.like.dto.LikeMessageDto;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.exception.PointMyselfException;
import okky.team.chawchaw.utils.message.ResponseLikeMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final BlockService blockService;
    private final SimpMessageSendingOperations messagingTemplate;

    @PostMapping("like/{userId}")
    public ResponseEntity createLike(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                     @PathVariable Long userId) {

        blockService.validBlockUser(principalDetails.getId(), userId);

        if (principalDetails.getId().equals(userId))
            throw new PointMyselfException();

        LikeMessageDto result = likeService.addLike(principalDetails.getUserEntity(), userId);

        if (result != null) {
            messagingTemplate.convertAndSend("/queue/like/" + userId, result);
            return new ResponseEntity(DefaultResponseVo.res(ResponseLikeMessage.CREATED_SUCCESS, true), HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity(DefaultResponseVo.res(ResponseLikeMessage.EXIST_LIKE, false), HttpStatus.OK);
        }
    }

    @DeleteMapping("like/{userId}")
    public ResponseEntity deleteLike(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                     @PathVariable Long userId) {

        LikeMessageDto result = likeService.deleteLike(principalDetails.getUserEntity(), userId);

        if (result != null) {
            messagingTemplate.convertAndSend("/queue/like/" + userId, result);
            return new ResponseEntity(DefaultResponseVo.res(ResponseLikeMessage.DELETE_SUCCESS, true), HttpStatus.OK);
        }
        else {
            return new ResponseEntity(DefaultResponseVo.res(ResponseLikeMessage.NOT_EXIST_LIKE, false), HttpStatus.OK);
        }
    }

}
