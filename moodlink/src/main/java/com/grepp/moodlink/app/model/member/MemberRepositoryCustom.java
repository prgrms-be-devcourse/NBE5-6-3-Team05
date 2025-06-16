package com.grepp.moodlink.app.model.member;

import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepositoryCustom {

    String findUsernameById(String userId);
}
