package com.example.spring.security.controller;

import com.example.spring.security.config.auth.PrincipalDetails;
import com.example.spring.security.domain.User;
import com.example.spring.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Iterator;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IndexController {
    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @GetMapping({ "", "/" })
    public @ResponseBody
    String index() {
        return "인덱스 페이지입니다.";
    }

    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principal) {
        System.out.println("Principal : " + principal);
        System.out.println("OAuth2 : "+principal.getUser().getProvider());
        
        for (GrantedAuthority auth : principal.getAuthorities()) {
            System.out.println(auth.getAuthority());
        }

        return "유저 페이지입니다.";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "어드민 페이지입니다.";
    }

    //@PostAuthorize("hasRole('ROLE_MANAGER')")
    //@PreAuthorize("hasRole('ROLE_MANAGER')")
    @Secured("ROLE_MANAGER")
    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "매니저 페이지입니다.";
    }

    @GetMapping("/loginForm")
    public String login() {
          return "loginForm";
    }

    @GetMapping("/joinForm")
    public String join() {
        return "joinForm";
    }

    @PostMapping("/joinProc")
    public String joinProc(User user) {
        System.out.println("회원가입 진행 : " + user);
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        user.setRole("ROLE_USER");
        user.setName(user.getEmail());

        log.info("User: {}", user);
        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @GetMapping("/test/login")
    @ResponseBody
    public String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        log.info("{}", principalDetails.getUsername());
        log.info("{}", principalDetails.getUsername());

        return "Session";
    }

    @GetMapping("/test/oauth/login")
    @ResponseBody
    public String testOAuth2Login(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("{}", oAuth2User.getName());
        log.info("{}", oAuth2User.getAttributes());

        log.info("{}", principalDetails.getName());
        log.info("{}", principalDetails.getAttributes());

        return "OAuth Session";
    }
}
