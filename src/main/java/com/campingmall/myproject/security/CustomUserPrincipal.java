package com.campingmall.myproject.security;

import com.campingmall.myproject.member.constant.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class CustomUserPrincipal implements UserDetails, OAuth2User {

    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes; // 소셜 로그인 사용자 정보 저장

    // ✅ 일반 로그인 생성자 (리스트 유지)
    public CustomUserPrincipal(String username, String password, List<Role> roles) {
        this.username = username;
        this.password = password;
        this.authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())) // Enum -> ROLE_USER 변환
                .toList();
        this.attributes = null; // 일반 로그인은 소셜 속성 필요 없음
    }

    // ✅ 소셜 로그인 생성자 (ROLE_USER 고정)
    public CustomUserPrincipal(String username, Map<String, Object> attributes) {
        this.username = username;
        this.password = null; // 소셜 로그인은 비밀번호 필요 없음
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_USER")); // 소셜 로그인은 항상 ROLE_USER
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
