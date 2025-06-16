package com.grepp.moodlink.infra.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@EnableScheduling
@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxPollingScheduler {

    private final OutboxRepository outboxRepository;
    private final RedisTemplate<String,String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Transactional
    @Scheduled(fixedDelayString = "10000")
    public void pollAndPublish() {
        List<Outbox> unpublished = outboxRepository.findByActivatedOrderByCreatedAt(true);
        if(unpublished.isEmpty()) {return;}

        log.info("{}",unpublished);

        unpublished.forEach(outbox -> {
            try {
                String json = objectMapper.writeValueAsString(outbox);
                log.info(json);
                redisTemplate.convertAndSend("moodlink", json);
                outbox.unActivated();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        });

    }
}
