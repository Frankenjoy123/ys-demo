package com.yunsoo.data.api.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.config.AmazonSetting;
import com.yunsoo.data.service.entity.OrganizationEntity;
import com.yunsoo.data.service.repository.OrganizationRepository;
import com.yunsoo.data.service.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    private OrganizationService organizationService;

    @Autowired
    private AmazonSetting amazonSetting;

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
        OrganizationEntity newEntity = organizationRepository.save(entity);
        return toOrganizationObject(newEntity);
    }


    @RequestMapping(value = "/{id}/{imageKey}", method = RequestMethod.GET)
    public FileObject getThumbnail(
            @PathVariable(value = "id") String id,
            @PathVariable(value = "imageKey") String imageKey) {

        S3Object s3Object;
        try {
            s3Object = organizationService.getOrgThumbnail(amazonSetting.getS3_basebucket(), amazonSetting.getS3_org_image_url() + "/" + id + "/" + imageKey);
            if (s3Object == null) {
                //throw new NotFoundException(40402, "找不到图片 id = " + id +"  client = " + client);
                s3Object = organizationService.getOrgThumbnail(amazonSetting.getS3_basebucket(), amazonSetting.getS3_org_default_image_url());
            }

            FileObject fileObject = new FileObject();
//            fileObject.setSuffix(s3Object.getObjectMetadata().getContentType());
            fileObject.setContentType(s3Object.getObjectMetadata().getContentType());
            fileObject.setData(IOUtils.toByteArray(s3Object.getObjectContent()));
            fileObject.setLength(s3Object.getObjectMetadata().getContentLength());
            return fileObject;

        } catch (IOException ex) {
            //to-do: log
            throw new InternalServerErrorException("图片获取出错！");
        }
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
}
