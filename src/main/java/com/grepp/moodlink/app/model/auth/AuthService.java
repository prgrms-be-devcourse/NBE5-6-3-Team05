package com.grepp.moodlink.app.model.auth;

import com.grepp.moodlink.app.model.auth.domain.Principal;
import com.grepp.moodlink.app.model.member.MemberRepository;
import com.grepp.moodlink.app.model.member.entity.Member;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthService implements UserDetailsService {
    
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username){
        
        Member user = memberRepository.findById(username)
                            .orElseThrow(() -> new UsernameNotFoundException(username));
        
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        // 스프링시큐리티는 기본적으로 권한 앞에 ROLE_ 이 있음을 가정
        // hasRole("ADMIN") =>  ROLE_ADMIN 권한이 있는 지 확인.
        // TEAM_{teamId}:{role}
        // hasAuthority("ADMIN") => ADMIN 권한을 확인

        return Principal.createPrincipal(user, authorities);
    }
    
    
}
