package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.LookupEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


/**
 * Created by:   yan
 * Created on:   9/7/2015
 * Descriptions:
 */
public interface LookupCodeRepository extends CrudRepository<LookupEntity, LookupEntity.LookupPK> {

    List<LookupEntity> findAll();

    List<LookupEntity> findByActive(boolean active);

    List<LookupEntity> findByTypeCode(String typeCode);

    List<LookupEntity> findByTypeCodeAndActive(String typeCode, boolean active);

    @Query("select distinct t.typeCode from #{#entityName} t")
    List<String> findDistinctTypeCode();

}
