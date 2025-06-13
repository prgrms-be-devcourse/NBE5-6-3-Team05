package com.grepp.moodlink.app.model.member;

import com.grepp.moodlink.app.model.auth.code.Role;
import com.grepp.moodlink.app.model.keyword.KeywordRepository;
import com.grepp.moodlink.app.model.keyword.entity.KeywordSelection;
import com.grepp.moodlink.app.model.member.dto.MemberDto;
import com.grepp.moodlink.app.model.member.dto.MemberInfoDto;
import com.grepp.moodlink.app.model.member.dto.ModifyDto;
import com.grepp.moodlink.app.model.member.entity.Member;
import com.grepp.moodlink.infra.error.UserNotFoundException;
import com.grepp.moodlink.infra.response.ResponseCode;
import java.time.LocalDate;
import java.util.Optional;
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

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final KeywordRepository keywordRepository;

    @Transactional
    public Optional<MemberInfoDto> GetMemberInfo(String userId) {
        Optional<Member> user = memberRepository.findById(userId);
        return user.map(MemberInfoDto::ToDto);
    }

    @Transactional
    public void modifyProfile(String userId, ModifyDto dto) {
        Member member = memberRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), member.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        if (passwordEncoder.matches(dto.getNewPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호는 현재 비밀번호와 같을 수 없습니다.");
        }

        if (dto.getNewPassword() != null && !dto.getNewPassword().isBlank()) {
            String encodedPassword = passwordEncoder.encode(dto.getNewPassword());
            member.setPassword(encodedPassword);
        }
        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            member.setUsername(dto.getUsername());
        }

        if (dto.getGenre() != null && !dto.getGenre().isBlank()) {
            member.setGenre(dto.getGenre());
        }

        if (dto.getPeriods() != null && !dto.getPeriods().isBlank()) {
            member.setPeriods(dto.getPeriods());
        }

        member.setUpdatedAt(LocalDate.now());

        memberRepository.save(member);
    }


    @Transactional
    public void signup(MemberDto dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        Member.MemberBuilder builder = Member.builder()
            .id(dto.getUserId())
            .password(encodedPassword)
            .username(dto.getUsername())
            .email(dto.getEmail())
            .genre(dto.getGenre())
            .periods(dto.getPeriods())
            .countries(dto.getCountries())
            .createdAt(LocalDate.now());

        if (dto.getUserId().contains("admin")) {
            builder.role(Role.ROLE_ADMIN);
        } else {
            builder.role(Role.ROLE_USER);
        }

        Member member = builder.build();
        memberRepository.save(member);
    }

    public boolean existsByUserId(String userId) {
        return memberRepository.existsById(userId);
    }


    public Optional<Member> findGenre(String userId) {
        System.out.println(memberRepository.findById(userId));
        return memberRepository.findById(userId);
    }
    @Transactional
    public void selectKeyword(String userId, String keywords) {
        Optional<Member> member = memberRepository.findById(userId);
        Optional<KeywordSelection> keywordSelection = keywordRepository.findByKeywords(keywords);
        if (member.isPresent() && keywordSelection.isPresent()){
            member.get().setKeywordSelection(keywordSelection.get());
            memberRepository.save(member.get());
        }else{
            throw new UserNotFoundException(ResponseCode.USER_NOTFOUND);
        }
    }

}