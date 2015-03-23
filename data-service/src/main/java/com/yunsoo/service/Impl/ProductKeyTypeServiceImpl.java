package com.yunsoo.service.Impl;

import com.yunsoo.jpa.repository.ProductKeyTypeRepository;
import com.yunsoo.service.ProductKeyTypeService;
import com.yunsoo.service.contract.lookup.ProductKeyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void save(ProductKeyType productKeyTypeModel) {
        productKeyTypeRepository.save(ProductKeyType.toEntity(productKeyTypeModel));
    }

    @Override
    public void update(ProductKeyType productKeyTypeModel) {
        productKeyTypeRepository.save(ProductKeyType.toEntity(productKeyTypeModel));
    }

    @Override
    public void delete(ProductKeyType productKeyTypeModel) {
        productKeyTypeRepository.delete(ProductKeyType.toEntity(productKeyTypeModel));
    }

    @Override
    @Transactional
    public List<ProductKeyType> getAllProductKeyTypes(boolean active) {
        return ProductKeyType.fromEntityList(productKeyTypeRepository.findAll());
    }

}
