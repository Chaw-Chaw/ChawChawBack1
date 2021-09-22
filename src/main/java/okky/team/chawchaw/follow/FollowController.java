package okky.team.chawchaw.follow;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.block.BlockService;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.follow.dto.FollowMessageDto;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.exception.PointMyselfException;
import okky.team.chawchaw.utils.message.ResponseFollowMessage;
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
public class FollowController {

    private final FollowService followService;
    private final BlockService blockService;
    private final SimpMessageSendingOperations messagingTemplate;

    @PostMapping("follow/{userId}")
    public ResponseEntity createFollow(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                       @PathVariable Long userId) {

        blockService.validBlockUser(principalDetails.getId(), userId);

        if (principalDetails.getId().equals(userId))
            throw new PointMyselfException();

        FollowMessageDto result = followService.addFollow(principalDetails.getUserEntity(), userId);

        if (result != null) {
            messagingTemplate.convertAndSend("/queue/follow/" + userId, result);
            return new ResponseEntity(DefaultResponseVo.res(ResponseFollowMessage.CREATED_SUCCESS, true), HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity(DefaultResponseVo.res(ResponseFollowMessage.EXIST_FOLLOW, false), HttpStatus.OK);
        }
    }

    @DeleteMapping("follow/{userId}")
    public ResponseEntity deleteFollow(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                       @PathVariable Long userId) {

        FollowMessageDto result = followService.deleteFollow(principalDetails.getUserEntity(), userId);

        if (result != null) {
            messagingTemplate.convertAndSend("/queue/follow/" + userId, result);
            return new ResponseEntity(DefaultResponseVo.res(ResponseFollowMessage.DELETE_SUCCESS, true), HttpStatus.OK);
        }
        else {
            return new ResponseEntity(DefaultResponseVo.res(ResponseFollowMessage.NOT_EXIST_FOLLOW, false), HttpStatus.OK);
        }
    }

}
