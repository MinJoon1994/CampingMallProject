package com.campingmall.myproject.security;

import com.campingmall.myproject.member.entity.Member;
import com.campingmall.myproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("➡ 일반 로그인 요청: " + username);

        // 1️⃣ DB에서 사용자 조회
        Member member = memberRepository.findByLoginId(username);
        if (member == null) {
            log.warn("❌ 사용자 없음: " + username);
            throw new UsernameNotFoundException(username);
        }

        log.info("✅ 사용자 찾음: " + member.getLoginId());

        // 2️⃣ CustomUserPrincipal 생성 후 반환 (권한 포함)
        return new CustomUserPrincipal(
                member.getLoginId(),
                member.getPassword(),
                member.getMemberRoleList() // ✅ 기존 memberRoleList 사용
        );
    }
}
