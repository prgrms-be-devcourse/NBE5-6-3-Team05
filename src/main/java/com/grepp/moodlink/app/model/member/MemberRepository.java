package com.grepp.moodlink.app.model.member;

import com.grepp.moodlink.app.model.member.entity.Member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member,String> {
    Optional<Member> findById(String id);

}
