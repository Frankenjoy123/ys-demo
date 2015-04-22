package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.OperationRecordEntity;
import com.yunsoo.data.service.entity.ProductFileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Jerry on 4/14/2015.
 */
public interface ProductFileRepository extends PagingAndSortingRepository<ProductFileEntity, Long> {

    Long countByCreateByAndFileType(String createBy, Integer fileType);

    Long countByCreateByAndStatusAndFileType(String createBy, Integer status, Integer fileType);

    Page<ProductFileEntity> findByCreateByAndFileTypeOrderByCreateDateDesc(String createBy, Integer fileType, Pageable pageable);

    Page<ProductFileEntity> findByCreateByAndStatusAndFileTypeOrderByCreateDateDesc(String createBy, Integer status, Integer fileType, Pageable pageable);
}
