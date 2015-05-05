package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.OrgProductKeyTransactionObject;
import com.yunsoo.common.util.IdGenerator;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.OrgProductKeyTransactionDetailEntity;
import com.yunsoo.data.service.repository.OrgProductKeyTransactionDetailRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by  : Zhe
 * Created on  : 2015/4/16
 * Descriptions:
 * Modified by : Lijian
 * Modified on : 2015/05/05
 */
@RestController
@RequestMapping("/orgproductkeytransaction")
public class OrgProductKeyTransactionController {

    @Autowired
    private OrgProductKeyTransactionDetailRepository orgProductKeyTransactionDetailRepository;

    @RequestMapping(value = "{id}")
    public OrgProductKeyTransactionObject getByTransactionId(@PathVariable(value = "id") String id) {
        List<OrgProductKeyTransactionDetailEntity> detailEntities = orgProductKeyTransactionDetailRepository.findByTransactionId(id);
        if (detailEntities.size() == 0) {
            throw new NotFoundException("ProductKeyTransaction not found by [id: " + id + "]");
        }
        return toOrgProductKeyTransactionObjectList(detailEntities).get(0);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<OrgProductKeyTransactionObject> getByFilter(@RequestParam(value = "org_id") String orgId,
                                                            @RequestParam(value = "product_key_batch_id", required = false) String productKeyBatchId,
                                                            @RequestParam(value = "order_id", required = false) String orderId,
                                                            @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                                            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        PageRequest pageRequest = (pageIndex == null || pageSize == null) ? null : new PageRequest(pageIndex, pageSize);

        List<OrgProductKeyTransactionDetailEntity> detailEntities = orgProductKeyTransactionDetailRepository.query(orgId, productKeyBatchId, orderId, pageRequest);

        return toOrgProductKeyTransactionObjectList(detailEntities);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public OrgProductKeyTransactionObject create(@RequestBody OrgProductKeyTransactionObject object) {
        if (object.getId() == null) {
            object.setId(IdGenerator.getNew());
        }
        if (object.getCreatedDateTime() == null) {
            object.setCreatedDateTime(DateTime.now());
        }

        List<OrgProductKeyTransactionDetailEntity> detailEntities = toOrgProductKeyTransactionDetailEntityList(object);
        for (OrgProductKeyTransactionDetailEntity entity : detailEntities) {
            entity.setId(null); //make sure it's insert
        }
        List<OrgProductKeyTransactionDetailEntity> newEntities = orgProductKeyTransactionDetailRepository.save(detailEntities);

        return toOrgProductKeyTransactionObjectList(newEntities).get(0);
    }
//
//    @RequestMapping(value = "", method = RequestMethod.PATCH)
//    public void patchUpdate(@RequestBody OrgProductKeyTransactionObject orgTransactionDetail) {
//        if (!orgProductKeyTransactionDetailRepository.exists(orgTransactionDetail.getId())) {
//            throw new NotFoundException("找不到Transaction Detail！ id=" + orgTransactionDetail.getId());
//        }
//        OrgProductKeyTransactionDetailEntity entity = orgProductKeyTransactionDetailRepository.save(OrgTransactionDetail.ToEntity(orgTransactionDetail));
//    }

    private List<OrgProductKeyTransactionObject> toOrgProductKeyTransactionObjectList(List<OrgProductKeyTransactionDetailEntity> detailEntities) {
        if (detailEntities == null) {
            return null;
        }
        if (detailEntities.size() == 0) {
            return new ArrayList<>(0);
        }
        Map<String, OrgProductKeyTransactionObject> transactionIdMap = new HashMap<>();
        List<OrgProductKeyTransactionObject> objects = new ArrayList<>();

        for (OrgProductKeyTransactionDetailEntity entity : detailEntities) {
            String transactionId = entity.getTransactionId();
            OrgProductKeyTransactionObject object;
            List<OrgProductKeyTransactionObject.Detail> details;
            if (transactionIdMap.containsKey(transactionId)) {
                object = transactionIdMap.get(transactionId);
            } else {
                object = new OrgProductKeyTransactionObject();
                object.setId(entity.getTransactionId());
                object.setOrgId(entity.getOrgId());
                object.setProductKeyBatchId(entity.getProductKeyBatchId());
                object.setCreatedAccountId(entity.getCreatedAccountId());
                object.setCreatedDateTime(entity.getCreatedDateTime());
                object.setDetails(new ArrayList<>());

                transactionIdMap.put(transactionId, object);
                objects.add(object);
            }
            OrgProductKeyTransactionObject.Detail detail = new OrgProductKeyTransactionObject.Detail();
            detail.setId(entity.getId());
            detail.setOrderId(entity.getOrderId());
            detail.setQuantity(entity.getQuantity());
            detail.setStatusCode(entity.getStatusCode());

            object.getDetails().add(detail);
        }
        return objects;
    }

    private List<OrgProductKeyTransactionDetailEntity> toOrgProductKeyTransactionDetailEntityList(OrgProductKeyTransactionObject object) {
        if (object == null) {
            return null;
        }
        List<OrgProductKeyTransactionObject.Detail> details = object.getDetails();
        if (details == null || details.size() == 0) {
            return null;
        }
        List<OrgProductKeyTransactionDetailEntity> detailEntities = new ArrayList<>();
        for (OrgProductKeyTransactionObject.Detail detail : details) {
            OrgProductKeyTransactionDetailEntity entity = new OrgProductKeyTransactionDetailEntity();
            entity.setId(detail.getId());
            entity.setTransactionId(object.getId());
            entity.setOrgId(object.getOrgId());
            entity.setProductKeyBatchId(object.getProductKeyBatchId());
            entity.setOrderId(detail.getOrderId());
            entity.setQuantity(detail.getQuantity());
            entity.setStatusCode(detail.getStatusCode());
            entity.setCreatedAccountId(object.getCreatedAccountId());
            entity.setCreatedDateTime(object.getCreatedDateTime());
            detailEntities.add(entity);
        }
        return detailEntities;
    }

}
