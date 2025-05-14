package com.grepp.moodlink.infra.config;

import com.grepp.moodlink.app.model.home.GeminiFortuneAiService;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiConfig {

    @Value("${openrouter.api.key}")
    private String apiKey;

    @Value("${openrouter.model}")
    private String model;

    @Bean
    public GeminiFortuneAiService geminiService() {
        return AiServices.builder(GeminiFortuneAiService.class)
            .chatLanguageModel(OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://openrouter.ai/api/v1")
                .modelName(model)
                .build())
            .build();
    }
}