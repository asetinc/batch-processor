package com.example.batchprocessor.database;

import org.springframework.data.repository.CrudRepository;

public interface DataRepository extends CrudRepository<PersonEntity, String> {
}
