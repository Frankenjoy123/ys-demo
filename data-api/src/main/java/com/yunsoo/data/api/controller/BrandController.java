package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.AttachmentObject;
import com.yunsoo.common.data.object.BrandApplicationHistoryObject2;
import com.yunsoo.common.data.object.BrandObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.AttachmentEntity;
import com.yunsoo.data.service.entity.BrandApplicationEntity;
import com.yunsoo.data.service.entity.BrandApplicationHistoryEntity;
import com.yunsoo.data.service.repository.AttachmentRepository;
import com.yunsoo.data.service.repository.BrandApplicationHistoryRepository;
import com.yunsoo.data.service.repository.BrandApplicationRepository;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yan on 3/16/2016.
 */
@RestController
@RequestMapping(value = "/brand")
public class BrandController {

    @Autowired
    private BrandApplicationRepository repository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private BrandApplicationHistoryRepository historyRepository;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public BrandObject getById(@PathVariable(value = "id") String id) {
        BrandApplicationEntity brandApplicationEntity = repository.findOne(id);
        if (brandApplicationEntity == null) {
            throw new NotFoundException("Brand application not found by [id: " + id + "]");
        }

        return toBrandObject(brandApplicationEntity);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public BrandObject create(@RequestBody BrandObject brand) {
        BrandApplicationEntity brandApplicationEntity = toBrandEntity(brand);
        brandApplicationEntity.setCreatedDateTime(DateTime.now());
        brandApplicationEntity.setStatusCode(LookupCodes.BrandApplicationStatus.CREATED);
        brandApplicationEntity.setId(null);
        repository.save(brandApplicationEntity);
        BrandApplicationHistoryEntity historyEntity = brandApplicationHistoryEntity(brandApplicationEntity, brand.getCreatedAccountId());
        historyRepository.save(historyEntity);

        return toBrandObject(brandApplicationEntity);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public BrandObject update(@RequestBody BrandObject brand) {
        BrandApplicationEntity brandApplicationEntity = toBrandEntity(brand);
        repository.save(brandApplicationEntity);

        BrandApplicationHistoryEntity historyEntity = brandApplicationHistoryEntity(brandApplicationEntity, brand.getCreatedAccountId());
        historyRepository.save(historyEntity);

        return toBrandObject(brandApplicationEntity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<BrandObject> getByFilter(@RequestParam(value = "carrier_id", required = false) String carrierId,
                                         @RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "status", required = false) String status,
                                         @RequestParam(value = "has_payment", required = false) Boolean hasPayment,
                                         @RequestParam(value = "search_text", required = false) String searchText,
                                         @RequestParam(value = "start_datetime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime startDateTime,
                                         @RequestParam(value = "end_datetime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime endDateTime,
                                         Pageable pageable,  HttpServletResponse response) {

        Page<BrandApplicationEntity> entityPage = repository.query(name, carrierId, status,hasPayment, startDateTime, endDateTime, searchText, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }
        return entityPage.getContent().stream()
                .map(this::toBrandObject)
                .collect(Collectors.toList());

    }


    @RequestMapping(value = "/attachment", method = RequestMethod.POST)
    public AttachmentObject createAttachment(@RequestBody AttachmentObject attachment) {
        attachment.setId(null);
        attachment.setCreatedDateTime(DateTime.now());
        AttachmentEntity entity = toAttachmentEntity(attachment);
        attachmentRepository.save(entity);
        return toAttachmentObject(entity);
    }

    @RequestMapping(value = "/attachment", method = RequestMethod.PUT)
    public AttachmentObject updateAttachment(@RequestBody AttachmentObject attachment) {
        AttachmentEntity existEntity = attachmentRepository.findOne(attachment.getId());
        attachment.setModifiedDateTime(DateTime.now());
        attachment.setCreatedDateTime(existEntity.getCreatedDateTime());
        AttachmentEntity entity = toAttachmentEntity(attachment);
        attachmentRepository.save(entity);
        return toAttachmentObject(entity);
    }

    @RequestMapping(value = "/attachment", method = RequestMethod.GET)
    public List<AttachmentObject> getAttachments(@RequestParam("ids") List<String> ids) {
        List<AttachmentEntity> EntityList = attachmentRepository.findByIdIn(ids);
        return EntityList.stream().map(this::toAttachmentObject).collect(Collectors.toList());

    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public int getPendingApprovalBrand(@RequestParam("carrier_id") String carrierId, @RequestParam("status") String status) {
        return repository.countByCarrierIdAndStatusCode(carrierId, status);
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public List<BrandApplicationHistoryObject2> getHistoryList(@RequestParam("id")String id){
        return historyRepository.findByBrandId(id).stream().map(this::brandApplicationHistoryObject).collect(Collectors.toList());
    }


    private BrandObject toBrandObject(BrandApplicationEntity brand) {
        BrandObject brandObj = new BrandObject();
        if (brand != null) {
            brandObj.setId(brand.getId());
            brandObj.setDescription(brand.getBrandDescription());
            brandObj.setName(brand.getBrandName());
            brandObj.setCreatedDateTime(brand.getCreatedDateTime());
            brandObj.setComments(brand.getComments());
            brandObj.setBusinessLicenseEnd(brand.getBusinessLicenseEnd());
            brandObj.setBusinessLicenseStart(brand.getBusinessLicenseStart());
            brandObj.setBusinessLicenseNumber(brand.getBusinessLicenseNumber());
            brandObj.setBusinessSphere(brand.getBusinessSphere());
            brandObj.setCarrierId(brand.getCarrierId());
            brandObj.setContactName(brand.getContactName());
            brandObj.setContactMobile(brand.getContactMobile());
            brandObj.setEmail(brand.getEmail());
            brandObj.setStatusCode(brand.getStatusCode());
            brandObj.setAttachment(brand.getAttachment());
            brandObj.setIdentifier(brand.getIdentifier());
            brandObj.setPassword(brand.getPassword());
            brandObj.setInvestigatorAttachment(brand.getInvestigatorAttachment());
            brandObj.setInvestigatorComments(brand.getInvestigatorComments());
            brandObj.setRejectReason(brand.getRejectReason());
            brandObj.setHashSalt(brand.getHashSalt());
            brandObj.setPaymentId(brand.getPaymentId());
        }
        return brandObj;
    }

    private BrandApplicationEntity toBrandEntity(BrandObject brand) {
        if (brand != null) {
            BrandApplicationEntity entity = new BrandApplicationEntity();
            entity.setId(brand.getId());
            entity.setCarrierId(brand.getCarrierId());
            entity.setBusinessSphere(brand.getBusinessSphere());
            entity.setBusinessLicenseStart(brand.getBusinessLicenseStart());
            entity.setBusinessLicenseEnd(brand.getBusinessLicenseEnd());
            entity.setBusinessLicenseNumber(brand.getBusinessLicenseNumber());
            entity.setContactName(brand.getContactName());
            entity.setContactMobile(brand.getContactMobile());
            entity.setComments(brand.getComments());
            entity.setEmail(brand.getEmail());
            entity.setBrandDescription(brand.getDescription());
            entity.setBrandName(brand.getName());
            entity.setCreatedDateTime(brand.getCreatedDateTime());
            entity.setStatusCode(brand.getStatusCode());
            entity.setAttachment(brand.getAttachment());
            entity.setIdentifier(brand.getIdentifier());
            entity.setPassword(brand.getPassword());
            entity.setInvestigatorAttachment(brand.getInvestigatorAttachment());
            entity.setInvestigatorComments(brand.getInvestigatorComments());
            entity.setRejectReason(brand.getRejectReason());
            entity.setHashSalt(brand.getHashSalt());
            entity.setPaymentId(brand.getPaymentId());
            return entity;
        }
        return null;
    }

    private AttachmentEntity toAttachmentEntity(AttachmentObject attachment) {
        if (attachment == null)
            return null;

        AttachmentEntity attachmentEntity = new AttachmentEntity();
        attachmentEntity.setId(attachment.getId());
        attachmentEntity.setOriginalFileName(attachment.getOriginalFileName());
        attachmentEntity.setS3FileName(attachment.getS3FileName());
        attachmentEntity.setCreatedDateTime(attachment.getCreatedDateTime());
        attachmentEntity.setModifiedDateTime(attachment.getModifiedDateTime());
        return attachmentEntity;

    }

    private AttachmentObject toAttachmentObject(AttachmentEntity attachment) {
        if (attachment == null)
            return null;

        AttachmentObject attachmentObj = new AttachmentObject();
        attachmentObj.setId(attachment.getId());
        attachmentObj.setOriginalFileName(attachment.getOriginalFileName());
        attachmentObj.setS3FileName(attachment.getS3FileName());
        attachmentObj.setCreatedDateTime(attachment.getCreatedDateTime());
        attachmentObj.setModifiedDateTime(attachment.getModifiedDateTime());
        return attachmentObj;

    }

    private BrandApplicationHistoryObject2 brandApplicationHistoryObject(BrandApplicationHistoryEntity entity){
        if(entity !=null){
            BrandApplicationHistoryObject2 object = new BrandApplicationHistoryObject2();
            BeanUtils.copyProperties(entity, object);
            return object;
        }
        else
            return null;
    }

    private  BrandApplicationHistoryEntity brandApplicationHistoryEntity(BrandApplicationEntity entity, String accountId){
        if(entity !=null){
            BrandApplicationHistoryEntity hEntity = new BrandApplicationHistoryEntity();
            BeanUtils.copyProperties(entity, hEntity);
            hEntity.setBrandId(entity.getId());
            hEntity.setId(null);
            hEntity.setCreatedAccountId(accountId);
            return hEntity;
        }
        else
            return null;
    }

}
