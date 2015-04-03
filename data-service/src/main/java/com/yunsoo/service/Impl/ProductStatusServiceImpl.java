package com.yunsoo.service.Impl;

import com.yunsoo.repository.ProductStatusRepository;
import com.yunsoo.service.ProductStatusService;
import com.yunsoo.service.contract.ProductStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/1/13
 * Descriptions:
 */
@Service("productStatusService")
public class ProductStatusServiceImpl implements ProductStatusService {

    @Autowired
    private ProductStatusRepository productStatusRepository;

    @Override
    public ProductStatus getById(int id) {
        return ProductStatus.fromEntity(productStatusRepository.findOne(id));
    }

    @Override
    public List<ProductStatus> getAll(Boolean activeOnly) {
        return ProductStatus.fromEntities(
                activeOnly == null
                        ? productStatusRepository.findAll()
                        : productStatusRepository.findByActive(activeOnly));
    }

    @Override
    public ProductStatus save(ProductStatus lookup) {
        return ProductStatus.fromEntity(productStatusRepository.save(ProductStatus.toEntity(lookup)));
    }

    @Override
    public void delete(ProductStatus lookup) {
        productStatusRepository.delete(ProductStatus.toEntity(lookup));
    }
}
