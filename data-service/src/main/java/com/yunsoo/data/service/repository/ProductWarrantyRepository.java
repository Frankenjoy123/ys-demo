package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ProductWarrantyEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

import java.util.List;

public interface ProductWarrantyRepository extends FindOneAndSaveRepository<ProductWarrantyEntity, String> {

    List<ProductWarrantyEntity> findByProductBaseId(String productBaseId);

    List<ProductWarrantyEntity> findByAssignPersonName(String assignPersonName);
}
