package com.campingmall.myproject.security.dto;

import com.campingmall.myproject.member.constant.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class MemberSecurityDTO extends User implements OAuth2User {

    private String name;
    private String email;
    private String password;
    private String address;

    private String nickname;
    private boolean del;

    private boolean social;
    private Map<String,Object> props;

    private Role role;
    private List<String> roleNames = new ArrayList<>();

    public MemberSecurityDTO(String name, String address, String nickname,
                             String username, String password, List<String> roleNames,
                             boolean del, boolean social){
        //User 객체가 Spring Security 에서 사용하는 세션정보 객체

        super(//User 부모 생성자를 통해서 username(email), password, 권한설정값을 초기화
                username,
                password,
                roleNames.stream()
                        .map(str->
                                // USER,MANAGER,ADMIN 문자열 -> "ROLE_" 접두로 붙이기("ROLE_USER","ROLE_MANAGER","ROLE_ADMIN")
                                // 권한객체(SimpleGrantedAuthority)타입으로 전환
                                new SimpleGrantedAuthority("ROLE_"+str)
                        )
                        .collect(Collectors.toList())
        );//end super()=> 부모 생성자

        //필드 추가
        this.name=name;
        this.email=username;
        this.roleNames=roleNames;

        this.password=password;
        this.address=address;
        this.nickname=nickname;

    }

    public Map<String,Object> getClaims(){
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("email",email);
        dataMap.put("password",password);
        dataMap.put("nickname",nickname);
        dataMap.put("social",social);
        dataMap.put("rolesNames",roleNames);

        return dataMap;
    }

    @Override
    public String getUsername(){return this.email;}

    @Override
    public Map<String, Object> getAttributes() {
        return this.getProps(); // 소셜 로그인 정보 MAP 구조로 반환
    }
}
