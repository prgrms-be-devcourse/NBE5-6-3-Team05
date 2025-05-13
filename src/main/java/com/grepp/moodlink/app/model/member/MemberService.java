package com.grepp.moodlink.app.model.member;

import com.grepp.moodlink.app.model.auth.code.Role;
import com.grepp.moodlink.app.model.member.dto.MemberDto;
import com.grepp.moodlink.app.model.member.entity.Member;
import com.grepp.moodlink.infra.error.exceptions.CommonException;
import com.grepp.moodlink.infra.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final ModelMapper mapper;

    @Transactional
    public void signup(MemberDto dto) {
        if (memberRepository.existsById(dto.getUserId())) {
            throw new CommonException(ResponseCode.BAD_REQUEST);
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        Member member = mapper.map(dto, Member.class);
        member.setPassword(encodedPassword);
        member.setRole(Role.ROLE_USER);
        memberRepository.save(member);
    }

    public Boolean isDuplicatedId(String id) {
        return memberRepository.existsById(id);
    }

    public MemberDto findById(String userId) {
        Member user = memberRepository.findById(userId)
            .orElseThrow(() -> new CommonException(ResponseCode.BAD_REQUEST));
        return mapper.map(user, MemberDto.class);
    }
}
