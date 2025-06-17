package com.grepp.moodlink.infra.event;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxRepository extends JpaRepository<Outbox, String> {

    List<Outbox> findByActivatedOrderByCreatedAt(Boolean activated);
}
