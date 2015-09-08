package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.LookupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


/**
 * Created by yan on 9/7/2015.
 */

public interface LookupCodeRepository extends CrudRepository<LookupEntity, String> {

    Page<LookupEntity> findAll(Pageable pageable);

    Page<LookupEntity> findByActive(boolean active, Pageable pageable);

    List<LookupEntity> findByTypeCode(String typeCode);

    List<LookupEntity> findByTypeCodeAndActive(String typeCode, boolean active);

    @Query("select distinct lookup.typeCode from LookupEntity lookup")
    List<String> findDistinctTypCode();
}
