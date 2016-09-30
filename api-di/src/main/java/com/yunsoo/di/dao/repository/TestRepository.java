package com.yunsoo.di.dao.repository;


import com.yunsoo.di.dao.entity.TestEntity;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by:   qiyong
 * Created on:   2016/9/10
 * Descriptions:
 */
public interface TestRepository extends Repository<TestEntity, Long> {

    List<TestEntity> findAll();
    TestEntity findOne(Long id);
    TestEntity save(TestEntity entity);
}
