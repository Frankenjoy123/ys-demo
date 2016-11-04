package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductTraceCommentsDomain;
import com.yunsoo.api.dto.ProductTraceComments;
import com.yunsoo.api.key.dto.Product;
import com.yunsoo.api.key.service.ProductService;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.ProductTraceCommentsObject;
import com.yunsoo.common.web.exception.BadRequestException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by:   yan
 * Created on:   10/20/2016
 * Descriptions:
 */
@RestController
@RequestMapping("productTraceComments")
public class ProductTraceCommentsController {

    @Autowired
    private ProductTraceCommentsDomain productTraceCommentsDomain;

    @Autowired
    private ProductService productService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductTraceComments save(@RequestBody ProductTraceComments comments) {
        Product product = productService.getProductByKey(comments.getProductKey());
        if (product == null) {
            throw new BadRequestException("product not found by key: " + comments.getProductKey());
        }
        comments.setProductBaseId(product.getProductBaseId());
        comments.setOrgId(AuthUtils.fixOrgId(null));
        comments.setCreatedDateTime(DateTime.now());
        comments.setCreatedAccountId(AuthUtils.fixAccountId(null));
        comments.setStatusCode(LookupCodes.TraceCommentsStatus.SUBMITTED);
        comments.setTypeCode(LookupCodes.TraceCommentsType.ARBITRARY_PRICE);

        ProductTraceCommentsObject object = productTraceCommentsDomain.save(ProductTraceComments.toProductTraceCommentsObject(comments));
        return new ProductTraceComments(object);
    }

}
