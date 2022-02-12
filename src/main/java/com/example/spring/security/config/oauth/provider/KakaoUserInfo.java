package com.example.spring.security.config.oauth.provider;

import lombok.RequiredArgsConstructor;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {
    private final String id;
    private final Map<String, Object> attributes;

    @SuppressWarnings("unchecked")
    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>) attributes.get("kakao_account");
        this.id = String.valueOf(attributes.get("id"));
    }

    @Override
    public String getProviderId() {
        return id;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("email");
    }
}
