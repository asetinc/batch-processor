package com.example.batchprocessor;

import com.example.batchprocessor.batch.BatchConfig;
import com.example.batchprocessor.database.DataRepository;
import com.example.batchprocessor.database.PersonEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.*;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBatchTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {BatchConfig.class})
class BatchIntegrationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    @Autowired
    private FlatFileItemReader<PersonEntity> fileReader;
    @Autowired
    private JdbcBatchItemWriter<PersonEntity> jdbcWriter;


    @Test
    public void test_jobStatus_success() throws Exception {
        // given

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        JobInstance actualJobInstance = jobExecution.getJobInstance();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

        // then
        assertThat(actualJobInstance.getJobName()).isEqualTo("testJob");
        assertThat(actualJobExitStatus.getExitCode()).isEqualTo("COMPLETED");
    }

    @Test
    public void test_step1_statusSuccess() {
        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("step1", defaultJobParameters());
        Collection actualStepExecutions = jobExecution.getStepExecutions();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

        // then
        assertThat(actualStepExecutions.size()).isEqualTo(1);
        assertThat(actualJobExitStatus.getExitCode()).isEqualTo("COMPLETED");
    }

    private JobParameters defaultJobParameters() {
        JobParametersBuilder paramsBuilder = new JobParametersBuilder();
//        paramsBuilder.addString("file.input", "sample-data.csv");
        return paramsBuilder.toJobParameters();
    }


    @Test
    public void test_step1_reader_extractionOfDataFromFile() throws Exception {
        // given
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(defaultJobParameters());

        // when
        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            PersonEntity person;
            fileReader.open(stepExecution.getExecutionContext());
            int expectedId = 1;
            while ((person = fileReader.read()) != null) {

                // then
                assertThat(person.getId()).isEqualTo(String.valueOf(expectedId));
                expectedId++;
//                assertThat(person.getFirstName()).isEqualTo("Mathew");
//                assertThat(person.getLastName()).isEqualTo("Robertson");
//                assertThat(person.getAge()).isEqualTo("39");
            }
            fileReader.close();
            return null;
        });
    }

    @Autowired
    DataSource dataSource;

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void givenMockedStep_whenWriterCalled_thenSuccess() throws Exception {
        // given

        PersonEntity newPerson = new PersonEntity();
        newPerson.setId("1");
        newPerson.setFirstName("Grisham");
        newPerson.setLastName("Firmstone");
        newPerson.setAge("33");
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(defaultJobParameters());

        // when
        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            jdbcTemplate.query("SELECT id, first_name, last_name, age FROM people",
                    (rs, row) -> new PersonEntity(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4))
            ).forEach(person -> assertThat(person.getId()).isEqualTo(1));
            return null;
        });
    }
}