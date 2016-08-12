package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.OrgBrandObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.OrgBrandEntity;
import com.yunsoo.data.service.repository.OrgBrandRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-08-05
 * Descriptions:
 */
@RestController
@RequestMapping("/orgBrand")
public class OrgBrandController {

    @Autowired
    private OrgBrandRepository orgBrandRepository;


    @RequestMapping(value = "{orgId}", method = RequestMethod.GET)
    public OrgBrandObject getOrgBrandById(@PathVariable(value = "orgId") String orgId) {
        OrgBrandEntity brand = orgBrandRepository.findOne(orgId);
        if (brand == null) {
            throw new NotFoundException("brand not found by [id: " + orgId + "]");
        }
        return toOrgBrandObject(brand);
    }

    @RequestMapping(value = "{orgId}", method = RequestMethod.PUT)
    public void putOrgBrand(@PathVariable(value = "orgId") String orgId,
                            @RequestBody @Valid OrgBrandObject obj) {
        OrgBrandEntity entity = toOrgBrandEntity(obj);
        entity.setOrgId(orgId);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        orgBrandRepository.save(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<OrgBrandObject> query(
            @RequestParam(value = "carrier_id") String carrierId,
            @RequestParam(value = "org_id_in", required = false) List<String> orgIdIn,
            @RequestParam(value = "org_name", required = false) String orgName,
            @RequestParam(value = "category_id", required = false) String categoryId,
            @RequestParam(value = "created_datetime_ge", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime createdDateTimeGE,
            @RequestParam(value = "created_datetime_le", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime createdDateTimeLE,
            @RequestParam(value = "search_text", required = false) String searchText,
            @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
            Pageable pageable,
            HttpServletResponse response) {
        Page<OrgBrandEntity> entityPage = orgBrandRepository.query(
                carrierId,
                orgIdIn == null || orgIdIn.size() == 0 ? null : orgIdIn,
                orgIdIn == null || orgIdIn.size() == 0,
                orgName,
                categoryId,
                createdDateTimeGE,
                createdDateTimeLE,
                searchText,
                pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }

        return entityPage.map(this::toOrgBrandObject).getContent();

    }

    @RequestMapping(value = "count", method = RequestMethod.GET)
    public Long countBrand(@RequestParam(value = "carrier_id") String carrierId) {
        return orgBrandRepository.countByCarrierId(carrierId);
    }

    @RequestMapping(value = "orgId", method = RequestMethod.GET)
    public List<String> getOrgBrandListIds(@RequestParam(value = "carrier_id") String carrierId) {
        return orgBrandRepository.findOrgIdByCarrierId(carrierId);
    }

    private OrgBrandObject toOrgBrandObject(OrgBrandEntity entity) {
        if (entity == null) {
            return null;
        }
        OrgBrandObject obj = new OrgBrandObject();
        obj.setOrgId(entity.getOrgId());
        obj.setOrgName(entity.getOrgName());
        obj.setContactName(entity.getContactName());
        obj.setContactMobile(entity.getContactMobile());
        obj.setEmail(entity.getEmail());
        obj.setBusinessLicenseNumber(entity.getBusinessLicenseNumber());
        obj.setBusinessLicenseStart(entity.getBusinessLicenseStart());
        obj.setBusinessLicenseEnd(entity.getBusinessLicenseEnd());
        obj.setBusinessSphere(entity.getBusinessSphere());
        obj.setComments(entity.getComments());
        obj.setCarrierId(entity.getCarrierId());
        obj.setAttachment(entity.getAttachment());
        obj.setCategoryId(entity.getCategoryId());
        obj.setCreatedDateTime(entity.getCreatedDateTime());
        return obj;
    }

    private OrgBrandEntity toOrgBrandEntity(OrgBrandObject obj) {
        if (obj == null) {
            return null;
        }
        OrgBrandEntity entity = new OrgBrandEntity();
        entity.setOrgId(obj.getOrgId());
        entity.setOrgName(obj.getOrgName());
        entity.setContactName(obj.getContactName());
        entity.setContactMobile(obj.getContactMobile());
        entity.setEmail(obj.getEmail());
        entity.setBusinessLicenseNumber(obj.getBusinessLicenseNumber());
        entity.setBusinessLicenseStart(obj.getBusinessLicenseStart());
        entity.setBusinessLicenseEnd(obj.getBusinessLicenseEnd());
        entity.setBusinessSphere(obj.getBusinessSphere());
        entity.setComments(obj.getComments());
        entity.setCarrierId(obj.getCarrierId());
        entity.setAttachment(obj.getAttachment());
        entity.setCategoryId(obj.getCategoryId());
        entity.setCreatedDateTime(obj.getCreatedDateTime());
        return entity;
    }
}
