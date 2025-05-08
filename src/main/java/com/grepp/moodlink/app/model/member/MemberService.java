package com.grepp.moodlink.app.model.member;

import com.grepp.moodlink.app.model.member.dto.MemberDto;
import com.grepp.moodlink.app.model.member.dto.MemberInfoDto;
import com.grepp.moodlink.app.model.member.entity.User;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import javax.swing.text.html.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
public Optional<MemberInfoDto> GetMemberInfo(String userId){
       Optional<User> user = memberRepository.findById(userId);


        return user.map(MemberInfoDto::ToDto);

    }




}
