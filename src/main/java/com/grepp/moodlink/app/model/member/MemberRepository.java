package com.grepp.moodlink.app.model.member;

import com.grepp.moodlink.app.model.member.entity.User;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<User,String> {
    Optional<User> findById(String id);

}
