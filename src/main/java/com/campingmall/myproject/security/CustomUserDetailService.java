package com.campingmall.myproject.security;

import com.campingmall.myproject.member.entity.Member;
import com.campingmall.myproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomUserDetailService implements UserDetailsService{
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("=>LoadUserByUserName:"+username);

        Member member = memberRepository.findByLoginId(username);
        log.info("멤버레포지토리 제대로 들어왔는지 확인");
        log.info(member);
        if(member==null) throw new UsernameNotFoundException(username);

        UserDetails userDetails = User.builder()
                .username(member.getLoginId())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();

        log.info("=>로그인 유저 디테일: "+userDetails.toString());
        return userDetails;
    }
}
