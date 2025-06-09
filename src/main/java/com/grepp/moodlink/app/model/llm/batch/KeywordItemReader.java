package com.grepp.moodlink.app.model.llm.batch;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class KeywordItemReader {
    @Bean
    public FlatFileItemReader<String> keywordItemReader() {
        return new FlatFileItemReaderBuilder<String>()
                .name("keywordItemReader")
                .resource(new ClassPathResource("keyword_combinations.txt"))
                .lineMapper((line, lineNumber) -> line.trim())
                .build();
    }
}