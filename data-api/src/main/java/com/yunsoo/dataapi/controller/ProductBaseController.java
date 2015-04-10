package com.yunsoo.dataapi.controller;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.config.AmazonSetting;
import com.yunsoo.data.service.service.ProductBaseService;
import com.yunsoo.data.service.service.contract.ProductBase;
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
    public ProductBaseObject getById(@PathVariable(value = "id") Long id) {
        ProductBase productBase = productBaseService.getById(id);
        if (productBase == null) {
            throw new NotFoundException("Product");
        }
        ProductBaseObject productBaseObject = new ProductBaseObject();
        BeanUtils.copyProperties(productBase, productBaseObject);
        return productBaseObject;
    }

    @RequestMapping(value = "/thumbnail/{id}/{client}", method = RequestMethod.GET)
    public ResponseEntity getThumbnail(
            @PathVariable(value = "id") Long id,
            @PathVariable(value = "client") String client) {
        if (id == null || id <= 0) throw new BadRequestException("ID不能小于0！");
        if (client == null || client.isEmpty()) throw new BadRequestException("client不能为空！");
        S3Object s3Object;
        try {
            s3Object = productBaseService.getProductThumbnail(amazonSetting.getS3_basebucket(), amazonSetting.getS3_productbaseurl() + "/" + id + "/" + client);
            if (s3Object == null) {
                //throw new NotFoundException(40402, "找不到图片 id = " + id +"  client = " + client);
                s3Object = productBaseService.getProductThumbnail(amazonSetting.getS3_basebucket(), amazonSetting.getS3_product_default_image_url());
            }

            FileObject fileObject = new FileObject();
            fileObject.setSuffix(s3Object.getObjectMetadata().getContentType());
            fileObject.setThumbnailData(IOUtils.toByteArray(s3Object.getObjectContent()));
            fileObject.setLenth(s3Object.getObjectMetadata().getContentLength());
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

    //query
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductBaseObject> getByFilter(
            @RequestParam(value = "manufacturerId", required = false) Integer manufacturerId,
            @RequestParam(value = "categoryId", required = false) Integer categoryId) {
        return productBaseService.getByFilter(manufacturerId, categoryId, true).stream()
                .map(p -> {
                    ProductBaseObject o = new ProductBaseObject();
                    BeanUtils.copyProperties(p, o);
                    return o;
                }).collect(Collectors.toList());
    }

    //create
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody ProductBaseObject productBase) {
        ProductBase p = new ProductBase();
        BeanUtils.copyProperties(productBase, p);
        productBaseService.save(p);
    }

    //patch update, we don't provide functions like update with set null properties.
    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable(value = "id") Long id, @RequestBody ProductBaseObject productBase) {
        productBase.setId(id);
        ProductBase p = new ProductBase();
        BeanUtils.copyProperties(productBase, p);
        productBaseService.patchUpdate(p);
    }

    //delete
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") Long id) {
        productBaseService.deactivate(id);
    }

}
