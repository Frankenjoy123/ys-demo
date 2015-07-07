package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.ProductCategoryObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.ProductCategoryEntity;
import com.yunsoo.data.service.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Zhe
 * Created on:   2015/1/16
 * Descriptions:
 */
@RestController
@RequestMapping("/productcategory")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductCategoryObject getById(@PathVariable(value = "id") Integer id) {
        ProductCategoryEntity entity = productCategoryRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("product category not found by [id: " + id + "]");
        }
        return toProductCategoryObject(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductCategoryObject> getRootProductCategories(
            @RequestParam(value = "parent_id", required = false) Integer parentId) {
        List<ProductCategoryEntity> entities = parentId == null
                ? productCategoryRepository.findAll()
                : productCategoryRepository.findByParentId(parentId);
        return entities.stream().map(this::toProductCategoryObject).collect(Collectors.toList());
    }

    private ProductCategoryObject toProductCategoryObject(ProductCategoryEntity entity) {
        if (entity == null) {
            return null;
        }
        ProductCategoryObject object = new ProductCategoryObject();
        object.setId(entity.getId());
        object.setName(entity.getName());
        object.setDescription(entity.getDescription());
        object.setParentId(entity.getParentId());
        object.setActive(entity.isActive());
        return object;
    }

}
