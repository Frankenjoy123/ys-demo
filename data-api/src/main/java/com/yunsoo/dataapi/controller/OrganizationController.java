package com.yunsoo.dataapi.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.config.AmazonSetting;
import com.yunsoo.dataapi.dto.ResultWrapper;
import com.yunsoo.dataapi.factory.ResultFactory;
import com.yunsoo.service.OrganizationService;
import com.yunsoo.service.ServiceOperationStatus;
import com.yunsoo.service.contract.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Created by Chen Jerry on 3/12/2015.
 */
@RestController
@RequestMapping("/organization")
public class OrganizationController {
    @Autowired
    private final OrganizationService organizationService;
    @Autowired
    private AmazonSetting amazonSetting;

    @Autowired
    OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<Organization> getOrganizationById(@PathVariable(value = "id") int id) {
        return new ResponseEntity<Organization>(organizationService.get(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<Organization> getOrganizationByName(@PathVariable(value = "name") String name) {
        return new ResponseEntity<Organization>(organizationService.get(name), HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createOrganization(@RequestBody Organization org) {
        long id = organizationService.save(org);
        HttpStatus status = id > 0 ? HttpStatus.CREATED : HttpStatus.UNPROCESSABLE_ENTITY;
        return new ResponseEntity<Long>(id, status);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateOrganization(@RequestBody Organization org) {
        Boolean result = organizationService.update(org).equals(ServiceOperationStatus.Success) ? true : false;
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteOrganization(@PathVariable(value = "id") int id) {
        Boolean result = organizationService.delete(id, 5); //deletePermanantly status is 5 in dev DB
        return new ResponseEntity<Boolean>(result, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/thumbnail/{id}/{imagekey}", method = RequestMethod.GET)
    public ResponseEntity getThumbnail(
            @PathVariable(value = "id") Long id,
            @PathVariable(value = "imagekey") String imagekey) {

        if (id == null || id <= 0) throw new BadRequestException("ID不能小于0！");
        if (imagekey == null || imagekey.isEmpty()) throw new BadRequestException("imagekey不能为空！");
        S3Object s3Object;
        try {
            s3Object = organizationService.getOrgThumbnail(amazonSetting.getS3_basebucket(), amazonSetting.getS3_org_image_url() + "/" + id + "/" + imagekey);
            if (s3Object == null) {
                //throw new NotFoundException(40402, "找不到图片 id = " + id +"  client = " + client);
                s3Object = organizationService.getOrgThumbnail(amazonSetting.getS3_basebucket(), amazonSetting.getS3_org_default_image_url());
            }

            FileObject fileObject = new FileObject();
            fileObject.setSuffix(s3Object.getObjectMetadata().getContentType());
            fileObject.setThumbnailData(IOUtils.toByteArray(s3Object.getObjectContent()));
            fileObject.setLenth(s3Object.getObjectMetadata().getContentLength());
            return new ResponseEntity<FileObject>(fileObject, HttpStatus.OK);

        } catch (IOException ex) {
            //to-do: log
            throw new InternalServerErrorException("图片获取出错！");
        }
    }
}
