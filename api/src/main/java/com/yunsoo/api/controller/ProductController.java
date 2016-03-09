package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AccountDomain;
import com.yunsoo.api.domain.ProductDomain;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.data.object.ProductObject;
import com.yunsoo.common.web.exception.NotAcceptableException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/9
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private ProductDomain productDomain;

    @Autowired
    private AccountDomain accountDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    //todo: change ProductObject to product dto
    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    public ProductObject get(@PathVariable(value = "key") String key) {
        return findProduct(key);
    }

    @RequestMapping(value = "/{key}/active", method = RequestMethod.POST)
    public void active(@PathVariable(value = "key") String key) {
        productDomain.activeProduct(key);
    }

    @RequestMapping(value = "/{key}/delete", method = RequestMethod.POST)
    public void delete(@PathVariable(value = "key") String key) {
        productDomain.deleteProduct(key);
    }

    @RequestMapping(value = "/{key}/details", method = RequestMethod.GET)
    public ResponseEntity<?> getDetails(@PathVariable(value = "key") String key) {
        String details = findProduct(key).getDetails();
        byte[] buffer = details.getBytes(StandardCharsets.UTF_8);
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
        bodyBuilder.contentType(MediaType.APPLICATION_JSON);
        bodyBuilder.contentLength(buffer.length);
        return bodyBuilder.body(new InputStreamResource(new ByteArrayInputStream(buffer)));
    }

    @RequestMapping(value = "/{key}/details", method = RequestMethod.PUT)
    public void putDetails(@PathVariable(value = "key") String key,
                           @RequestBody String details) {
        ProductObject productObject = new ProductObject();
        productObject.setDetails(details);
        productDomain.patchUpdateProduct(key, productObject);
    }

    @RequestMapping(value = "/delete/file", method = RequestMethod.POST)
    public Boolean UploadFile(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException, IllegalAccessException {

        String createdAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        AccountObject accountObject = accountDomain.getById(createdAccountId);
        if (accountObject == null) {
            throw new IllegalAccessException("Current account is not valid to delete product keys.");
        }
        String orgId = accountObject.getOrgId();

        Iterator<String> itr = request.getFileNames();
        MultipartFile file = request.getFile(itr.next());
        boolean batchResult = false;
        try {
            InputStreamReader in = new InputStreamReader(file.getInputStream());
            BufferedReader reader = new BufferedReader(in);
            // StringBuilder content = new StringBuilder();
            String line = null;
            String[] dataArray;
            List<String> productKeys = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (StringUtils.isEmpty(line.replaceAll("\\r\\n", ""))) {
                    continue;
                }
            }
            dataArray = line.split(";");
            reader.close();
            batchResult = productDomain.batchDeleteProducts(dataArray, orgId);

        } catch (NotAcceptableException ex) {
            throw new NotAcceptableException("文件中有不规范的产品码，请检查。");
        }

        return batchResult;
    }

    private ProductObject findProduct(String key) {
        ProductObject productObject = productDomain.getProduct(key);
        if (productObject == null) {
            throw new NotFoundException("product not found");
        }
        return productObject;
    }

}

