package com.yunsoo.data.service.service.Impl;

import com.yunsoo.data.service.service.ProductKeyTypeService;
import com.yunsoo.data.service.repository.ProductKeyTypeRepository;
import com.yunsoo.data.service.service.contract.ProductKeyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/1/13
 * Descriptions:
 */
@Service("productKeyTypeService")
public class ProductKeyTypeServiceImpl implements ProductKeyTypeService {

    @Autowired
    private ProductKeyTypeRepository productKeyTypeRepository;

    @Override
    public ProductKeyType getById(int id) {
        return ProductKeyType.fromEntity(productKeyTypeRepository.findOne(id));
    }

    @Override
    public List<ProductKeyType> getAll(Boolean activeOnly) {
        return ProductKeyType.fromEntities(
                activeOnly == null
                        ? productKeyTypeRepository.findAll()
                        : productKeyTypeRepository.findByActive(activeOnly));
    }

    @Override
    public ProductKeyType save(ProductKeyType lookup) {
        return ProductKeyType.fromEntity(productKeyTypeRepository.save(ProductKeyType.toEntity(lookup)));
    }

    @Override
    public void delete(ProductKeyType lookup) {
        productKeyTypeRepository.delete(ProductKeyType.toEntity(lookup));
    }
}
