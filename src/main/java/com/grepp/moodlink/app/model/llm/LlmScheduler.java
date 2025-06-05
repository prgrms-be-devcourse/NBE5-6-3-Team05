package com.grepp.moodlink.app.model.llm;

public class LlmScheduler {
    private static final int BATCH_SIZE = 5; // RPM은 15지만 TPM에 걸릴 듯 하기에 보다 조금 작게
    private static final long DELAY_BATCHES = 60 * 1000; // 1분 단위


}
