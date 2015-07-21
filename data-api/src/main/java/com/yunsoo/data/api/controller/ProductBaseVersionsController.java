package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.ProductBaseVersionsObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.ProductBaseVersionsEntity;
import com.yunsoo.data.service.repository.ProductBaseRepository;
import com.yunsoo.data.service.repository.ProductBaseVersionsRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by haitao on 2015/7/20.
 */
@RestController
@RequestMapping("/productbaseversions")
public class ProductBaseVersionsController {

    @Autowired
    private ProductBaseVersionsRepository productBaseVersionsRepository;

    @Autowired
    private ProductBaseRepository productBaseRepository;

    @RequestMapping(value = "{product_base_id}/{version}", method = RequestMethod.GET)
    public ProductBaseVersionsObject getByProductBaseIdAndVersion(
            @PathVariable(value = "product_base_id") String productBaseId,
            @PathVariable(value = "version") String version) {
        List<ProductBaseVersionsEntity> entities = productBaseVersionsRepository.findByProductBaseIdAndVersion(productBaseId, version);
        if (entities.size() == 0) {
            throw new NotFoundException("product base not found on the specific version");
        }
        return toProductBaseVersionsObject(entities.get(0));
    }

    //query
    @RequestMapping(value = "{product_base_id}", method = RequestMethod.GET)
    public List<ProductBaseVersionsObject> getByProductBaseIdAndVersion(@PathVariable(value = "product_base_id") String productBaseId) {
        return productBaseVersionsRepository.findByProductBaseId(productBaseId).stream()
                .map(this::toProductBaseVersionsObject)
                .collect(Collectors.toList());
    }

    //create
    @RequestMapping(value = "{product_base_id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductBaseVersionsObject create(@PathVariable(value = "product_base_id") String productBaseId,
                                            @RequestBody @Valid ProductBaseVersionsObject productBaseVersionsObject) {
        if (productBaseRepository.findOne(productBaseId) == null) {
            throw new NotFoundException("product base not found by id");
        }
        ProductBaseVersionsEntity entity = toProductBaseVersionsEntity(productBaseVersionsObject);
        entity.setId(null);
        entity.setProductBaseId(productBaseId);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        entity.setModifiedAccountId(null);
        entity.setModifiedDateTime(null);
        ProductBaseVersionsEntity newEntity = productBaseVersionsRepository.save(entity);
        return toProductBaseVersionsObject(newEntity);
    }

    //update
    @RequestMapping(value = "{product_base_id}/{version}", method = RequestMethod.PUT)
    public void updateByProductBaseIdAndVersion(@PathVariable(value = "product_base_id") String productBaseId,
                                                @PathVariable(value = "version") String version,
                                                @RequestBody ProductBaseVersionsObject productBaseVersionsObject) {
        List<ProductBaseVersionsEntity> productBaseVersionsEntities = productBaseVersionsRepository.findByProductBaseIdAndVersion(productBaseId, version);
        if (productBaseVersionsEntities.size() == 0) {
            throw new NotFoundException("product base not found on the specific version");
        }
        ProductBaseVersionsEntity oldEntity = productBaseVersionsEntities.get(0);
        ProductBaseVersionsEntity entity = toProductBaseVersionsEntity(productBaseVersionsObject);
        entity.setId(oldEntity.getId());
        entity.setProductBaseId(productBaseId);
        entity.setVersion(version);
        entity.setOrgId(oldEntity.getOrgId());
        entity.setCreatedAccountId(oldEntity.getCreatedAccountId());
        entity.setCreatedDateTime(oldEntity.getCreatedDateTime());
        if (entity.getModifiedDateTime() == null) {
            entity.setModifiedDateTime(DateTime.now());
        }
        productBaseVersionsRepository.save(entity);
    }

    //delete
    @RequestMapping(value = "{product_base_id}/{version}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByProductBaseIdAndVersion(@PathVariable(value = "product_base_id") String productBaseId,
                                                @PathVariable(value = "version") String version) {
        productBaseVersionsRepository.deleteByProductBaseIdAndVersion(productBaseId, version);
    }

    private ProductBaseVersionsObject toProductBaseVersionsObject(ProductBaseVersionsEntity entity) {
        if (entity == null) {
            return null;
        }
        ProductBaseVersionsObject object = new ProductBaseVersionsObject();
        object.setId(entity.getId());
        object.setProductBaseId(entity.getProductBaseId());
        object.setVersion(entity.getVersion());
        object.setStatusCode(entity.getStatusCode());
        object.setOrgId(entity.getOrgId());
        object.setCategoryId(entity.getCategoryId());
        object.setName(entity.getName());
        object.setDescription(entity.getDescription());
        object.setBarcode(entity.getBarcode());
        String codes = entity.getProductKeyTypeCodes();
        if (codes != null) {
            object.setProductKeyTypeCodes(Arrays.asList(StringUtils.delimitedListToStringArray(codes, ",")));
        }
        object.setShelfLife(entity.getShelfLife());
        object.setShelfLifeInterval(entity.getShelfLifeInterval());
        object.setChildProductCount(entity.getChildProductCount());
        object.setComments(entity.getComments());
        object.setReviewComments(entity.getReviewComments());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setModifiedAccountId(entity.getModifiedAccountId());
        object.setModifiedDateTime(entity.getModifiedDateTime());
        return object;

    }

    private ProductBaseVersionsEntity toProductBaseVersionsEntity(ProductBaseVersionsObject object) {
        if (object == null) {
            return null;
        }
        ProductBaseVersionsEntity entity = new ProductBaseVersionsEntity();
        entity.setId(object.getId());
        entity.setProductBaseId(object.getProductBaseId());
        entity.setVersion(object.getVersion());
        entity.setStatusCode(object.getStatusCode());
        entity.setOrgId(object.getOrgId());
        entity.setCategoryId(object.getCategoryId());
        entity.setName(object.getName());
        entity.setDescription(object.getDescription());
        entity.setBarcode(object.getBarcode());
        entity.setProductKeyTypeCodes(StringUtils.collectionToCommaDelimitedString(object.getProductKeyTypeCodes()));
        entity.setShelfLife(object.getShelfLife());
        entity.setShelfLifeInterval(object.getShelfLifeInterval());
        entity.setChildProductCount(object.getChildProductCount());
        entity.setComments(object.getComments());
        entity.setReviewComments(object.getReviewComments());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setModifiedAccountId(object.getModifiedAccountId());
        entity.setModifiedDateTime(object.getModifiedDateTime());
        return entity;
    }

}
