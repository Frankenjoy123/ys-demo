package com.yunsoo.api.controller;

import com.yunsoo.api.dto.ProductFile;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.ProductFileObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jerry on 4/14/2015.
 */
@RestController
@RequestMapping(value = "/productfile")
public class ProductFileController {

    @Autowired
    private RestClient dataAPIClient;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductFile> get(@RequestParam(value = "createby", required = false) String createby,
                                 @RequestParam(value = "status", required = false, defaultValue = "0") Integer status,
                                 @RequestParam(value = "filetype", required = false, defaultValue = "0") Integer filetype,
                                 @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

        if (createby == null)
            createby = AuthUtils.getCurrentAccount().getId();

        ProductFileObject[] objects =
                dataAPIClient.get("productfile?createby={createby}&&status={status}&&filetype={filetype}&&pageIndex={pageIndex}&&pageSize={pageSize}",
                        ProductFileObject[].class,
                        createby,
                        status,
                        filetype,
                        pageIndex,
                        pageSize);

        if (objects == null)
            throw new NotFoundException("Product file not found");

        List<ProductFile> productFiles = fromProductFileObjectList(Arrays.asList(objects));

        return productFiles;
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Long getCount(@RequestParam(value = "createby", required = false) String createby,
                         @RequestParam(value = "status", required = false, defaultValue = "0") Integer status,
                         @RequestParam(value = "filetype", required = false, defaultValue = "0") Integer filetype) {

        if (createby == null)
            createby = AuthUtils.getCurrentAccount().getId();

        Long count = 0l;
        count = dataAPIClient.get("productfile/count?createby={createby}&&status={status}&&filetype={filetype}",
                Long.class,
                createby,
                status,
                filetype);

        return count;
    }

    private ProductFile fromProductFileObject(ProductFileObject productFileObject) {
        ProductFile productFile = new ProductFile();
        productFile.setId(productFileObject.getId());
        productFile.setFileName(productFileObject.getFileName());
        productFile.setFileType(productFileObject.getFileType());
        productFile.setOrgId(productFileObject.getOrgId());
        productFile.setCreateBy(productFileObject.getCreateBy());
        productFile.setCreateDateTime(productFileObject.getCreateDate());
        productFile.setFilePath(productFileObject.getFilePath());
        productFile.setStatus(productFileObject.getStatus());

        return productFile;
    }

    private List<ProductFile> fromProductFileObjectList(List<ProductFileObject> productFileObjects) {
        List<ProductFile> productFiles = new ArrayList<ProductFile>();
        for (ProductFileObject model : productFileObjects) {
            productFiles.add(fromProductFileObject(model));
        }

        return productFiles;
    }
}
