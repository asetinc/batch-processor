package com.example.batchprocessor.batch;

import com.example.batchprocessor.database.DataRepository;
import com.example.batchprocessor.database.PersonEntity;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
public class Step1 {

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step getStep(DataSource dataSource) {
        return stepBuilderFactory.get("step1")
                .<PersonEntity, PersonEntity>chunk(1)
                .reader(fileReader())
                .processor(processor())
                .writer(jdbcWriter(dataSource))
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<PersonEntity> fileReader() {
        return new FlatFileItemReaderBuilder<PersonEntity>()
                .name("step1-fileReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .linesToSkip(1)
                .delimited()
                .names(new String[]{"id","firstName", "lastName","age"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<PersonEntity>() {{
                    setTargetType(PersonEntity.class);
                }})
                .build();
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<PersonEntity> jdbcWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<PersonEntity>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO people (id, first_name, last_name, age) VALUES (:id, :firstName, :lastName, :age)")
                .dataSource(dataSource)
                .build();
    }
}
