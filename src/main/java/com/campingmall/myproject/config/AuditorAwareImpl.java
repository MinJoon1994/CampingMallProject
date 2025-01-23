package com.campingmall.myproject.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Security;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor(){
        //현재 로그인 한 사용자의 정보를 조회하여 사용자의 이름을 등록자와 수정자로 지정
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId="";

        //인증과정이 정상적으로 처리되었을때 (로그인 상태)
        if(authentication != null)
            userId = authentication.getName();
        return Optional.of(userId);
    }
}
