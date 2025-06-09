package com.grepp.moodlink.app.model.llm.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class RecommendationBatch {
    // job 정의
    @Bean
    public Job recommendationJob(JobRepository jobRepository, Step recommendationStep) {
        return new JobBuilder("recommendationJob", jobRepository)
                .start(recommendationStep)
                .build();
    }

    // step 정의
    @Bean
    public Step recommendationStep(JobRepository jobRepository,
                                   PlatformTransactionManager transactionManager,
                                   ItemReader<String> keywordItemReader,
                                   RecommendationProcessor recommendationProcessor,
                                   ItemWriter<RecommendationDto> recommendationWriter) {
        return new StepBuilder("recommendationStep", jobRepository)
                .<String, RecommendationDto>chunk(10, transactionManager) // 10개씩 모아서
                .reader(keywordItemReader) // 키워드를 읽고
                .processor(recommendationProcessor) // llmservice를 호출
                .writer(recommendationWriter)
                .build();
    }
}
