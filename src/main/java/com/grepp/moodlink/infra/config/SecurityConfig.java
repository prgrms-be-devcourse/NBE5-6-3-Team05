package com.grepp.moodlink.infra.config;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
//@RequiredArgsConstructor
public class SecurityConfig {


    //    @Value("${remember-me.key}")
    private String rememberMeKey;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                HttpServletResponse response, Authentication authentication)
                throws IOException, ServletException {

                boolean isAdmin = authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

                if (isAdmin) {
                    response.sendRedirect("/admin/books");
                    return;
                }

                response.sendRedirect("/");
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // * : 1depth 아래 모든 경로
        // ** : 모든 depth 의 모든 경로
        // Security Config 에는 인증과 관련된 설정만 지정 (PermitAll or Authenticated)
        http
            .csrf().disable() // 추가
            .authorizeHttpRequests(
                (requests) -> requests
                    .requestMatchers(GET, "/member/signup", "/member/signin").permitAll()
                    .requestMatchers(POST, "/member/signin", "/member/signup").permitAll()
                    .requestMatchers("/movies/**").permitAll()
                    .requestMatchers("/worldcup").permitAll()
                    .requestMatchers(GET, "/mypage").authenticated()
                    .anyRequest().permitAll()
            )
            .formLogin((form) -> form
                    .loginPage("/signin")
                    .usernameParameter("userId")
                    .loginProcessingUrl("/signin")
//                                     .defaultSuccessUrl("/")
                    .successHandler(successHandler())
                    .permitAll()
            )
//            .rememberMe(rememberMe -> rememberMe.key(rememberMeKey))
//            .logout(LogoutConfigurer::permitAll);
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/signin")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}