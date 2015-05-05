package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.OrgProductKeyOrderObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.OrgProductKeyOrderEntity;
import com.yunsoo.data.service.repository.OrgProductKeyOrderRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Zhe
 * Created on  : 2015/4/16
 * Descriptions:
 */
@RestController
@RequestMapping("/orgProductKeyOrder")
public class OrgProductKeyOrderController {

    @Autowired
    private OrgProductKeyOrderRepository orgProductKeyOrderRepository;

    @RequestMapping(value = "{id}")
    public OrgProductKeyOrderObject getById(@PathVariable(value = "id") String id) {
        OrgProductKeyOrderEntity entity = orgProductKeyOrderRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("ProductKeyOrder not found by [id: " + id + "]");
        }
        return toOrgProductKeyOrderObject(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<OrgProductKeyOrderObject> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                                      @RequestParam(value = "active", required = false) Boolean active,
                                                      @RequestParam(value = "remain_ge", required = false) Long remainGE,
                                                      @RequestParam(value = "expire_datetime_ge", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime expireDateTimeGE,
                                                      @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                                      @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        PageRequest pageRequest = (pageIndex == null || pageSize == null) ? null : new PageRequest(pageIndex, pageSize);

        List<OrgProductKeyOrderEntity> entities = orgProductKeyOrderRepository.query(orgId, active, remainGE, expireDateTimeGE.toDate(), pageRequest);

        return entities.stream().map(this::toOrgProductKeyOrderObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public OrgProductKeyOrderObject create(@RequestBody OrgProductKeyOrderObject order) {
        OrgProductKeyOrderEntity entity = toOrgProductKeyOrderEntity(order);
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
        OrgProductKeyOrderEntity newEntity = orgProductKeyOrderRepository.save(entity);
        return toOrgProductKeyOrderObject(newEntity);
    }

//    @RequestMapping(value = "/batch", method = RequestMethod.POST)
//    @ResponseStatus(HttpStatus.CREATED)
//    public void batchCreate(@RequestBody Iterable<OrgOrder> orgOrders) {
//        Iterable<OrgProductKeyOrderEntity> orgOrderEntities = orgProductKeyOrderRepository.save(OrgOrder.ToEntityList(orgOrders));
////        return newEntity.getId();
//    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.OK)
    public OrgProductKeyOrderObject patchUpdate(@PathVariable(value = "id") String id,
                                                @RequestBody OrgProductKeyOrderObject order) {
        OrgProductKeyOrderEntity entity = orgProductKeyOrderRepository.findOne(id);
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

        OrgProductKeyOrderEntity newEntity = orgProductKeyOrderRepository.save(entity);
        return toOrgProductKeyOrderObject(newEntity);
    }


    OrgProductKeyOrderObject toOrgProductKeyOrderObject(OrgProductKeyOrderEntity entity) {
        if (entity == null) {
            return null;
        }
        OrgProductKeyOrderObject object = new OrgProductKeyOrderObject();
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

    OrgProductKeyOrderEntity toOrgProductKeyOrderEntity(OrgProductKeyOrderObject object) {
        if (object == null) {
            return null;
        }
        OrgProductKeyOrderEntity entity = new OrgProductKeyOrderEntity();
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
