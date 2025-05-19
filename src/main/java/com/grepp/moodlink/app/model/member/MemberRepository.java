package com.grepp.moodlink.app.model.member;

import com.grepp.moodlink.app.model.member.entity.Member;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    @Query("SELECT M.username FROM Member M WHERE M.id = :id")
    String findByUsernameById(@Param("id") String id);

}
