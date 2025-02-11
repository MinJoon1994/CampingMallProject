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
    private String defaultAddress;             //회원 주소

    //소셜로그인관련
    private String socialId;            //회원 소셜로그인아이디

    //추가
    private String nickname;    // 닉네임
    private boolean del;        // 회원 탈퇴 여부
    private boolean social;     // 소셜 로그인 사용 여부: false 이면 : 일반 회원들만 사용 대상
    

    //약관동의
    private boolean emailNotifications; //이메일 수신동의
    private boolean smsNotifications;   //sms 수신동의
    private boolean marketingConsent;   //마케팅 수신동의

    @ElementCollection(fetch = FetchType.EAGER) // 소셜 로그인시 Role 값을 세션정보 전환
    @Builder.Default            // 회원 접근 권한 Role
    private List<Role> memberRoleList = new ArrayList<>();

    //회원 접근 권한 부여
    public void addRole(Role memberRole){
        memberRoleList.add(memberRole); //USER, MANAGER, ADMIN
    }

    //회원 접근 권한 모두 회수
    public void clearRole(){memberRoleList.clear();}
    public void changePw(String pw){this.password=pw;}
    public void changeSocial(boolean social){this.social=social;}

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
