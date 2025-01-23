package com.campingmall.myproject.member.entity;

import com.campingmall.myproject.member.constant.Role;
import com.campingmall.myproject.member.dto.MemberDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter@Setter@ToString
@AllArgsConstructor@NoArgsConstructor
@Builder
@Table(name="member")
public class Member {
    
    //기본사항
    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;                    //회원 ID(PK)
    @Column(unique = true)
    private String loginId;             //회원 로그인아이디
    private String name;                //회원 이름
    private String password;            //회원 비밀번호
    @Column(unique = true)
    private String phoneNumber;         //회원 전화번호
    @Column(unique = true)
    private String email;               //회원 이메일
    @Enumerated(EnumType.STRING)
    private Role role;                  //회원 권한설정(USER 기본)

    //배송관련
    private String defaultAddress;      //회원 기본주소
    private String addresses;           //회원 추가주소

    //소셜로그인관련
    private String socialId;            //회원 소셜로그인아이디

    //약관동의
    private boolean emailNotifications; //이메일 수신동의
    private boolean smsNotifications;   //sms 수신동의
    private boolean marketingConsent;   //마케팅 수신동의

    //1.dto -> Entity
    public static Member createMember(MemberDTO memberDTO,
            PasswordEncoder passwordEncoder
            ){
        Member member = Member.builder()
                .loginId(memberDTO.getLoginId())
                .name(memberDTO.getName())
                .password(passwordEncoder.encode(memberDTO.getPassword()))
                .phoneNumber(memberDTO.getPhoneNumber())
                .defaultAddress(memberDTO.getDefaultAddress())
                .addresses(memberDTO.getAddresses())
                .email(memberDTO.getEmail())
                .emailNotifications(memberDTO.isEmailNotifications())
                .smsNotifications(memberDTO.isSmsNotifications())
                .marketingConsent(memberDTO.isMarketingConsent())
                .role(Role.USER) //관리자 회원가입은 ADMIN
                .build();
        return  member;
    }
}
