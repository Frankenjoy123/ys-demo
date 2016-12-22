package com.yunsoo.key.dao.repository;

import com.yunsoo.key.dao.entity.KeySerialNoEntity;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-12-20
 * Descriptions:
 */
public interface KeySerialNoRepository extends Repository<KeySerialNoEntity, String> {

    KeySerialNoEntity findOne(String id);

    KeySerialNoEntity save(KeySerialNoEntity entity);

    List<KeySerialNoEntity> findByOrgId(String orgId);

    List<KeySerialNoEntity> findByOrgIdIn(List<String> orgIds);

}
