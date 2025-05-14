package com.grepp.moodlink.app.model.home;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface GeminiFortuneAiService extends FortuneAiService {

    @Override
    @UserMessage("오늘의 운세를 한줄로 알려줘")
    @SystemMessage("당신은 하루의 운세를 알려주는 전문가입니다.")
    String generateFortune();
}
