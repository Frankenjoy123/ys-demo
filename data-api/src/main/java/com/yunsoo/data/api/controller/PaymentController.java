package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.PaymentObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.PaymentEntity;
import com.yunsoo.data.service.repository.PaymentRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by  : haitao
 * Created on  : 2016/4/5
 * Descriptions:
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    //create marketing plan, provide API
    @RequestMapping(value = "/brand/alipay", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentObject createPayment(@RequestBody PaymentObject paymentObject) {
        PaymentEntity entity = toPaymentEntity(paymentObject);
        entity.setId(null);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        entity.setPaidDateTime(null);
        PaymentEntity newEntity = paymentRepository.save(entity);
        return toPaymentObject(newEntity);
    }

    @RequestMapping(value = "/brand/alipay/{id}", method = RequestMethod.PUT)
    public void updatePayment(@PathVariable String id, @RequestBody PaymentObject paymentObject) {
        PaymentEntity oldEntity = findPaymentById(id);
        PaymentEntity entity = toPaymentEntity(paymentObject);
        entity.setCreatedDateTime(oldEntity.getCreatedDateTime());
        paymentRepository.save(entity);
    }

    //get payment by id
    @RequestMapping(value = "/brand/{id}", method = RequestMethod.GET)
    public PaymentObject getPaymentById(@PathVariable String id) {
        PaymentEntity entity = findPaymentById(id);
        return toPaymentObject(entity);
    }

    private PaymentEntity findPaymentById(String id) {
        PaymentEntity entity = paymentRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("payment record not found by [id: " + id + ']');
        }
        return entity;
    }

    private PaymentEntity toPaymentEntity(PaymentObject object) {
        if (object == null) {
            return null;
        }
        PaymentEntity entity = new PaymentEntity();
        entity.setId(object.getId());
        entity.setBrandApplicationId(object.getBrandApplicationId());
        entity.setTradeNo(object.getTradeNo());
        entity.setStatusCode(object.getStatusCode());
        entity.setTypeCode(object.getTypeCode());
        entity.setPayTotals(object.getPayTotals());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setPaidDateTime(object.getPaidDateTime());
        entity.setAccount(object.getAccount());
        return entity;
    }

    private PaymentObject toPaymentObject(PaymentEntity entity) {
        if (entity == null) {
            return null;
        }
        PaymentObject object = new PaymentObject();
        object.setId(entity.getId());
        object.setBrandApplicationId(entity.getBrandApplicationId());
        object.setTradeNo(entity.getTradeNo());
        object.setStatusCode(entity.getStatusCode());
        object.setTypeCode(entity.getTypeCode());
        object.setPayTotals(entity.getPayTotals());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setPaidDateTime(entity.getPaidDateTime());
        object.setAccount(entity.getAccount());
        return object;
    }

}
