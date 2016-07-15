package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.BrandObject;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.BrandEntity;
import com.yunsoo.data.service.entity.OrganizationEntity;
import com.yunsoo.data.service.repository.BrandRepository;
import com.yunsoo.data.service.repository.OrganizationRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Chen Jerry
 * Created on  : 3/12/2015
 * Descriptions:
 */
@RestController
@RequestMapping("/organization")
public class OrganizationController {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private BrandRepository brandRepository;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public OrganizationObject getById(@PathVariable(value = "id") String id) {
        OrganizationEntity organizationEntity = organizationRepository.findOne(id);
        if (organizationEntity == null) {
            throw new NotFoundException("organization not found by [id: " + id + "]");
        }
        return toOrganizationObject(organizationEntity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<OrganizationObject> getByFilter(
            @RequestParam(value = "name", required = false) String name,
            Pageable pageable,
            HttpServletResponse response) {
        if (name != null) {
            return organizationRepository.findByName(name).stream()
                    .map(this::toOrganizationObject)
                    .collect(Collectors.toList());
        } else {
            Page<OrganizationEntity> entityPage = organizationRepository.findAll(pageable);
            if (pageable != null) {
                response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
            }
            return entityPage.getContent().stream()
                    .map(this::toOrganizationObject)
                    .collect(Collectors.toList());
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public OrganizationObject create(@RequestBody OrganizationObject organizationObject) {
        OrganizationEntity entity = toOrganizationEntity(organizationObject);
        entity.setId(null);
        entity.setStatusCode(LookupCodes.OrgStatus.AVAILABLE);
        if(entity.getCreatedDateTime() == null)
            entity.setCreatedDateTime(DateTime.now());
        OrganizationEntity newEntity = organizationRepository.save(entity);
        return toOrganizationObject(newEntity);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public OrganizationObject updateOrgStatus(@PathVariable(value = "id") String id, @RequestBody OrganizationObject organizationObject) {
        OrganizationEntity organizationEntity = organizationRepository.findOne(id);
        if (organizationEntity == null) {
            throw new NotFoundException("organization not found by [id: " + id + "]");
        }

        OrganizationEntity newEntity = toOrganizationEntity(organizationObject);
        newEntity.setCreatedDateTime(organizationEntity.getCreatedDateTime());
        newEntity.setCreatedAccountId(organizationEntity.getCreatedAccountId());
        organizationRepository.save(newEntity);

        return toOrganizationObject(newEntity);
    }



    @RequestMapping(value = "/brand", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public BrandObject createBrand(@RequestBody BrandObject brandObject) {
        OrganizationEntity entity = toOrganizationEntity(brandObject);
        entity.setId(null);
        entity.setTypeCode(LookupCodes.OrgType.BRAND);
        entity.setCreatedDateTime(DateTime.now());
        OrganizationEntity newEntity = organizationRepository.save(entity);

        BrandEntity brandEntity = toBrandEntity(brandObject);
        brandEntity.setOrgId(newEntity.getId());
        brandRepository.save(brandEntity);

        return toBrandObject(brandEntity, newEntity);
    }

    @RequestMapping(value = "/brand/{id}", method = RequestMethod.GET)
    public BrandObject getBrandById(@PathVariable(value = "id") String id) {
        BrandEntity brand = brandRepository.findOne(id);
        if (brand == null) {
            throw new NotFoundException("brand not found by [id: " + id + "]");
        }
        return toBrandObject(brand);
    }

    @RequestMapping(value = "/brand/{id}", method = RequestMethod.PATCH)
    public void patchBrand(@PathVariable(value = "id") String id, @RequestBody BrandObject brandObj) {
        BrandEntity brand = brandRepository.findOne(id);
        if (brand == null) {
            throw new NotFoundException("brand not found by [id: " + id + "]");
        }
        if(brandObj.getContactName() != null)
            brand.setContactName(brandObj.getContactName());

        if(brandObj.getContactMobile() != null)
            brand.setContactMobile(brandObj.getContactMobile());

        if(brandObj.getEmail() != null)
            brand.setEmail(brandObj.getEmail());

        brandRepository.save(brand);

        OrganizationEntity org = organizationRepository.findOne(id);

        if(brandObj.getName() != null)
            org.setName(brandObj.getName());

        organizationRepository.save(org);
    }


    @RequestMapping(value = "{id}/brand/count", method = RequestMethod.GET)
    public int countBrand(@PathVariable(value = "id") String id,
                          @RequestParam(value="status", required = false)String status){
        return brandRepository.countByCarrierIdAndStatus(id, status);
    }

    @RequestMapping(value = "{id}/brand", method = RequestMethod.GET)
    public List<BrandObject> getOrgBrandList(@PathVariable(value = "id") String id,
                                             @RequestParam(value="status", required = false)String status,
                                             @RequestParam(value = "name", required = false) String name,
                                             @RequestParam(value = "search_text", required = false) String searchText,
                                             @RequestParam(value = "start_datetime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime startDateTime,
                                             @RequestParam(value = "end_datetime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime endDateTime,
                                             @RequestParam(value = "category_id", required = false) String categoryId,
                                             Pageable pageable,
                                             HttpServletResponse response) {

        Page<BrandEntity> entityPage = brandRepository.filter(id, status, name, searchText, startDateTime, endDateTime, categoryId, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }

        return entityPage.map(this::toBrandObject).getContent();

    }

    @RequestMapping(value = "{id}/brand/name", method = RequestMethod.GET)
    public List<BrandObject> getOrgBrandListByName(@PathVariable(value = "id") String id,
                                             @RequestParam(value = "name", required = false) String name,
                                             Pageable pageable,
                                             HttpServletResponse response) {

        Page<BrandEntity> entityPage = brandRepository.filterByName(id, name, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }
        return entityPage.map(this::toBrandObject).getContent();
    }


    @RequestMapping(value = "{id}/brandIds", method = RequestMethod.GET)
    public List<String> getOrgBrandListIds(@PathVariable(value = "id") String id) {

        return brandRepository.findOrgIdByCarrierId(id);

    }

    private OrganizationObject toOrganizationObject(OrganizationEntity entity) {
        OrganizationObject object = new OrganizationObject();
        object.setId(entity.getId());
        object.setName(entity.getName());
        object.setStatusCode(entity.getStatusCode());
        object.setDescription(entity.getDescription());
        object.setTypeCode(entity.getTypeCode());
        object.setDetails(entity.getDetails());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        return object;
    }

    private OrganizationEntity toOrganizationEntity(OrganizationObject object) {
        OrganizationEntity entity = new OrganizationEntity();
        entity.setId(object.getId());
        entity.setName(object.getName());
        entity.setStatusCode(object.getStatusCode());
        entity.setDescription(object.getDescription());
        entity.setTypeCode(object.getTypeCode());
        entity.setDetails(object.getDetails());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        return entity;
    }

    private BrandObject toBrandObject(BrandEntity brand, OrganizationEntity org){
        BrandObject brandObj = new BrandObject();
        if(org != null){
            brandObj.setId(org.getId());
            brandObj.setStatusCode(org.getStatusCode());
            brandObj.setCreatedDateTime(org.getCreatedDateTime());
            brandObj.setCreatedAccountId(org.getCreatedAccountId());
            brandObj.setDescription(org.getDescription());
            brandObj.setName(org.getName());
            brandObj.setTypeCode(org.getTypeCode());
        }

        if(brand!=null) {
            brandObj.setComments(brand.getComments());
            brandObj.setBusinessLicenseEnd(brand.getBusinessLicenseEnd());
            brandObj.setBusinessLicenseStart(brand.getBusinessLicenseStart());
            brandObj.setBusinessLicenseNumber(brand.getBusinessLicenseNumber());
            brandObj.setBusinessSphere(brand.getBusinessSphere());
            brandObj.setCarrierId(brand.getCarrierId());
            brandObj.setContactName(brand.getContactName());
            brandObj.setContactMobile(brand.getContactMobile());
            brandObj.setEmail(brand.getEmail());
            brandObj.setAttachment(brand.getAttachment());
            brandObj.setCategoryId(brand.getCategoryId());
        }
        return brandObj;
    }

    private BrandObject toBrandObject(BrandEntity brand){
        BrandObject brandObj = new BrandObject();
        if(brand.getOrganization() != null){
            OrganizationEntity org = brand.getOrganization();
            brandObj.setId(org.getId());
            brandObj.setStatusCode(org.getStatusCode());
            brandObj.setCreatedDateTime(org.getCreatedDateTime());
            brandObj.setCreatedAccountId(org.getCreatedAccountId());
            brandObj.setDescription(org.getDescription());
            brandObj.setName(org.getName());
            brandObj.setTypeCode(org.getTypeCode());
        }

        if(brand!=null) {
            brandObj.setComments(brand.getComments());
            brandObj.setBusinessLicenseEnd(brand.getBusinessLicenseEnd());
            brandObj.setBusinessLicenseStart(brand.getBusinessLicenseStart());
            brandObj.setBusinessLicenseNumber(brand.getBusinessLicenseNumber());
            brandObj.setBusinessSphere(brand.getBusinessSphere());
            brandObj.setCarrierId(brand.getCarrierId());
            brandObj.setContactName(brand.getContactName());
            brandObj.setContactMobile(brand.getContactMobile());
            brandObj.setEmail(brand.getEmail());
            brandObj.setAttachment(brand.getAttachment());
            brandObj.setCategoryId(brand.getCategoryId());
        }
        return brandObj;
    }

    private BrandEntity toBrandEntity(BrandObject brand){
        if(brand != null) {
            BrandEntity entity = new BrandEntity();
            entity.setOrgId(brand.getId());
            entity.setCarrierId(brand.getCarrierId());
            entity.setBusinessSphere(brand.getBusinessSphere());
            entity.setBusinessLicenseStart(brand.getBusinessLicenseStart());
            entity.setBusinessLicenseEnd(brand.getBusinessLicenseEnd());
            entity.setBusinessLicenseNumber(brand.getBusinessLicenseNumber());
            entity.setContactName(brand.getContactName());
            entity.setContactMobile(brand.getContactMobile());
            entity.setComments(brand.getComments());
            entity.setEmail(brand.getEmail());
            entity.setAttachment(brand.getAttachment());
            entity.setCategoryId(brand.getCategoryId());
            return entity;
        }
        return null;
    }

}
