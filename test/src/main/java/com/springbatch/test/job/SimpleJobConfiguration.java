package com.springbatch.test.job;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SimpleJobConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    @Bean
    public Job simpleJob() {
        return new JobBuilder("simpleJob", jobRepository)
                .start(simpleStep1(null))
                .next(simpleStep2(null))
                .build();
    }

    @Bean
    @JobScope
    public Step simpleStep1(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return new StepBuilder("simpleStep1", jobRepository)
                .tasklet((contribution,chunkContext) -> {
                    log.info(">>>>>>> this is step1");
                    log.info(">>>>>>> requestDate = {}",requestDate);
                    return RepeatStatus.FINISHED;
                },transactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step simpleStep2(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return new StepBuilder("simpleStep2", jobRepository)
                .tasklet((contribution,chunkContext) -> {
                    log.info(">>>>>>> this is step2");
                    log.info("requestDate = {}",requestDate);
                    return RepeatStatus.FINISHED;
                },transactionManager)
                .build();
    }
}
