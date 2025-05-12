package com.grepp.moodlink.app.model.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public String findUsernameById(String userId) {
        return em.createQuery("select m.username from Member m where m.id = :userId", String.class)
            .setParameter("userId", userId)
            .setMaxResults(1)
            .getSingleResult();
    }
}
