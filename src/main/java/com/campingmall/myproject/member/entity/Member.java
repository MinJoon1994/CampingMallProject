package com.campingmall.myproject.member.entity;

import com.campingmall.myproject.member.constant.Role;
import com.campingmall.myproject.member.dto.MemberDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

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
    // MemberAddress와의 OneToMany 관계
    private String defaultAddress;

//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private List<MemberAddress> addresses = new ArrayList<>();
//
//    // 편의 메서드
//    public void addAddress(MemberAddress address) {
//        addresses.add(address);
//        address.setMember(this);
//    }
//
//    public void removeAddress(MemberAddress address) {
//        addresses.remove(address);
//        address.setMember(null);
//    }

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
//                .addresses(memberDTO.getAddresses())
                .email(memberDTO.getEmail())
                .emailNotifications(memberDTO.isEmailNotifications())
                .smsNotifications(memberDTO.isSmsNotifications())
                .marketingConsent(memberDTO.isMarketingConsent())
                .role(Role.USER) //관리자 회원가입은 ADMIN
                .build();

        return  member;
    }
}
