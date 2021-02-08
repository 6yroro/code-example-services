package com.test.database.repository;

import com.test.database.entity.DataEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author Alexander Zubkov
 */
public interface DataRepository extends CrudRepository<DataEntity, Long> {

    @NonNull
    List<DataEntity> findAll();

}
