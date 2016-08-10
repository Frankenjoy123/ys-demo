package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.BrandApplicationHistoryObject;
import com.yunsoo.common.data.object.BrandApplicationObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.BrandApplicationEntity;
import com.yunsoo.data.service.entity.BrandApplicationHistoryEntity;
import com.yunsoo.data.service.repository.BrandApplicationHistoryRepository;
import com.yunsoo.data.service.repository.BrandApplicationRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-08-05
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/brandApplication")
public class BrandApplicationController {

    @Autowired
    private BrandApplicationRepository brandApplicationRepository;

    @Autowired
    private BrandApplicationHistoryRepository brandApplicationHistoryRepository;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public BrandApplicationObject getById(@PathVariable(value = "id") String id) {
        BrandApplicationEntity brandApplicationEntity = brandApplicationRepository.findOne(id);
        if (brandApplicationEntity == null) {
            throw new NotFoundException("brand application not found by [id: " + id + "]");
        }

        return toBrandApplicationObject(brandApplicationEntity);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public BrandApplicationObject create(@RequestBody @Valid BrandApplicationObject obj) {
        BrandApplicationEntity brandApplicationEntity = toBrandApplicationEntity(obj);
        brandApplicationEntity.setId(null);
        brandApplicationEntity.setCreatedDateTime(DateTime.now());
        brandApplicationEntity.setStatusCode(LookupCodes.BrandApplicationStatus.CREATED);
        brandApplicationEntity = brandApplicationRepository.save(brandApplicationEntity);

        BrandApplicationHistoryEntity historyEntity = toBrandApplicationHistoryEntity(brandApplicationEntity, obj.getCreatedAccountId());
        brandApplicationHistoryRepository.save(historyEntity);

        return toBrandApplicationObject(brandApplicationEntity);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public BrandApplicationObject update(@PathVariable(value = "id") String id,
                                         @RequestBody @Valid BrandApplicationObject obj) {
        BrandApplicationEntity brandApplicationEntity = toBrandApplicationEntity(obj);
        brandApplicationEntity.setId(id);
        brandApplicationEntity = brandApplicationRepository.save(brandApplicationEntity);

        BrandApplicationHistoryEntity historyEntity = toBrandApplicationHistoryEntity(brandApplicationEntity, obj.getCreatedAccountId());
        brandApplicationHistoryRepository.save(historyEntity);

        return toBrandApplicationObject(brandApplicationEntity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<BrandApplicationObject> query(
            @RequestParam(value = "carrier_id", required = false) String carrierId,
            @RequestParam(value = "brand_name", required = false) String brandName,
            @RequestParam(value = "status_code", required = false) String statusCode,
            @RequestParam(value = "has_payment", required = false) Boolean hasPayment,
            @RequestParam(value = "created_datetime_ge", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime createdDateTimeGE,
            @RequestParam(value = "created_datetime_le", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime createdDateTimeLE,
            @RequestParam(value = "search_text", required = false) String searchText,
            Pageable pageable,
            HttpServletResponse response) {
        Page<BrandApplicationEntity> entityPage = brandApplicationRepository.query(
                carrierId,
                brandName,
                statusCode,
                hasPayment,
                createdDateTimeGE,
                createdDateTimeLE,
                searchText,
                pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }
        return entityPage.getContent().stream()
                .map(this::toBrandApplicationObject)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "count", method = RequestMethod.GET)
    public int count(@RequestParam("carrier_id") String carrierId,
                     @RequestParam("status_code") String statusCode,
                     @RequestParam("has_payment") boolean hasPayment) {
        if (hasPayment) {
            return brandApplicationRepository.countByCarrierIdAndStatusCodeAndPaymentIdIsNotNull(carrierId, statusCode);
        } else {
            return brandApplicationRepository.countByCarrierIdAndStatusCodeAndPaymentIdIsNull(carrierId, statusCode);
        }
    }

    @RequestMapping(value = "{id}/history", method = RequestMethod.GET)
    public List<BrandApplicationHistoryObject> getHistoryList(@PathVariable("id") String id) {
        BrandApplicationEntity brandApplicationEntity = brandApplicationRepository.findOne(id);
        if (brandApplicationEntity == null) {
            throw new NotFoundException("brand application not found by [id: " + id + "]");
        }
        return brandApplicationHistoryRepository.findByBrandIdOrderByCreatedDateTimeDesc(id)
                .stream()
                .map((e) -> {
                    BrandApplicationHistoryObject o = toBrandApplicationHistoryObject(e);
                    o.setCarrierId(brandApplicationEntity.getCarrierId());
                    o.setIdentifier(brandApplicationEntity.getIdentifier());
                    return o;
                })
                .collect(Collectors.toList());
    }


    private BrandApplicationObject toBrandApplicationObject(BrandApplicationEntity entity) {
        if (entity == null) {
            return null;
        }
        BrandApplicationObject obj = new BrandApplicationObject();
        obj.setId(entity.getId());
        obj.setBrandName(entity.getBrandName());
        obj.setBrandDesc(entity.getBrandDesc());
        obj.setContactName(entity.getContactName());
        obj.setContactMobile(entity.getContactMobile());
        obj.setEmail(entity.getEmail());
        obj.setBusinessLicenseNumber(entity.getBusinessLicenseNumber());
        obj.setBusinessLicenseStart(entity.getBusinessLicenseStart());
        obj.setBusinessLicenseEnd(entity.getBusinessLicenseEnd());
        obj.setBusinessSphere(entity.getBusinessSphere());
        obj.setComments(entity.getComments());
        obj.setCarrierId(entity.getCarrierId());
        obj.setPaymentId(entity.getPaymentId());
        obj.setCreatedDateTime(entity.getCreatedDateTime());
        obj.setStatusCode(entity.getStatusCode());
        obj.setAttachment(entity.getAttachment());
        obj.setIdentifier(entity.getIdentifier());
        obj.setPassword(entity.getPassword());
        obj.setHashSalt(entity.getHashSalt());
        obj.setInvestigatorAttachment(entity.getInvestigatorAttachment());
        obj.setInvestigatorComments(entity.getInvestigatorComments());
        obj.setRejectReason(entity.getRejectReason());
        obj.setCategoryId(entity.getCategoryId());
        return obj;
    }

    private BrandApplicationEntity toBrandApplicationEntity(BrandApplicationObject obj) {
        if (obj == null) {
            return null;
        }
        BrandApplicationEntity entity = new BrandApplicationEntity();
        entity.setId(obj.getId());
        entity.setBrandName(obj.getBrandName());
        entity.setBrandDesc(obj.getBrandDesc());
        entity.setContactName(obj.getContactName());
        entity.setContactMobile(obj.getContactMobile());
        entity.setEmail(obj.getEmail());
        entity.setBusinessLicenseNumber(obj.getBusinessLicenseNumber());
        entity.setBusinessLicenseStart(obj.getBusinessLicenseStart());
        entity.setBusinessLicenseEnd(obj.getBusinessLicenseEnd());
        entity.setBusinessSphere(obj.getBusinessSphere());
        entity.setComments(obj.getComments());
        entity.setCarrierId(obj.getCarrierId());
        entity.setPaymentId(obj.getPaymentId());
        entity.setCreatedDateTime(obj.getCreatedDateTime());
        entity.setStatusCode(obj.getStatusCode());
        entity.setAttachment(obj.getAttachment());
        entity.setIdentifier(obj.getIdentifier());
        entity.setPassword(obj.getPassword());
        entity.setHashSalt(obj.getHashSalt());
        entity.setInvestigatorAttachment(obj.getInvestigatorAttachment());
        entity.setInvestigatorComments(obj.getInvestigatorComments());
        entity.setRejectReason(obj.getRejectReason());
        entity.setCategoryId(obj.getCategoryId());
        return entity;
    }

    private BrandApplicationHistoryObject toBrandApplicationHistoryObject(BrandApplicationHistoryEntity entity) {
        if (entity == null) {
            return null;
        }
        BrandApplicationHistoryObject obj = new BrandApplicationHistoryObject();
        obj.setHistoryId(entity.getId());
        obj.setId(entity.getBrandId());
        obj.setBrandName(entity.getBrandName());
        obj.setBrandDesc(entity.getBrandDesc());
        obj.setContactName(entity.getContactName());
        obj.setContactMobile(entity.getContactMobile());
        obj.setEmail(entity.getEmail());
        obj.setBusinessLicenseNumber(entity.getBusinessLicenseNumber());
        obj.setBusinessLicenseStart(entity.getBusinessLicenseStart());
        obj.setBusinessLicenseEnd(entity.getBusinessLicenseEnd());
        obj.setBusinessSphere(entity.getBusinessSphere());
        obj.setComments(entity.getComments());
        obj.setPaymentId(entity.getPaymentId());
        obj.setCreatedAccountId(entity.getCreatedAccountId());
        obj.setCreatedDateTime(entity.getCreatedDateTime());
        obj.setStatusCode(entity.getStatusCode());
        obj.setAttachment(entity.getAttachment());
        obj.setPassword(entity.getPassword());
        obj.setInvestigatorAttachment(entity.getInvestigatorAttachment());
        obj.setInvestigatorComments(entity.getInvestigatorComments());
        obj.setRejectReason(entity.getRejectReason());
        obj.setCategoryId(entity.getCategoryId());
        return obj;
    }

    private BrandApplicationHistoryEntity toBrandApplicationHistoryEntity(BrandApplicationEntity entity, String accountId) {
        if (entity == null) {
            return null;
        }
        BrandApplicationHistoryEntity historyEntity = new BrandApplicationHistoryEntity();
        historyEntity.setId(null);
        historyEntity.setBrandId(entity.getId());
        historyEntity.setBrandName(entity.getBrandName());
        historyEntity.setBrandDesc(entity.getBrandDesc());
        historyEntity.setContactName(entity.getContactName());
        historyEntity.setContactMobile(entity.getContactMobile());
        historyEntity.setEmail(entity.getEmail());
        historyEntity.setBusinessLicenseNumber(entity.getBusinessLicenseNumber());
        historyEntity.setBusinessLicenseStart(entity.getBusinessLicenseStart());
        historyEntity.setBusinessLicenseEnd(entity.getBusinessLicenseEnd());
        historyEntity.setBusinessSphere(entity.getBusinessSphere());
        historyEntity.setComments(entity.getComments());
        historyEntity.setPaymentId(entity.getPaymentId());
        historyEntity.setCreatedAccountId(accountId);
        historyEntity.setCreatedDateTime(DateTime.now());
        historyEntity.setStatusCode(entity.getStatusCode());
        historyEntity.setAttachment(entity.getAttachment());
        historyEntity.setPassword(entity.getPassword());
        historyEntity.setInvestigatorAttachment(entity.getInvestigatorAttachment());
        historyEntity.setInvestigatorComments(entity.getInvestigatorComments());
        historyEntity.setRejectReason(entity.getRejectReason());
        historyEntity.setCategoryId(entity.getCategoryId());
        return historyEntity;
    }

}
