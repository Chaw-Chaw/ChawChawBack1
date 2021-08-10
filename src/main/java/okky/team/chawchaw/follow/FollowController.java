package okky.team.chawchaw.follow;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.message.ResponseFollowMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("follow/{userId}")
    public ResponseEntity createFollow(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                @PathVariable Long userId) {

        followService.addFollow(principalDetails.getUserEntity(), userId);

        return new ResponseEntity(DefaultResponseVo.res(ResponseFollowMessage.CREATED_SUCCESS, true), HttpStatus.CREATED);
    }

    @DeleteMapping("follow/{userId}")
    public ResponseEntity<Boolean> deleteFollow(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                @PathVariable Long userId) {

        followService.deleteFollow(principalDetails.getUserEntity(), userId);

        return new ResponseEntity(DefaultResponseVo.res(ResponseFollowMessage.DELETE_SUCCESS, true), HttpStatus.OK);

    }

}
