package com.campingmall.myproject.member.repository;

import com.campingmall.myproject.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByLoginId(String loginid);           //로그인 아이디로 회원조회
    Member findByEmail(String email);               //이메일로 회원조회
    Member findByPhoneNumber(String phoneNumber);   //핸드폰번호로 회원조회

}
