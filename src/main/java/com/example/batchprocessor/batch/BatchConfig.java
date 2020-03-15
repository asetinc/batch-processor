package com.example.batchprocessor.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


@Slf4j
@Configuration
@Import(Step1.class)
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                               JdbcTemplate jdbcTemplate,
                               Step1 step1,
                               DataSource dataSource) {

        return jobBuilderFactory.get("testJob")
                .incrementer(new RunIdIncrementer())
                .listener(new JobCompletionListener(jdbcTemplate))
                .start(step1.getStep(dataSource))
                .build();
    }
}
