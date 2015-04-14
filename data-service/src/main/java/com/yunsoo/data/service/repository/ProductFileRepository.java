package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.OperationRecordEntity;
import com.yunsoo.data.service.entity.ProductFileEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Jerry on 4/14/2015.
 */
public interface ProductFileRepository extends CrudRepository<ProductFileEntity, Long> {
    Iterable<ProductFileEntity> findByCreateByAndStatusAndFileType(Long createBy, Integer status, Integer fileType);
}
