package com.campingmall.myproject.security.handler;

import com.campingmall.myproject.security.dto.MemberSecurityDTO;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {
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

        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();
        log.info("=>5. MemberSecurityDTO.getNickName() : "+memberSecurityDTO.getNickname());
        Map<String,Object> claims = memberSecurityDTO.getClaims();

        claims.put("accessToken","");
        claims.put("refreshToken","");

        //JSON 객체 생성
        Gson gson = new Gson();
        String jsonStr = gson.toJson(claims);

        //로그인 성공 후 마지막 보여질 VIEW
        response.sendRedirect("/");
    }
}
