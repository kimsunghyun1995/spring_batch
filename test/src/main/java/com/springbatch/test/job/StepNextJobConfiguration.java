package com.springbatch.test.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor

public class StepNextJobConfiguration {

    private final JobRepository jobRepository;
    // todo : 왜 TransactionManager가 아니고 PlatformTransactionManager 일까?
    private final PlatformTransactionManager platformTransactionManager;
    @Bean
    public Job stepNextJob() {
        return new JobBuilder("stepNextJob",jobRepository)
                .start(step1())
                .next(step2())
                .next(step3())
                .build();
    }
    @Bean
    public Step step3() {
        return new StepBuilder("step3",jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>> this is step3");
                    return RepeatStatus.FINISHED;
                }),platformTransactionManager)
                .build();
    }

    @Bean
    public Step step2() {
        return new StepBuilder("step2",jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>> this is step2");
                    return RepeatStatus.FINISHED;
                }),platformTransactionManager)
                .build();
    }

    @Bean
    public Step step1() {
        return new StepBuilder("step1",jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>>>> this is step1");
                    return RepeatStatus.FINISHED;
                }),platformTransactionManager)
                .build();
    }


}
