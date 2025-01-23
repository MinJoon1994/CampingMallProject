package com.campingmall.myproject.member.service;

import com.campingmall.myproject.member.dto.MemberDTO;
import com.campingmall.myproject.member.entity.Member;
import com.campingmall.myproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member saveMember(MemberDTO memberDTO){
        Member member = Member.createMember(memberDTO,passwordEncoder);

        //아이디로 중복성 검사
        validateDuplicateLoginId(member);
        //이메일로 중복성 검사
        validateDuplicateEmail(member);
        //전화번호로 중복성 검사
        validateDuplicatePhoneNumber(member);
        
        return memberRepository.save(member);
    }
    
    private void validateDuplicateLoginId(Member member){
        Member findLoginId = memberRepository.findByLoginId(member.getLoginId());
        if(findLoginId!=null) throw new IllegalStateException("이미 중복된 아이디입니다.");
    }

    private void validateDuplicateEmail(Member member){
        Member findEmail = memberRepository.findByEmail(member.getEmail());
        if(findEmail!=null) throw new IllegalStateException("이미 등록된 이메일 입니다.");
    }

    private void validateDuplicatePhoneNumber(Member member){
        Member findPhoneNumber = memberRepository.findByPhoneNumber(member.getPhoneNumber());
        if(findPhoneNumber!=null) throw new IllegalStateException("이미 등록된 전화번호 입니다.");
    }
}
