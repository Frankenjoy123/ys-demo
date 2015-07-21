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

    //query
    @RequestMapping(value = "{productBaseId}", method = RequestMethod.GET)
    public List<ProductBaseVersionsObject> getByProductBaseIdAndVersoin(@PathVariable(value = "product_base_id") String productBaseId) {
        return productBaseVersionsRepository.findByProductBaseId(productBaseId).stream()
                .map(this::toProductBaseVersionsObject)
                .collect(Collectors.toList());
    }

    //create
    @RequestMapping(value = "{productBaseId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductBaseVersionsObject create(@PathVariable(value = "product_base_id") String productBaseId,
                                            @RequestBody @Valid ProductBaseVersionsObject productBaseVersionsObject) {
        ProductBaseVersionsEntity entity = toProductBaseVersionsEntity(productBaseVersionsObject);
        entity.setId(null);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        entity.setModifiedAccountId(null);
        entity.setModifiedDateTime(null);
        ProductBaseVersionsEntity newEntity = productBaseVersionsRepository.save(entity);
        return toProductBaseVersionsObject(newEntity);
    }

    //update
    @RequestMapping(value = "{productBaseId}/{version}", method = RequestMethod.PUT)
    public void updateByProductBaseIdAndVersoin(@PathVariable(value = "product_base_id") String productBaseId,
                                                @PathVariable(value = "version") String version,
                                                @RequestBody ProductBaseVersionsObject productBaseVersionsObject) {
        List<ProductBaseVersionsEntity> productBaseVersionsEntities = productBaseVersionsRepository.findByProductBaseIdAndVersion(productBaseId, version);
        if (productBaseVersionsEntities.size() == 0) {
            throw new NotFoundException("product base version object not found.");
        }
        ProductBaseVersionsEntity entity = toProductBaseVersionsEntity(productBaseVersionsObject);
        entity.setCreatedAccountId(productBaseVersionsEntities.get(0).getCreatedAccountId());
        entity.setCreatedDateTime(productBaseVersionsEntities.get(0).getCreatedDateTime());
        if (entity.getModifiedDateTime() == null) {
            entity.setModifiedDateTime(DateTime.now());
        }
        productBaseVersionsRepository.save(entity);

    }

    //delete
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByProductBaseIdAndVersoin(@RequestParam(value = "product_base_id") String productBaseId,
                                                @RequestParam(value = "version") String version) {
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
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setModifiedAccountId(object.getModifiedAccountId());
        entity.setModifiedDateTime(object.getModifiedDateTime());
        return entity;
    }

}
