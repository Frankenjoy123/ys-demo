package com.yunsoo.processor.dao.repository;

import com.yunsoo.processor.dao.entity.TaskEntity;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-04-22
 * Descriptions:
 */
public interface TaskRepository extends Repository<TaskEntity, String> {

    TaskEntity findOne(String code);

    List<TaskEntity> findByEnvironment(String environment);

    List<TaskEntity> findByEnvironmentAndEnabledTrue(String environment);

    TaskEntity save(TaskEntity entity);

}
