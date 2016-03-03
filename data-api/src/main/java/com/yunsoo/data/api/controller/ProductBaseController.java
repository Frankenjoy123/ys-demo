package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.ProductBaseEntity;
import com.yunsoo.data.service.repository.ProductBaseRepository;
import com.yunsoo.data.service.util.EntityUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Zhe
 * Created on:   2015/3/13
 * Descriptions:
 */
@RestController
@RequestMapping("/productbase")
public class ProductBaseController {

    @Autowired
    private ProductBaseRepository productBaseRepository;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductBaseObject getById(@PathVariable(value = "id") String id) {
        ProductBaseEntity entity = productBaseRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("product base not found by [id:" + id + "]");
        }
        return toProductBaseObject(entity);
    }

    //query
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductBaseObject> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                               Pageable pageable,
                                               HttpServletResponse response) {
        Page<ProductBaseEntity> entityPage;
        if (orgId != null) {
            entityPage = productBaseRepository.findByDeletedFalseAndOrgId(orgId, pageable);
        } else {
            entityPage = productBaseRepository.findByDeletedFalse(pageable);
        }

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }
        return entityPage.getContent().stream()
                .map(this::toProductBaseObject)
                .collect(Collectors.toList());
    }

    //create
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductBaseObject create(@RequestBody ProductBaseObject productBaseObject) {
        ProductBaseEntity entity = toProductBaseEntity(productBaseObject);
        entity.setId(null);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        entity.setModifiedAccountId(null);
        entity.setModifiedDateTime(null);
        return toProductBaseObject(productBaseRepository.save(entity));
    }

    //update
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public void update(@PathVariable(value = "id") String id,
                       @RequestBody ProductBaseObject productBaseObject) {
        ProductBaseEntity oldEntity = productBaseRepository.findOne(id);
        if (oldEntity != null && !oldEntity.isDeleted()) {
            ProductBaseEntity entity = toProductBaseEntity(productBaseObject);
            entity.setId(id);
            entity.setCreatedAccountId(oldEntity.getCreatedAccountId());
            entity.setCreatedDateTime(oldEntity.getCreatedDateTime());
            if (entity.getModifiedDateTime() == null) {
                entity.setModifiedDateTime(DateTime.now());
            }
            productBaseRepository.save(entity);
        }
    }

    //patchUpdate
    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable(value = "id") String id,
                            @RequestBody ProductBaseObject productBaseObject) {
        productBaseObject.setId(null);
        ProductBaseEntity entity = productBaseRepository.findOne(id);
        if(entity==null){
            throw new NotFoundException();
        }
        EntityUtils.patchUpdate(entity, toProductBaseEntity(productBaseObject));
        productBaseRepository.save(entity);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") String id) {
        ProductBaseEntity entity = productBaseRepository.findOne(id);
        if (entity != null && !entity.isDeleted()) {
            entity.setDeleted(true);
            productBaseRepository.save(entity);
        }
    }


    private ProductBaseObject toProductBaseObject(ProductBaseEntity entity) {
        if (entity == null) {
            return null;
        }
        ProductBaseObject object = new ProductBaseObject();
        object.setId(entity.getId());
        object.setVersion(entity.getVersion());
        object.setOrgId(entity.getOrgId());
        object.setStatusCode(entity.getStatusCode());
        object.setCategoryId(entity.getCategoryId());
        object.setName(entity.getName());
        object.setDescription(entity.getDescription());
        object.setBarcode(entity.getBarcode());
        object.setProductKeyTypeCodes(Arrays.asList(StringUtils.commaDelimitedListToStringArray(entity.getProductKeyTypeCodes())));
        object.setShelfLife(entity.getShelfLife());
        object.setShelfLifeInterval(entity.getShelfLifeInterval());
        object.setChildProductCount(entity.getChildProductCount());
        object.setImage(entity.getImage());
        object.setComments(entity.getComments());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setModifiedAccountId(entity.getModifiedAccountId());
        object.setModifiedDateTime(entity.getModifiedDateTime());
        return object;
    }

    private ProductBaseEntity toProductBaseEntity(ProductBaseObject object) {
        if (object == null) {
            return null;
        }
        ProductBaseEntity entity = new ProductBaseEntity();
        entity.setId(object.getId());
        entity.setVersion(object.getVersion());
        entity.setOrgId(object.getOrgId());
        entity.setStatusCode(object.getStatusCode());
        entity.setCategoryId(object.getCategoryId());
        entity.setName(object.getName());
        entity.setDescription(object.getDescription());
        entity.setBarcode(object.getBarcode());
        entity.setProductKeyTypeCodes(StringUtils.collectionToCommaDelimitedString(object.getProductKeyTypeCodes()));
        entity.setShelfLife(object.getShelfLife());
        entity.setShelfLifeInterval(object.getShelfLifeInterval());
        entity.setChildProductCount(object.getChildProductCount());
        entity.setImage(object.getImage());
        entity.setComments(object.getComments());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setModifiedAccountId(object.getModifiedAccountId());
        entity.setModifiedDateTime(object.getModifiedDateTime());
        return entity;
    }
}
