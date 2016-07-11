package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.OperationLogEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yan on 7/6/2016.
 */
public interface OperationLogRepository extends CrudRepository<OperationLogEntity, String> {
}
