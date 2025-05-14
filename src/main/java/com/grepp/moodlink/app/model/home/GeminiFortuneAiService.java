package com.grepp.moodlink.app.model.home;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface GeminiFortuneAiService extends FortuneAiService {

    @Override
    @UserMessage("오늘의 운세를 한줄로 띄워줘. 운세를 보고 하루를 생각할 수 있도록 해줘")
    @SystemMessage("당신은 하루의 운세를 한줄로 띄워주는 시스템입니다.")
    String generateFortune();
}
