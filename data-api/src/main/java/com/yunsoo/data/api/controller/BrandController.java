package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.AttachmentObject;
import com.yunsoo.common.data.object.BrandObject;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.AttachmentEntity;
import com.yunsoo.data.service.entity.BrandApplicationEntity;
import com.yunsoo.data.service.entity.OrganizationEntity;
import com.yunsoo.data.service.repository.AttachmentRepository;
import com.yunsoo.data.service.repository.BrandApplicationRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return toBrandObject(brandApplicationEntity);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public BrandObject update(@RequestBody BrandObject brand) {
        BrandApplicationEntity brandApplicationEntity = toBrandEntity(brand);
        repository.save(brandApplicationEntity);
        return toBrandObject(brandApplicationEntity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<BrandObject> getByFilter(  @RequestParam(value = "carrier_id", required = false) String carrierId,
            @RequestParam(value = "name", required = false) String name,
            Pageable pageable,
            HttpServletResponse response) {

            Page<BrandApplicationEntity> entityPage = repository.query(name, carrierId,pageable);
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
        AttachmentEntity existEntity =  attachmentRepository.findOne(attachment.getId());
        attachment.setModifiedDateTime(DateTime.now());
        attachment.setCreatedDateTime(existEntity.getCreatedDateTime());
        AttachmentEntity entity = toAttachmentEntity(attachment);
        attachmentRepository.save(entity);
        return toAttachmentObject(entity);
    }

    @RequestMapping(value = "/attachment", method = RequestMethod.GET)
    public List<AttachmentObject> getAttachments(@RequestParam("ids") List<String> ids) {
        List<AttachmentEntity> EntityList =  attachmentRepository.findByIdIn(ids);
        return EntityList.stream().map(this::toAttachmentObject).collect(Collectors.toList());

    }


    private BrandObject toBrandObject(BrandApplicationEntity brand){
        BrandObject brandObj = new BrandObject();
              if(brand!=null) {
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
        }
        return brandObj;
    }

    private BrandApplicationEntity toBrandEntity(BrandObject brand){
        if(brand != null) {
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
            return entity;
        }
        return null;
    }

    private AttachmentEntity toAttachmentEntity(AttachmentObject attachment){
        if(attachment == null)
            return null;

        AttachmentEntity attachmentEntity = new AttachmentEntity();
        attachmentEntity.setId(attachment.getId());
        attachmentEntity.setOriginalFileName(attachment.getOriginalFileName());
        attachmentEntity.setS3FileName(attachment.getS3FileName());
        attachmentEntity.setCreatedDateTime(attachment.getCreatedDateTime());
        attachmentEntity.setModifiedDateTime(attachment.getModifiedDateTime());
        return attachmentEntity;

    }

    private AttachmentObject toAttachmentObject(AttachmentEntity attachment){
        if(attachment == null)
            return null;

        AttachmentObject attachmentObj = new AttachmentObject();
        attachmentObj.setId(attachment.getId());
        attachmentObj.setOriginalFileName(attachment.getOriginalFileName());
        attachmentObj.setS3FileName(attachment.getS3FileName());
        attachmentObj.setCreatedDateTime(attachment.getCreatedDateTime());
        attachmentObj.setModifiedDateTime(attachment.getModifiedDateTime());
        return attachmentObj;

    }

}