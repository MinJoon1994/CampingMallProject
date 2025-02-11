package com.campingmall.myproject.security.handler;

import com.campingmall.myproject.member.constant.Role;
import com.campingmall.myproject.member.entity.Member;
import com.campingmall.myproject.member.repository.MemberRepository;
import com.campingmall.myproject.security.dto.MemberSecurityDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

// 로그인 성공한 후 AuthenticationSuccessHandler 통해서 후처리 작업
@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("------------------ onAuthenticationSuccess ------------------");
        log.info(" =>1. authentication   : "+authentication.toString());
        log.info("-------------------------------------------------------------");
        log.info(" =>2. getName()        : "+authentication.getName());
        log.info(" =>3. getAuthorities() : "+authentication.getAuthorities());
        log.info(" =>4. getPrincipal()   : "+authentication.getPrincipal());
        log.info("-------------------------------------------------------------");

        Object principal = authentication.getPrincipal();
        OAuth2User oAuth2User = (OAuth2User) principal;
        Map<String, Object> paramMap = oAuth2User.getAttributes();
        String email = (String) paramMap.get("email");

        MemberSecurityDTO memberSecurityDTO = generateDTO(email,paramMap);
        log.info(" =>5. MemberSecurityDTO.getNickname() : "+memberSecurityDTO.getNickname());
        Map<String ,Object> claims = memberSecurityDTO.getClaims();

        String encodingPw = memberSecurityDTO.getEmail();

        //소셜 로그인시 패스워드가 1111이면 다시 만들어라고하는 로직
        if(memberSecurityDTO.isSocial() && (memberSecurityDTO.getPassword().equals("1111")) ||
        passwordEncoder.matches("1111", memberSecurityDTO.getPassword())){
            log.info("shold Change password");
            log.info("redirect to Member Modify");

            response.sendRedirect("/member/modify");
        }else{
            response.sendRedirect("/");
        }

    }

    //소셜 로그인으로 받은 정보를 Member Entity 에 반영하여 DOM 에 저장하기
    private MemberSecurityDTO generateDTO(String email, Map<String,Object> params){

        //String email1 = (String) params.get("email");
        String name = (String) params.get("name");

        Member result = memberRepository.findByEmail(email);

        //데이터베이스에 이메일을 이용해 검색해서 사용자가 없다면
        if(result == null) {
            //회원 추가
            Member member = Member.builder()
                    .email(email)
                    .loginId(email)
                    // 비밀번호는 테스트용으로 1111로 통일
                    //.password(passwordEncoder.encode("1111"))
                    .password(passwordEncoder.encode("1234"))
                    .role(Role.USER)
                    .name(name)
                    .social(true) // 일반 회원 정보가 아닌 소설 로그인으로 받은 정보임을 설정
                    .build();

            member.addRole(Role.USER);
            memberRepository.save(member);

            // MemberSecurityDTO 구성 및 변환
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    name,
                    "부산",
                    "민똘",
                    email,
                    "1111",
                    Arrays.asList(("ROLE_USER")), //LIST 구조 String 타입
                    false,
                    member.isSocial()
            );

            return memberSecurityDTO;

        }else{

            MemberSecurityDTO memberSecurityDTO =
                    new MemberSecurityDTO(
                            result.getName(),
                            result.getDefaultAddress(),
                            result.getNickname(),
                            result.getEmail(),
                            result.getPassword(),
                            result.getMemberRoleList()
                                    .stream()
                                    .map(memberRole->("ROLE_"+memberRole.name()))
                                    .collect(Collectors.toList()),
                            result.isDel(),
                            result.isSocial()
                    );
            return memberSecurityDTO;
        }// else end

    }
}
