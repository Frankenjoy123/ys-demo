package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.ProductSalesTerritoryObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.ProductSalesTerritoryEntity;
import com.yunsoo.data.service.repository.ProductSalesTerritoryRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Haitao
 * Created on:   2015/8/25
 * Descriptions:
 */
@RestController
@RequestMapping("/productsalesterritory")
public class ProductSalesTerritoryController {

    @Autowired
    private ProductSalesTerritoryRepository productSalesTerritoryRepository;

    //create
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductSalesTerritoryObject create(@RequestBody ProductSalesTerritoryObject productSalesTerritoryObject) {
        ProductSalesTerritoryEntity entity = toProductSalesTerritoryEntity(productSalesTerritoryObject);
        entity.setId(null);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        entity.setModifiedAccountId(null);
        entity.setModifiedDateTime(null);
        ProductSalesTerritoryEntity newEntity = productSalesTerritoryRepository.save(entity);
        return toProductSalesTerritoryObject(newEntity);
    }

    //update
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public void update(@PathVariable(value = "id") String id,
                       @RequestBody ProductSalesTerritoryObject productSalesTerritoryObject) {
        ProductSalesTerritoryEntity oldentity = productSalesTerritoryRepository.findOne(id);
        if (oldentity != null) {
            ProductSalesTerritoryEntity entity = toProductSalesTerritoryEntity(productSalesTerritoryObject);
            productSalesTerritoryRepository.save(entity);
        }
    }

    //query
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductSalesTerritoryObject> getByFilter(@RequestParam(value = "product_key", required = false) String productKey,
                                                         @RequestParam(value = "org_agency_id", required = false) String orgAgencyId) {
        List<ProductSalesTerritoryEntity> productSalesTerritoryEntities = new ArrayList<ProductSalesTerritoryEntity>();
        if ((productKey == null) && (orgAgencyId == null)) {
            throw new BadRequestException("at least need one filter parameter [product_key, org_agency_id]");
        }
        if ((productKey != null) && !StringUtils.isEmpty(productKey)) {
            productSalesTerritoryEntities = productSalesTerritoryRepository.findByProductKey(productKey);
        } else if ((orgAgencyId != null) && !StringUtils.isEmpty(orgAgencyId)) {
            productSalesTerritoryEntities = productSalesTerritoryRepository.findByOrgAgencyId(orgAgencyId);
        }
        return productSalesTerritoryEntities.stream()
                .map(this::toProductSalesTerritoryObject)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductSalesTerritoryObject getById(@PathVariable String id) {
        ProductSalesTerritoryEntity entity = findById(id);
        return toProductSalesTerritoryObject(entity);
    }


    //delete
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") String id) {
        ProductSalesTerritoryEntity entity = productSalesTerritoryRepository.findOne(id);
        if (entity != null) {
            productSalesTerritoryRepository.delete(id);
        }
    }

    private ProductSalesTerritoryEntity findById(String id) {
        ProductSalesTerritoryEntity entity = productSalesTerritoryRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("product comments not found by [id: " + id + ']');
        }
        return entity;
    }


    private ProductSalesTerritoryObject toProductSalesTerritoryObject(ProductSalesTerritoryEntity entity) {
        if (entity == null) {
            return null;
        }
        ProductSalesTerritoryObject object = new ProductSalesTerritoryObject();
        object.setId(entity.getId());
        object.setProductKey(entity.getProductKey());
        object.setOrgAgencyId(entity.getOrgAgencyId());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setModifiedAccountId(entity.getModifiedAccountId());
        object.setModifiedDateTime(entity.getModifiedDateTime());
        return object;
    }

    private ProductSalesTerritoryEntity toProductSalesTerritoryEntity(ProductSalesTerritoryObject object) {
        if (object == null) {
            return null;
        }
        ProductSalesTerritoryEntity entity = new ProductSalesTerritoryEntity();
        entity.setId(object.getId());
        entity.setProductKey(object.getProductKey());
        entity.setOrgAgencyId(object.getOrgAgencyId());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setModifiedAccountId(object.getModifiedAccountId());
        entity.setModifiedDateTime(object.getModifiedDateTime());
        return entity;
    }
}
