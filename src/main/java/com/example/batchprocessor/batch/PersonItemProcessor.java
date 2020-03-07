package com.example.batchprocessor.batch;

import com.example.batchprocessor.database.PersonEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class PersonItemProcessor implements ItemProcessor<PersonEntity, PersonEntity> {

    @Override
    public PersonEntity process(final PersonEntity person) throws Exception {
        final String firstName = person.getFirstName().toUpperCase();
        final String lastName = person.getLastName().toUpperCase();

        final PersonEntity transformedPerson = new PersonEntity(firstName, lastName);
        log.info("Processing complete. person={} converted to newPerson={}", person, transformedPerson);

        return transformedPerson;
    }
}
