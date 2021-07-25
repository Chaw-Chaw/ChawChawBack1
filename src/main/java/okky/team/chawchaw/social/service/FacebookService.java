package okky.team.chawchaw.social.service;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.social.dto.SocialDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class FacebookService {

    private final RestTemplate restTemplate;

    /*
     * accessToken 을 이용하여 유저정보 받기
     * */
    public String getUserInfoByAccessToken(String user_id, String accessToken) {

        SocialDto socialDto = new SocialDto();

        String url = "https://graph.facebook.com/v11.0/" + user_id + "?fields=email,name,picture&access_token=" + accessToken;

        return restTemplate.getForObject(url, String.class);
    }
}
