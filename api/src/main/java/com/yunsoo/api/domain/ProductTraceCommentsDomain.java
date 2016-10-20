package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.ProductTraceCommentsObject;
import com.yunsoo.common.web.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by yan on 10/20/2016.
 */
@Component
public class ProductTraceCommentsDomain {
    @Autowired
    private RestClient dataApiClient;

    public ProductTraceCommentsObject save(ProductTraceCommentsObject object){

        return dataApiClient.post("producttracecomments", object, ProductTraceCommentsObject.class);
    }
}
