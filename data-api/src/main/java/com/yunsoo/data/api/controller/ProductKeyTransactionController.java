package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.ProductKeyTransactionObject;
import com.yunsoo.common.util.IdGenerator;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.ProductKeyTransactionDetailEntity;
import com.yunsoo.data.service.repository.ProductKeyTransactionDetailRepository;
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
@RequestMapping("/productKeyTransaction")
public class ProductKeyTransactionController {

    @Autowired
    private ProductKeyTransactionDetailRepository productKeyTransactionDetailRepository;

    @RequestMapping(value = "{transactionId}", method = RequestMethod.GET)
    public ProductKeyTransactionObject getByTransactionId(@PathVariable(value = "transactionId") String transactionId) {
        List<ProductKeyTransactionDetailEntity> detailEntities = productKeyTransactionDetailRepository.findByTransactionId(transactionId);
        if (detailEntities.size() == 0) {
            throw new NotFoundException("ProductKeyTransaction not found by [transactionId: " + transactionId + "]");
        }
        return toOrgProductKeyTransactionObjectList(detailEntities).get(0);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductKeyTransactionObject> getByFilter(@RequestParam(value = "org_id") String orgId,
                                                         @RequestParam(value = "product_key_batch_id", required = false) String productKeyBatchId,
                                                         @RequestParam(value = "order_id", required = false) String orderId,
                                                         @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        PageRequest pageRequest = (pageIndex == null || pageSize == null) ? null : new PageRequest(pageIndex, pageSize);

        List<ProductKeyTransactionDetailEntity> detailEntities = productKeyTransactionDetailRepository.query(orgId, productKeyBatchId, orderId, pageRequest);

        return toOrgProductKeyTransactionObjectList(detailEntities);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductKeyTransactionObject create(@RequestBody ProductKeyTransactionObject object) {
        if (object.getId() == null) {
            object.setId(IdGenerator.getNew());
        }
        if (object.getCreatedDateTime() == null) {
            object.setCreatedDateTime(DateTime.now());
        }

        List<ProductKeyTransactionDetailEntity> detailEntities = toOrgProductKeyTransactionDetailEntityList(object);
        for (ProductKeyTransactionDetailEntity entity : detailEntities) {
            entity.setId(null); //make sure it's insert
        }
        List<ProductKeyTransactionDetailEntity> newEntities = productKeyTransactionDetailRepository.save(detailEntities);

        return toOrgProductKeyTransactionObjectList(newEntities).get(0);
    }

    @RequestMapping(value = "{transactionId}/statuscode", method = RequestMethod.PUT)
    public void updateStatusCode(@PathVariable(value = "transactionId") String transactionId, @RequestBody String statusCode) {
        List<ProductKeyTransactionDetailEntity> detailEntities = productKeyTransactionDetailRepository.findByTransactionId(transactionId);
        if (detailEntities.size() == 0) {
            throw new NotFoundException("ProductKeyTransaction not found by [transactionId: " + transactionId + "]");
        }
        if (statusCode == null || statusCode.length() == 0) {
            throw new BadRequestException("status_code can not be null or empty");
        }
        detailEntities.forEach(e -> e.setStatusCode(statusCode));
        productKeyTransactionDetailRepository.save(detailEntities);
    }


    private List<ProductKeyTransactionObject> toOrgProductKeyTransactionObjectList(List<ProductKeyTransactionDetailEntity> detailEntities) {
        if (detailEntities == null) {
            return null;
        }
        if (detailEntities.size() == 0) {
            return new ArrayList<>(0);
        }
        Map<String, ProductKeyTransactionObject> transactionIdMap = new HashMap<>();
        List<ProductKeyTransactionObject> objects = new ArrayList<>();

        for (ProductKeyTransactionDetailEntity entity : detailEntities) {
            String transactionId = entity.getTransactionId();
            ProductKeyTransactionObject object;
            if (transactionIdMap.containsKey(transactionId)) {
                object = transactionIdMap.get(transactionId);
            } else {
                object = new ProductKeyTransactionObject();
                object.setId(entity.getTransactionId());
                object.setOrgId(entity.getOrgId());
                object.setProductKeyBatchId(entity.getProductKeyBatchId());
                object.setCreatedAccountId(entity.getCreatedAccountId());
                object.setCreatedDateTime(entity.getCreatedDateTime());
                object.setDetails(new ArrayList<>());

                transactionIdMap.put(transactionId, object);
                objects.add(object);
            }
            ProductKeyTransactionObject.Detail detail = new ProductKeyTransactionObject.Detail();
            detail.setId(entity.getId());
            detail.setOrderId(entity.getOrderId());
            detail.setQuantity(entity.getQuantity());
            detail.setStatusCode(entity.getStatusCode());

            object.getDetails().add(detail);
        }
        return objects;
    }

    private List<ProductKeyTransactionDetailEntity> toOrgProductKeyTransactionDetailEntityList(ProductKeyTransactionObject object) {
        if (object == null) {
            return null;
        }
        List<ProductKeyTransactionObject.Detail> details = object.getDetails();
        if (details == null || details.size() == 0) {
            return new ArrayList<>();
        }
        List<ProductKeyTransactionDetailEntity> detailEntities = new ArrayList<>();
        for (ProductKeyTransactionObject.Detail detail : details) {
            ProductKeyTransactionDetailEntity entity = new ProductKeyTransactionDetailEntity();
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
