package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductTraceCommentsDomain;
import com.yunsoo.api.dto.ProductTraceComments;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.ProductTraceCommentsObject;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by yan on 10/20/2016.
 */
@RestController
@RequestMapping("producttracecomments")
public class ProductTraceCommentsController {

    @Autowired
    private ProductTraceCommentsDomain domain;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductTraceComments save(@RequestBody ProductTraceComments comments){
        comments.setOrgId(AuthUtils.fixOrgId(null));
        comments.setCreatedDateTime(DateTime.now());
        comments.setCreatedAccountId(AuthUtils.fixAccountId(null));
        comments.setStatusCode(LookupCodes.TraceCommentsStatus.SUBMITTED);
        comments.setTypeCode(LookupCodes.TraceCommentsType.ARBITRARY_PRICE);
        ProductTraceCommentsObject object =  domain.save(ProductTraceComments.toProductTraceCommentsObject(comments));
        return new ProductTraceComments(object);
    }




}
