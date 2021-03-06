package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.ProductCommentsObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.ProductCommentsEntity;
import com.yunsoo.data.service.repository.ProductBaseRepository;
import com.yunsoo.data.service.repository.ProductCommentsRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Haitao
 * Created on:   2015/8/20
 * Descriptions:
 */
@RestController
@RequestMapping("/productcomments")
public class ProductCommentsController {

    @Autowired
    private ProductCommentsRepository productCommentsRepository;

    @Autowired
    private ProductBaseRepository productBaseRepository;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductCommentsObject getById(@PathVariable String id) {
        ProductCommentsEntity entity = findById(id);
        return toProductCommentsObject(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductCommentsObject> getByFilter(
            @RequestParam(value = "product_base_id", required = true) String productBaseId,
            @RequestParam(value = "score_ge", required = false) Integer scoreGE,
            @RequestParam(value = "score_le", required = false) Integer scoreLE,
            @RequestParam(value = "last_comment_datetime_ge", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            DateTime lastCommentDatetimeGE,
            Pageable pageable,
            HttpServletResponse response) {
        Page<ProductCommentsEntity> entityPage = productCommentsRepository.query(
                productBaseId,
                scoreGE,
                scoreLE,
                lastCommentDatetimeGE,
                pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }
        return entityPage.getContent().stream()
                .map(this::toProductCommentsObject)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/totalcount", method = RequestMethod.GET)
    public Long countMktDrawPrizesByOrgId(@RequestParam(value = "org_id") String orgId) {
        if (orgId == null) {
            throw new BadRequestException("org id is not valid.");
        }
        List<String> productBaseIdIn = productBaseRepository.findIdByOrgId(orgId);
        if (productBaseIdIn != null) {
            return productCommentsRepository.countByProductBaseIdIn(productBaseIdIn);
        } else {
            return null;
        }
    }


    @RequestMapping(value = "/count/{id}", method = RequestMethod.GET)
    public Long getCommentsNumberByProductBaseId(@PathVariable(value = "id") String productBaseId) {
        return productCommentsRepository.countByProductBaseId(productBaseId);
    }

    @RequestMapping(value = "/avgscore/{id}", method = RequestMethod.GET)
    public Long getCommentsAverageScoreByProductBaseId(@PathVariable(value = "id") String productBaseId) {
        return productCommentsRepository.averageScoreByProductBaseId(productBaseId);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ProductCommentsObject createProductComments(@RequestBody @Valid ProductCommentsObject productCommentsObject) {
        ProductCommentsEntity entity = toProductCommentsEntity(productCommentsObject);
        entity.setId(null);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        ProductCommentsEntity newEntity = productCommentsRepository.save(entity);
        return toProductCommentsObject(newEntity);
    }

    //delete
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") String id) {
        ProductCommentsEntity entity = productCommentsRepository.findOne(id);
        if (entity != null) {
            productCommentsRepository.delete(id);
        }
    }

    private ProductCommentsEntity findById(String id) {
        ProductCommentsEntity entity = productCommentsRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("product comments not found by [id: " + id + ']');
        }
        return entity;
    }

    private ProductCommentsObject toProductCommentsObject(ProductCommentsEntity entity) {
        if (entity == null) {
            return null;
        }
        ProductCommentsObject object = new ProductCommentsObject();
        object.setId(entity.getId());
        object.setProductBaseId(entity.getProductBaseId());
        object.setComments(entity.getComments());
        object.setScore(entity.getScore());
        object.setUserId(entity.getUserId());
        object.setYsId(entity.getYsId());
        object.setScanRecordId(entity.getScanRecordId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setAddress(entity.getAddress());
        return object;
    }

    private ProductCommentsEntity toProductCommentsEntity(ProductCommentsObject object) {
        if (object == null) {
            return null;
        }
        ProductCommentsEntity entity = new ProductCommentsEntity();
        entity.setId(object.getId());
        entity.setProductBaseId(object.getProductBaseId());
        entity.setComments(object.getComments());
        entity.setScore(object.getScore());
        entity.setUserId(object.getUserId());
        entity.setYsId(object.getYsId());
        entity.setScanRecordId(object.getScanRecordId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setAddress(object.getAddress());
        return entity;
    }


}
