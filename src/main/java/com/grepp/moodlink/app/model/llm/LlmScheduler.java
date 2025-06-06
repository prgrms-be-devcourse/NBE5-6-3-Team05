package com.grepp.moodlink.app.model.llm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LlmScheduler {
    private static final int BATCH_SIZE = 5; // RPM은 15지만 TPM에 걸릴 듯 하기에 보다 조금 작게
    private static final long DELAY_BATCHES = 60 * 1000; // 1분 단위

    private final LlmService llmService;
    // 자동 실행
    @Scheduled(cron = "0 */1 * * * *")
    public void autoRecommendProcess(){
        log.info("Start ");
    }
}
