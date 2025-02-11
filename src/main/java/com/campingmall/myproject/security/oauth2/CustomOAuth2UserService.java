package com.campingmall.myproject.security.oauth2;

import com.campingmall.myproject.member.constant.Role;
import com.campingmall.myproject.member.entity.Member;
import com.campingmall.myproject.member.repository.MemberRepository;
import com.campingmall.myproject.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("➡ 소셜 로그인 요청: " + userRequest.getClientRegistration().getRegistrationId());

        OAuth2User oauth2User = super.loadUser(userRequest);
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        Member member = memberRepository.findByEmail(email); // ✅ Optional 제거
        if (member == null) {
            log.info("✅ 새로운 소셜 로그인 사용자! 자동 회원가입 진행...");

            member = Member.builder()
                    .email(email)
                    .loginId(email) // 소셜 로그인은 email을 loginId로 사용
                    .name(name)
                    .social(true)
                    .role(Role.USER) // ✅ 기본 ROLE_USER 부여
                    .build();

            member.addRole(Role.USER); // ✅ memberRoleList에 ROLE_USER 추가
            memberRepository.save(member);
        }

        // ✅ CustomUserPrincipal 반환 (ROLE_USER 자동 포함)
        return new CustomUserPrincipal(
                member.getLoginId(),
                oauth2User.getAttributes() // ✅ 소셜 로그인은 ROLE_USER 자동 부여됨
        );
    }
}
