package com.campingmall.myproject.security;

import com.campingmall.myproject.exception.Custom403Handler;
import com.campingmall.myproject.member.repository.MemberRepository;
import com.campingmall.myproject.security.handler.CustomSocialLoginSuccessHandler;
import com.campingmall.myproject.security.oauth2.CustomOAuth2UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
import java.io.IOException;

@Log4j2
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {

    private final DataSource dataSource;
    private final CustomUserDetailService userDetailService;
    private final MemberRepository memberRepository;
    private final CustomOAuth2UserService customOAuth2UserService;

    //암호화 시켜주는 객체 생성
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //security 관련 설정
        log.info("=================== Security configure : securityFilterChain ===================");

        // 1. 로그인 과정 생략(제작시 편의를 위해)
        //http.csrf(c->c.disable()); // CSRF 요청 비활성화 RestAPI 에서는 비활성화 안됨
        http.httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable);

        //return http.build(); //로그인 과정 없이 모든 사용자가 리소스(자원) 접근 가능

        // 2. 로그인 설정
        http.formLogin(login->{
            login.loginPage("/member/login")
                    .defaultSuccessUrl("/",true)
                    .usernameParameter("loginId")
                    .passwordParameter("password")
                    .failureUrl("/member/login/error")
                    .successHandler(new AuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                            response.sendRedirect("/");
                        }
                    })
                    .failureHandler(new AuthenticationFailureHandler() {
                        @Override
                        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                            log.info("=>exception:"+exception.getMessage());
                            response.sendRedirect("/member/login/error");
                        }
                    });
        });

        // 2.1 로그인 성공(인증과정)시, 리소스 접근 권한 설정
        http.authorizeHttpRequests(auth->{
            // 2.1.1 정적 리소스 접근 권한 부여
            auth.requestMatchers("/bs/**","/login/**","/images/**","/members/**","/navbar/**","/search/**","/topnotice/**","/main/**").permitAll();

            // 2.1.2 특정 리소스 권한 부여
            auth.requestMatchers("/","/member/**","/admin/**").permitAll();

            // 2.1.3 유저별로 권한 설정
            //auth.requestMatchers("/cart/**","/order/**").hasRole("USER");
            //auth.requestMatchers("/admin/**").hasRole("ADMIN");

            // 2.1.4 설정 리소스 접근 제외한 나머지 인증절차 요구 설정
            auth.anyRequest().authenticated();
        });

        // ✅ 2. 소셜 로그인 설정
        http.oauth2Login(oauth -> oauth
                .loginPage("/member/login") // 일반 로그인과 동일한 페이지 사용
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService) // CustomOAuth2UserService 적용
                )
                .successHandler(authenticationSuccessHandler()) // 소셜 로그인 성공 핸들러
                .failureUrl("/member/login/error")
        );

        // 3. 로그아웃 관련 설정
        // 3.1 기본설정
        http.logout(Customizer.withDefaults());

        //3.2 로그아웃 커스텀설정
        http.logout(logout->{
            logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
                    .deleteCookies("remember-me","JSESSIONID")
                    .invalidateHttpSession(true);
        });

        // 4. 자동 로그인 설정
        http.rememberMe(rememberMe->
                    rememberMe.key("12345678")
                            .tokenRepository(persistentTokenRepository()) //자동 로그인 토큰 설정
                            .userDetailsService(userDetailService) // 인자값은 UserDetailService 객체
                            .tokenValiditySeconds(365*24*60*60)
                            .alwaysRemember(true)
                );
        // .tokenValiditySeconds(30*24*60*60) : 30일 유효 (일*분*시*초)
        // .rememberMeparameter("remember") // login form에서 넘어온 매개변수, 생략시: "remeber-me"
        // .alwaysRemember(true) // remember-me 기능이 화설화 되지 않아도 항상 실행

        return http.build();
    }

    // 4.1 자동 로그인 토큰
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        //repo.setCreateTableOnStartup(true); //최초 테이블 시작시 자동으로 만들기/ 프로덕션과정에선 없애기
        return repo;
    }

    // 정적 자원 경로 접근 제한되어 있는 static 폴더 인식할 수 있게 설정
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        log.info("정적 지원 접근 설정 실행");
        return (web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
    }

    //---------------------------------------------------------------------------------//
    //  5. 접근 권한에 맞지 않은 요청시 403에러 핸들러
    //---------------------------------------------------------------------------------//

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        log.info("------ 접근 거부 커스텀 핸들러");
        return new Custom403Handler();
    }

    //---------------------------------------------------------------------------------//
    // 7.1 소셜 로그인 성공 처리하는 핸들러
    //---------------------------------------------------------------------------------//

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        log.info("소셜 로그인 성공 핸들러 Loading...");
        return new CustomSocialLoginSuccessHandler(memberRepository,passwordEncoder());
    }
}
