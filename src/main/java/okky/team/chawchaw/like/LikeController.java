package okky.team.chawchaw.like;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.block.BlockService;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.like.dto.CreateLikeDto;
import okky.team.chawchaw.like.dto.DeleteLikeDto;
import okky.team.chawchaw.like.dto.LikeMessageDto;
import okky.team.chawchaw.user.UserService;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.message.ResponseGlobalMessage;
import okky.team.chawchaw.utils.message.ResponseLikeMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("like")
public class LikeController {

    private final LikeService likeService;
    private final BlockService blockService;
    private final UserService userService;
    private final SimpMessageSendingOperations messagingTemplate;

    @PostMapping("")
    public ResponseEntity<?> createLike(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                     @Valid @RequestBody CreateLikeDto createLikeDto) {

        userService.validMyself(principalDetails.getId(), createLikeDto.getUserId());
        blockService.validBlockUser(principalDetails.getId(), createLikeDto.getUserId());

        createLikeDto.setUserFromId(principalDetails.getId());
        createLikeDto.setUserFromName(principalDetails.getName());

        LikeMessageDto result = likeService.addLike(createLikeDto);

        if (result != null) {
            messagingTemplate.convertAndSend("/queue/like/" + createLikeDto.getUserId(), result);
            return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G200), HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>(DefaultResponseVo.res(ResponseLikeMessage.L400), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteLike(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                     @Valid @RequestBody DeleteLikeDto deleteLikeDto) {

        deleteLikeDto.setUserFromId(principalDetails.getId());
        deleteLikeDto.setUserFromName(principalDetails.getName());

        userService.validMyself(deleteLikeDto.getUserFromId(), deleteLikeDto.getUserId());

        LikeMessageDto result = likeService.deleteLike(deleteLikeDto);

        if (result != null) {
            messagingTemplate.convertAndSend("/queue/like/" + deleteLikeDto.getUserId(), result);
            return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G200), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(DefaultResponseVo.res(ResponseLikeMessage.L401), HttpStatus.BAD_REQUEST);
        }
    }

}
