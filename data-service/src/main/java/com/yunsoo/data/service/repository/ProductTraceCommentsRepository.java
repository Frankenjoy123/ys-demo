package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ProductTraceCommentsEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yan on 10/20/2016.
 */
public interface ProductTraceCommentsRepository extends CrudRepository<ProductTraceCommentsEntity, String> {

    public List<ProductTraceCommentsEntity> findByOrgIdAndProductBaseIdAndTypeCode(String orgId, String productBaseId, String typeCode);
}
