package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductDomain;
import com.yunsoo.api.dto.basic.ProductBase;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/20
 * Descriptions:
 *
 *  * ErrorCode
 *     40401    :   产品找不到
 *     40402    :   产品Thumbnail找不到
 */
@RestController
@RequestMapping(value = "/productbase")
public class ProductBaseController {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private ProductDomain productDomain;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductBase get(@PathVariable(value = "id") String id) {
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("ProductBaseId不应为空！");
        }
        ProductBase productBase = productDomain.getProductBaseById(id);
        if (productBase == null) {
            throw new NotFoundException(40401, "找不到产品");
        }
        return productBase;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductBase> getAllForCurrentOrg() {
        String orgId = "123456789"; //fetch from AuthContext
        return productDomain.getAllProductBaseByOrgId(orgId);
    }

    //create
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody ProductBase productBase) {
        ProductBaseObject p = new ProductBaseObject();
        BeanUtils.copyProperties(productBase, p);
        String id = dataAPIClient.post("productbase/", p, String.class);
        return id;
    }

    //patch update
    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void update(@PathVariable(value = "id") String id, @RequestBody ProductBase productBase) throws Exception {
        //patch update, we don't provide functions like update with set null properties.
        ProductBaseObject p = new ProductBaseObject();
        BeanUtils.copyProperties(productBase, p);
        dataAPIClient.patch("productbase/", p);
    }

    //delete
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") String id) {
        dataAPIClient.delete("productbase/{id}", id);
    }

    @RequestMapping(value = "/thumbnail/{id}/{client}", method = RequestMethod.GET)
    public ResponseEntity<?> getThumbnail(
            @PathVariable(value = "id") String id,
            @PathVariable(value = "client") String client) {
        try {
            FileObject fileObject = dataAPIClient.get("productbase/thumbnail/{id}/{client}", FileObject.class, id, client);
            if (fileObject.getLength() > 0) {
                return ResponseEntity.ok()
                        .contentLength(fileObject.getLength())
                        .contentType(MediaType.parseMediaType(fileObject.getSuffix()))
                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getThumbnailData())));
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(fileObject.getSuffix()))
                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getThumbnailData())));
            }
        } catch (NotFoundException ex) {
            throw new NotFoundException(40402, "找不到产品图片 id = " + id + "  client = " + client);
        }
    }

}
