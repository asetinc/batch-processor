package com.example.batchprocessor.batch;

import com.example.batchprocessor.database.PersonEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobCompletionListener extends JobExecutionListenerSupport {

    private final JdbcTemplate jdbcTemplate;

    public JobCompletionListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
//        super.afterJob(jobExecution);
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Job finished.");

            jdbcTemplate.query("SELECT first_name, last_name FROM people",
                    (rs, row) -> new PersonEntity(
                            rs.getString(1),
                            rs.getString(2))
            ).forEach(person -> log.info("Found <" + person + "> in the database."));
        } else {
            log.info("Job still not finished.");
        }
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        super.beforeJob(jobExecution);
        log.info("Job started.");
    }
}
