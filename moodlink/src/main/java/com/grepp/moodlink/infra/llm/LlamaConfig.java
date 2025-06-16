package com.grepp.moodlink.infra.llm;

import com.grepp.moodlink.app.model.home.LlamaFortuneAiService;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LlamaConfig {

    @Value("${openrouter.api.key}")
    private String apiKey;

    @Value("${openrouter.model}")
    private String model;

    @Value("${openrouter.base-url}")
    private String url;

    @Bean
    public LlamaFortuneAiService llamaService() {
        return AiServices.builder(LlamaFortuneAiService.class)
            .chatLanguageModel(OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(model)
                .baseUrl(url)
                .build())
            .build();
    }
}