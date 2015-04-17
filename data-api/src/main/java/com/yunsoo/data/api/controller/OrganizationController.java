package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.OrganizationEntity;
import com.yunsoo.data.service.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public OrganizationObject getOrganizationById(@PathVariable(value = "id") String id) {
        OrganizationEntity organizationEntity = organizationRepository.findOne(id);
        if (organizationEntity == null) {
            throw new NotFoundException("organization not found by [id: " + id + "]");
        }
        return entityToObject(organizationEntity);
    }


//
//    @RequestMapping(value = "/thumbnail/{id}/{imagekey}", method = RequestMethod.GET)
//    public ResponseEntity getThumbnail(
//            @PathVariable(value = "id") Long id,
//            @PathVariable(value = "imagekey") String imagekey) {
//
//        if (id == null || id <= 0) throw new BadRequestException("ID不能小于0！");
//        if (imagekey == null || imagekey.isEmpty()) throw new BadRequestException("imagekey不能为空！");
//        S3Object s3Object;
//        try {
//            s3Object = organizationService.getOrgThumbnail(amazonSetting.getS3_basebucket(), amazonSetting.getS3_org_image_url() + "/" + id + "/" + imagekey);
//            if (s3Object == null) {
//                //throw new NotFoundException(40402, "找不到图片 id = " + id +"  client = " + client);
//                s3Object = organizationService.getOrgThumbnail(amazonSetting.getS3_basebucket(), amazonSetting.getS3_org_default_image_url());
//            }
//
//            FileObject fileObject = new FileObject();
//            fileObject.setSuffix(s3Object.getObjectMetadata().getContentType());
//            fileObject.setThumbnailData(IOUtils.toByteArray(s3Object.getObjectContent()));
//            fileObject.setLenth(s3Object.getObjectMetadata().getContentLength());
//            return new ResponseEntity<FileObject>(fileObject, HttpStatus.OK);
//
//        } catch (IOException ex) {
//            //to-do: log
//            throw new InternalServerErrorException("图片获取出错！");
//        }
//    }

    private OrganizationObject entityToObject(OrganizationEntity entity) {
        OrganizationObject object = new OrganizationObject();
        object.setId(entity.getId());
        object.setName(entity.getName());
        object.setStatusCode(entity.getStatusCode());
        object.setDescription(entity.getDescription());
        object.setTypeCode(entity.getTypeCode());
        object.setImageUri(entity.getImageUri());
        object.setDetails(entity.getDetails());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        return object;
    }
}
