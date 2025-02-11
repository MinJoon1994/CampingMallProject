package com.campingmall.myproject.member.repository;

import com.campingmall.myproject.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByLoginId(String loginid);           //로그인 아이디로 회원조회
    Member findByEmail(String email);               //이메일로 회원조회
    Member findByPhoneNumber(String phoneNumber);   //핸드폰번호로 회원조회



    //지연 로딩 설정시 : 쿼리 실행시 proxy 객체 생성하여 proxy 객체 호출시에만 쿼리문 수행
    @EntityGraph(attributePaths = {"memberRoleList"})
    @Query("select m from Member m where m.email = :email and m.social = false")
    Member getWithRoles(@Param("email") String email);
}
