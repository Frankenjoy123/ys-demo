package com.yunsoo.data.api.controller;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.config.AmazonSetting;
import com.yunsoo.data.service.service.ProductBaseService;
import com.yunsoo.data.service.service.contract.ProductBase;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Zhe
 * Created on:   2015/3/13
 * Descriptions:
 */
@RestController
@RequestMapping("/productbase")
public class ProductBaseController {

    @Autowired
    private ProductBaseService productBaseService;
    @Autowired
    private AmazonSetting amazonSetting;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductBaseObject getById(@PathVariable(value = "id") String id) {
        ProductBase productBase = productBaseService.getById(id);
        if (productBase == null) {
            throw new NotFoundException("Product");
        }
        ProductBaseObject productBaseObject = new ProductBaseObject();
        BeanUtils.copyProperties(productBase, productBaseObject);
        return productBaseObject;
    }

    @RequestMapping(value = "/{id}/{client}", method = RequestMethod.GET)
    public ResponseEntity getThumbnail(
            @PathVariable(value = "id") String id,
            @PathVariable(value = "client") String client) {
        if (id == null || id.isEmpty()) throw new BadRequestException("ID不能为空！");
        if (client == null || client.isEmpty()) throw new BadRequestException("client不能为空！");
        S3Object s3Object;
        try {
            s3Object = productBaseService.getProductS3Object(amazonSetting.getS3_basebucket(), amazonSetting.getS3_productbaseurl() + "/" + id + "/" + client);
            if (s3Object == null) {
                s3Object = productBaseService.getProductS3Object(amazonSetting.getS3_basebucket(), amazonSetting.getS3_product_default_image_url());
            }

            FileObject fileObject = new FileObject();
//            fileObject.setSuffix(s3Object.getObjectMetadata().getContentType());
            fileObject.setContentType(s3Object.getObjectMetadata().getContentType());
            fileObject.setData(IOUtils.toByteArray(s3Object.getObjectContent()));
            fileObject.setLength(s3Object.getObjectMetadata().getContentLength());
            return new ResponseEntity<FileObject>(fileObject, HttpStatus.OK);

        } catch (AmazonS3Exception s3ex) {
            if (s3ex.getErrorCode() == "NoSuchKey") {
                throw new NotFoundException(40402, "找不到图片 id = " + id + "  client = " + client);
            }
            throw new InternalServerErrorException(50001, s3ex.getErrorMessage());
        } catch (IOException ex) {
            //to-do: log
            throw new InternalServerErrorException(50002, "图片获取出错！");
        }
    }

    @RequestMapping(value = "/{id}/{key}/json", method = RequestMethod.GET)
    public ResponseEntity getNotes(
            @PathVariable(value = "id") String id,
            @PathVariable(value = "key") String key) {
        if (id == null || id.isEmpty()) throw new BadRequestException("ID不能为空！");
        if (key == null || key.isEmpty()) throw new BadRequestException("key不能为空！");
        S3Object s3Object;
        try {
            s3Object = productBaseService.getProductS3Object(amazonSetting.getS3_basebucket(), amazonSetting.getS3_productbaseurl() + "/" + id + "/" + key + ".json");
            if (s3Object == null) {
                throw new NotFoundException("找不到产品相关的文件（json描述）");
            }
            FileObject fileObject = new FileObject();
            fileObject.setSuffix(s3Object.getObjectMetadata().getContentType());
            fileObject.setData(IOUtils.toByteArray(s3Object.getObjectContent()));
            fileObject.setLength(s3Object.getObjectMetadata().getContentLength());
            return new ResponseEntity<FileObject>(fileObject, HttpStatus.OK);
//            return new ResponseEntity<S3Object>(s3Object, HttpStatus.OK);

        } catch (AmazonS3Exception s3ex) {
            if (s3ex.getErrorCode() == "NoSuchKey") {
                throw new NotFoundException(40402, "找不到Notes.json id = " + id);
            }
            throw new InternalServerErrorException(50001, s3ex.getErrorMessage());
        } catch (IOException ex) {
            //to-do: log
            throw new InternalServerErrorException(50002, "S3文件获取出错！");
        }
    }


    //query
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductBaseObject> getByFilter(
            @RequestParam(value = "org_id", required = false) String orgId,
            @RequestParam(value = "category_id", required = false) Integer categoryId) {
        return productBaseService.getByFilter(orgId, categoryId, LookupCodes.ProductBaseStatus.CUSTOMER_INVISIBLE_STATUS).stream()
                .map(p -> {
                    ProductBaseObject o = new ProductBaseObject();
                    BeanUtils.copyProperties(p, o);
                    return o;
                }).collect(Collectors.toList());
    }

    //create
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody ProductBaseObject productBase) {
        productBase.setCreatedDateTime(DateTime.now());
        ProductBase p = new ProductBase();
        BeanUtils.copyProperties(productBase, p);
        return productBaseService.save(p);
    }

    //patch update, we don't provide functions like update with set null properties.
    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public void patchUpdate(@RequestBody ProductBaseObject productBase) {
        productBase.setModifiedDateTime(DateTime.now());
        ProductBase p = new ProductBase();
        BeanUtils.copyProperties(productBase, p);
        productBaseService.patchUpdate(p);
    }

    //delete
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") String id) {
        productBaseService.deactivate(id);
    }

}
