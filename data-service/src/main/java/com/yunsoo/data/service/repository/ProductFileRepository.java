package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.OperationRecordEntity;
import com.yunsoo.data.service.entity.ProductFileEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Jerry on 4/14/2015.
 */
public interface ProductFileRepository extends PagingAndSortingRepository<ProductFileEntity, Long> {

    Iterable<ProductFileEntity> findByFileType(Integer fileType);

    Iterable<ProductFileEntity> findByCreateByAndFileType(Long createBy, Integer fileType);

    Iterable<ProductFileEntity> findByCreateByAndFileTypeOrderByCreateDateDesc(Long createBy, Integer fileType);

    Iterable<ProductFileEntity> findByCreateByAndStatusAndFileType(Long createBy, Integer status, Integer fileType);

    Iterable<ProductFileEntity> findByCreateByAndStatusAndFileTypeOrderByCreateDateDesc(Long createBy, Integer status, Integer fileType);
}
