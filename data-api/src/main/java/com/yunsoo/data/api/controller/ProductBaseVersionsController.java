package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ProductBaseVersionsObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnprocessableEntityException;
import com.yunsoo.data.service.entity.ProductBaseVersionsEntity;
import com.yunsoo.data.service.repository.ProductBaseRepository;
import com.yunsoo.data.service.repository.ProductBaseVersionsRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by  : haitao
 * Created on  : 2015/7/20
 * Descriptions:
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
            @PathVariable(value = "version") Integer version) {
        List<ProductBaseVersionsEntity> entities = productBaseVersionsRepository.findByProductBaseIdAndVersion(productBaseId, version);
        if (entities.size() == 0) {
            throw new NotFoundException("product base not found on the specific version");
        }
        return toProductBaseVersionsObject(entities.get(0));
    }

    //query
    @RequestMapping(value = "{product_base_id}", method = RequestMethod.GET)
    public List<ProductBaseVersionsObject> getByProductBaseId(@PathVariable(value = "product_base_id") String productBaseId) {
        return productBaseVersionsRepository.findByProductBaseIdOrderByVersionAsc(productBaseId).stream()
                .map(this::toProductBaseVersionsObject)
                .collect(Collectors.toList());
    }

    //query for multi-productBases
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, List<ProductBaseVersionsObject>> getByFilter(@RequestParam(value = "product_base_ids") List<String> productBaseIds) {
        Map<String, List<ProductBaseVersionsObject>> map = new HashMap<>();
        if (productBaseIds.size() > 0) {
            productBaseVersionsRepository.findByProductBaseIdIn(productBaseIds).forEach(e -> {
                String id = e.getProductBaseId();
                List<ProductBaseVersionsObject> list;
                if (map.containsKey(id)) {
                    list = map.get(id);
                } else {
                    map.put(id, list = new ArrayList<>());
                }
                list.add(toProductBaseVersionsObject(e));
            });
        }
        return map;
    }

    //create
    @RequestMapping(value = "{product_base_id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductBaseVersionsObject create(@PathVariable(value = "product_base_id") String productBaseId,
                                            @RequestBody @Valid ProductBaseVersionsObject productBaseVersionsObject) {
        if (productBaseRepository.findOne(productBaseId) == null) {
            throw new UnprocessableEntityException("product base not found by id");
        }
        ProductBaseVersionsEntity entity = toProductBaseVersionsEntity(productBaseVersionsObject);
        List<ProductBaseVersionsEntity> currentVersions = productBaseVersionsRepository.findByProductBaseIdOrderByVersionAsc(productBaseId);
        int latestVersion = currentVersions.size() == 0 ? 0 : currentVersions.get(currentVersions.size() - 1).getVersion();
        entity.setId(null);
        entity.setProductBaseId(productBaseId);
        entity.setVersion(latestVersion + 1);
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
                                                @PathVariable(value = "version") Integer version,
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
                                                @PathVariable(value = "version") Integer version) {
        productBaseVersionsRepository.deleteByProductBaseIdAndVersion(productBaseId, version);
    }

    private ProductBaseVersionsObject toProductBaseVersionsObject(ProductBaseVersionsEntity entity) {
        if (entity == null) {
            return null;
        }
        ProductBaseVersionsObject object = new ProductBaseVersionsObject();
        object.setProductBaseId(entity.getProductBaseId());
        object.setVersion(entity.getVersion());
        object.setStatusCode(entity.getStatusCode());
        object.setReviewComments(entity.getReviewComments());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setModifiedAccountId(entity.getModifiedAccountId());
        object.setModifiedDateTime(entity.getModifiedDateTime());

        ProductBaseObject pbObject = new ProductBaseObject();
        pbObject.setId(entity.getProductBaseId());
        pbObject.setVersion(entity.getVersion());
        pbObject.setOrgId(entity.getOrgId());
        pbObject.setCategoryId(entity.getCategoryId());
        pbObject.setName(entity.getName());
        pbObject.setDescription(entity.getDescription());
        pbObject.setBarcode(entity.getBarcode());
        pbObject.setProductKeyTypeCodes(Arrays.asList(StringUtils.commaDelimitedListToStringArray(entity.getProductKeyTypeCodes())));
        pbObject.setShelfLife(entity.getShelfLife());
        pbObject.setShelfLifeInterval(entity.getShelfLifeInterval());
        pbObject.setChildProductCount(entity.getChildProductCount());
        pbObject.setImage(entity.getImage());
        pbObject.setComments(entity.getComments());

        object.setProductBase(pbObject);
        return object;

    }

    private ProductBaseVersionsEntity toProductBaseVersionsEntity(ProductBaseVersionsObject object) {
        if (object == null) {
            return null;
        }
        ProductBaseVersionsEntity entity = new ProductBaseVersionsEntity();
        entity.setProductBaseId(object.getProductBaseId());
        entity.setVersion(object.getVersion());
        entity.setStatusCode(object.getStatusCode());
        entity.setReviewComments(object.getReviewComments());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setModifiedAccountId(object.getModifiedAccountId());
        entity.setModifiedDateTime(object.getModifiedDateTime());

        ProductBaseObject pbObject = object.getProductBase();
        if (pbObject != null) {
            entity.setOrgId(pbObject.getOrgId());
            entity.setCategoryId(pbObject.getCategoryId());
            entity.setName(pbObject.getName());
            entity.setDescription(pbObject.getDescription());
            entity.setBarcode(pbObject.getBarcode());
            entity.setProductKeyTypeCodes(StringUtils.collectionToCommaDelimitedString(pbObject.getProductKeyTypeCodes()));
            entity.setShelfLife(pbObject.getShelfLife());
            entity.setShelfLifeInterval(pbObject.getShelfLifeInterval());
            entity.setChildProductCount(pbObject.getChildProductCount());
            entity.setImage(pbObject.getImage());
            entity.setComments(pbObject.getComments());
        }
        return entity;
    }

}
