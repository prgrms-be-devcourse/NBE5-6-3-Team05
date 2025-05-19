package com.grepp.moodlink.app.model.home;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface LlamaFortuneAiService extends FortuneAiService {

    @Override
    @UserMessage("오늘의 운세를 한줄로 적어줘. 운세 내용만 띄워줘. 존댓말 사용해줘.")
    @SystemMessage("당신은 하루의 운세를 한줄로 띄워주는 시스템입니다.")
    String generateFortune();
}
