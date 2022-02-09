package com.example.spring.security.service.oauth;

import com.example.spring.security.config.auth.PrincipalDetails;
import com.example.spring.security.config.oauth.provider.FaceBookUserInfo;
import com.example.spring.security.config.oauth.provider.GoogleUserInfo;
import com.example.spring.security.config.oauth.provider.NaverUserInfo;
import com.example.spring.security.config.oauth.provider.OAuth2UserInfo;
import com.example.spring.security.domain.User;
import com.example.spring.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        return super.loadUser(userRequest);
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("UserRequest ClientRegistration: {}", userRequest.getClientRegistration());
        log.info("OAuth2 User: {}", oAuth2User);

        return processOAuth2User(userRequest, oAuth2User);
    }

    @SuppressWarnings(value = {"unchecked"})
    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        // Attribute를 파싱해서 공통 객체로 묶는다. 관리가 편함.
        OAuth2UserInfo oAuth2UserInfo;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            log.info("OAuth-Google: {}", userRequest.getClientRegistration());
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            log.info("OAuth-Facebook: {}", userRequest.getClientRegistration());
            oAuth2UserInfo = new FaceBookUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            log.info("OAuth-Naver: {}", userRequest.getClientRegistration());
            oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_IMPLEMENTED);
        }

        log.info("OAuth2UserInfo.getProvider(): {}", oAuth2UserInfo.getProvider());
        log.info("OAuth2UserInfo.getProviderId(): {}", oAuth2UserInfo.getProviderId());

        Optional<User> userOptional = userRepository.findByProviderAndProviderId(
                oAuth2UserInfo.getProvider(),
                oAuth2UserInfo.getProviderId()
        );

        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            // user가 존재하면 update 해주기
            user.setEmail(oAuth2UserInfo.getEmail());
            userRepository.save(user);
        } else {
            // user의 패스워드가 null이기 때문에 OAuth 유저는 일반적인 로그인을 할 수 없음.
            user = User.builder()
                    .username(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
                    .email(oAuth2UserInfo.getEmail())
                    .role("ROLE_USER")
                    .provider(oAuth2UserInfo.getProvider())
                    .providerId(oAuth2UserInfo.getProviderId())
                    .build();

            userRepository.save(user);
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }


}
