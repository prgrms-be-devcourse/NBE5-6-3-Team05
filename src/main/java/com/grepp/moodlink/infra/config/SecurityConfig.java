package com.grepp.moodlink.infra.config;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import com.grepp.moodlink.infra.auth.token.JwtAuthenticationEntryPoint;
import com.grepp.moodlink.infra.auth.token.filter.AuthExceptionFilter;
import com.grepp.moodlink.infra.auth.token.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthExceptionFilter authExceptionFilter;
    private final JwtAuthenticationEntryPoint entryPoint;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .build();
    }

//    @Bean
//    public AuthenticationSuccessHandler successHandler() {
//        return new AuthenticationSuccessHandler() {
//            @Override
//            public void onAuthenticationSuccess(HttpServletRequest request,
//                HttpServletResponse response, Authentication authentication)
//                throws IOException, ServletException {
//
//                boolean isAdmin = authentication.getAuthorities()
//                    .stream()
//                    .anyMatch(authority ->
//                        authority.getAuthority().equals("ROLE_ADMIN"));
//
//                if (isAdmin) {
//                    response.sendRedirect("/admin/movies");
//                    return;
//                }
//
//                response.sendRedirect("/");
//            }
//        };
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

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
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(authExceptionFilter, JwtAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}