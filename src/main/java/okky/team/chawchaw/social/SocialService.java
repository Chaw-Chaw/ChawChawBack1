package okky.team.chawchaw.social;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.social.Dto.KakaoAccessTokenDto;
import okky.team.chawchaw.social.Dto.SocialDto;
import okky.team.chawchaw.social.service.FacebookService;
import okky.team.chawchaw.social.service.KakaoService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialService {

    private final KakaoService kakaoService;
    private final FacebookService facebookService;
    private final ObjectMapper objectMapper;

    public SocialDto verificationKakao(String code){

        SocialDto socialDto = new SocialDto();
        KakaoAccessTokenDto authorization = kakaoService.getAccessTokenByCode(code);
        String userInfo = kakaoService.getUserInfoByAccessToken(authorization.getAccess_token());

        try {
            JsonNode jsonNode = objectMapper.readTree(userInfo);
            String email = String.valueOf(jsonNode.get("kakao_account").get("email"));
            socialDto.setEmail("kakao_" + email.substring(1, email.length() - 1));
            String name = String.valueOf(jsonNode.get("kakao_account").get("profile").get("nickname"));
            socialDto.setName(name.substring(1, name.length() - 1));
            String imageUrl = String.valueOf(jsonNode.get("kakao_account").get("profile").get("profile_image_url"));
            socialDto.setImageUrl(imageUrl.substring(1, imageUrl.length() - 1));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return socialDto;
    }

    public SocialDto verificationFacebook(String userId, String accessToken){

        SocialDto socialDto = new SocialDto();
        String userInfo = facebookService.getUserInfoByAccessToken(userId, accessToken);
        try {
            JsonNode jsonNode = objectMapper.readTree(userInfo);
            String email = String.valueOf(jsonNode.get("email"));
            socialDto.setEmail("facebook_" + email.substring(1, email.length() - 1));
            String name = String.valueOf(jsonNode.get("name"));
            socialDto.setName(name.substring(1, name.length() - 1));
            String imageUrl = String.valueOf(jsonNode.get("picture").get("data").get("url"));
            socialDto.setImageUrl(imageUrl.substring(1, imageUrl.length() - 1));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return socialDto;
    }

}
