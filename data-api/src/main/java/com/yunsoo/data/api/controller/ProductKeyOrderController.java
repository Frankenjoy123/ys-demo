package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.ProductKeyOrderObject;
import com.yunsoo.common.util.DateTimeUtils;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.ProductKeyOrderEntity;
import com.yunsoo.data.service.repository.ProductKeyOrderRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by  : Zhe
 * Created on  : 2015/4/16
 * Descriptions:
 */
@RestController
@RequestMapping("/productKeyOrder")
public class ProductKeyOrderController {

    @Autowired
    private ProductKeyOrderRepository productKeyOrderRepository;

    @RequestMapping(value = "{id}")
    public ProductKeyOrderObject getById(@PathVariable(value = "id") String id) {
        ProductKeyOrderEntity entity = productKeyOrderRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("ProductKeyOrder not found by [id: " + id + "]");
        }
        return toProductKeyOrderObject(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductKeyOrderObject> getByFilter(
            @RequestParam(value = "org_id", required = false) String orgId,
            @RequestParam(value = "active", required = false) Boolean active,
            @RequestParam(value = "remain_ge", required = false) Long remainGE,
            @RequestParam(value = "expire_datetime_ge", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime expireDateTimeGE,
            @RequestParam(value = "product_base_id", required = false) String productBaseId,
            Pageable pageable,
            HttpServletResponse response) {

        Page<ProductKeyOrderEntity> entityPage = productKeyOrderRepository.query(orgId, active, remainGE, expireDateTimeGE, productBaseId, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }
        return entityPage.getContent().stream().map(this::toProductKeyOrderObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductKeyOrderObject create(@RequestBody ProductKeyOrderObject order) {
        ProductKeyOrderEntity entity = toProductKeyOrderEntity(order);
        entity.setId(null);
        if (entity.getRemain() == null) {
            entity.setRemain(entity.getTotal());
        }
        if (entity.getActive() == null) {
            entity.setActive(true);
        }
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        ProductKeyOrderEntity newEntity = productKeyOrderRepository.save(entity);
        return toProductKeyOrderObject(newEntity);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public ProductKeyOrderObject patchUpdate(@PathVariable(value = "id") String id,
                                             @RequestBody ProductKeyOrderObject order) {
        ProductKeyOrderEntity entity = productKeyOrderRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("ProductKeyOrder not found by [id: " + id + "]");
        }
        if (order.getRemain() != null) {
            entity.setRemain(order.getRemain());
        }
        if (order.getActive() != null) {
            entity.setActive(order.getActive());
        }
        if (order.getProductBaseId() != null) {
            entity.setProductBaseId(order.getProductBaseId());
        }
        if (order.getDescription() != null) {
            entity.setDescription(order.getDescription());
        }
        if (order.getExpireDateTime() != null) {
            entity.setExpireDateTime(order.getExpireDateTime());
        }

        ProductKeyOrderEntity savedEntity = productKeyOrderRepository.save(entity);
        return toProductKeyOrderObject(savedEntity);
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public List<ProductKeyOrderObject> patchUpdateBatch(@RequestBody List<ProductKeyOrderObject> orders) {
        if (orders.size() == 0) {
            throw new BadRequestException("orders must not be empty");
        }
        List<String> orderIds = orders.stream().map(ProductKeyOrderObject::getId).collect(Collectors.toList());
        List<ProductKeyOrderEntity> entities = productKeyOrderRepository.findAll(orderIds);
        if (entities == null || entities.size() < orderIds.size()) {
            throw new NotFoundException("ProductKeyOrders not found");
        }

        Map<String, ProductKeyOrderEntity> entityMap = entities.stream().collect(Collectors.toMap(ProductKeyOrderEntity::getId, e -> e));
        for (ProductKeyOrderObject order : orders) {
            ProductKeyOrderEntity entity = entityMap.get(order.getId());
            if (entity == null) {
                throw new NotFoundException("ProductKeyOrder not found by [id: " + order.getId() + "]");
            }
            if (order.getRemain() != null) {
                entity.setRemain(order.getRemain());
            }
            if (order.getActive() != null) {
                entity.setActive(order.getActive());
            }
            if (order.getProductBaseId() != null) {
                entity.setProductBaseId(order.getProductBaseId());
            }
            if (order.getDescription() != null) {
                entity.setDescription(order.getDescription());
            }
            if (order.getExpireDateTime() != null) {
                entity.setExpireDateTime(order.getExpireDateTime());
            }
        }

        List<ProductKeyOrderEntity> savedEntities = productKeyOrderRepository.save(entities);

        return savedEntities.stream().map(this::toProductKeyOrderObject).collect(Collectors.toList());
    }

    ProductKeyOrderObject toProductKeyOrderObject(ProductKeyOrderEntity entity) {
        if (entity == null) {
            return null;
        }
        ProductKeyOrderObject object = new ProductKeyOrderObject();
        object.setId(entity.getId());
        object.setOrgId(entity.getOrgId());
        object.setTotal(entity.getTotal());
        object.setRemain(entity.getRemain());
        object.setActive(entity.getActive());
        object.setProductBaseId(entity.getProductBaseId());
        object.setDescription(entity.getDescription());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setExpireDateTime(entity.getExpireDateTime());
        return object;
    }

    ProductKeyOrderEntity toProductKeyOrderEntity(ProductKeyOrderObject object) {
        if (object == null) {
            return null;
        }
        ProductKeyOrderEntity entity = new ProductKeyOrderEntity();
        entity.setId(object.getId());
        entity.setOrgId(object.getOrgId());
        entity.setTotal(object.getTotal());
        entity.setRemain(object.getRemain());
        entity.setActive(object.getActive());
        entity.setProductBaseId(object.getProductBaseId());
        entity.setDescription(object.getDescription());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setExpireDateTime(object.getExpireDateTime());
        return entity;
    }

}
