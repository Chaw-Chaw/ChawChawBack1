package okky.team.chawchaw.social;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okky.team.chawchaw.social.Dto.KakaoAccessTokenDto;
import okky.team.chawchaw.social.Dto.SocialDto;
import okky.team.chawchaw.social.service.KakaoService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialService {

    private final KakaoService kakaoService;

    public SocialDto verificationKakao(String code){
        KakaoAccessTokenDto authorization = kakaoService.getAccessTokenByCode(code);
        String userInfo = kakaoService.getUserInfoByAccessToken(authorization.getAccess_token());
        SocialDto socialDto = new SocialDto();

        String s1 = userInfo.replace("{", "").replace("}", "").replace("\"", "");
        String[] s2 = s1.split(",");

        for (String s: s2) {
            String[] split = s.split(":");
            if (split[0].equals("properties") && split[1].equals("nickname"))
                socialDto.setName(split[2]);
            else if (split[0].equals("profile_image"))
                socialDto.setImage_url(split[1] + split[2]);
            else if (split[0].equals("email"))
                socialDto.setEmail("kakao_" + split[1]);
        }
        return socialDto;
    }

}
