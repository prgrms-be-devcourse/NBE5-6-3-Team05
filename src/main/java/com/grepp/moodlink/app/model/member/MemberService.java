package com.grepp.moodlink.app.model.member;

import com.grepp.moodlink.app.model.member.dto.MemberInfoDto;
import com.grepp.moodlink.app.model.member.dto.ModifyDto;
import com.grepp.moodlink.app.model.member.entity.Member;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Optional<MemberInfoDto> GetMemberInfo(String userId) {
        Optional<Member> user = memberRepository.findById(userId);
        return user.map(MemberInfoDto::ToDto);

    }

    public String GetUsername(String userId) {
        return memberRepository.findByUsernameById(userId);
    }


    public void modifyProfile(String userId, ModifyDto dto) {
        Member member = memberRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            String encodedPassword = passwordEncoder.encode(dto.getPassword());
            member.setPassword(encodedPassword);
        }
        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            member.setUsername(dto.getUsername());
        }

        memberRepository.save(member);
    }
}
