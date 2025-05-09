package com.grepp.moodlink.app.model.member;

import com.grepp.moodlink.app.model.member.dto.MemberInfoDto;
import com.grepp.moodlink.app.model.member.entity.Member;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
public Optional<MemberInfoDto> GetMemberInfo(String userId){
       Optional<Member> user = memberRepository.findById(userId);


        return user.map(MemberInfoDto::ToDto);

    }




}
