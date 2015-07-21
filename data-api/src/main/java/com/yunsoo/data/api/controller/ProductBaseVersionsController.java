package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.ProductBaseVersionsObject;
import com.yunsoo.data.service.entity.ProductBaseVersionsEntity;
import com.yunsoo.data.service.repository.ProductBaseRepository;
import com.yunsoo.data.service.repository.ProductBaseVersionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

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

    @RequestMapping(value = "{productBaseId}/{version}", method = RequestMethod.GET)
    public ProductBaseVersionsObject getByProductBaseIdAndVersoin() {
        return null;
    }

    @RequestMapping(value = "{productBaseId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductBaseVersionsObject create(@RequestBody @Valid ProductBaseVersionsObject productBaseVersions) {
        return null;
    }

    @RequestMapping(value = "{productBaseId}/{version}", method = RequestMethod.PUT)
    public void updateByProductBaseIdAndVersoin() {
    }

    @RequestMapping(value = "{productBaseId}/{version}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByProductBaseIdAndVersoin() {
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
        return object;

    }

    private ProductBaseVersionsEntity toGroupEntity(ProductBaseVersionsObject object) {
        return null;
    }

}
