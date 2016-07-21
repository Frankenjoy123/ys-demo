package com.yunsoo.api.rabbit.domain;

import com.yunsoo.common.data.object.ProductCommentsObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by  : Haitao
 * Created on  : 2015/8/19
 * Descriptions:
 */

@Component
public class ProductCommentsDomain {

    @Autowired
    private RestClient dataApiClient;

    @Autowired
    private ProductBaseDomain productBaseDomain;

    public Page<ProductCommentsObject> getProductCommentsByFilter(String productBaseId, Integer scoreGE, Integer scoreLE, DateTime lastCommentDatetimeGE, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("product_base_id", productBaseId)
                .append("score_ge", scoreGE)
                .append("score_le", scoreLE)
                .append("last_comment_datetime_ge", lastCommentDatetimeGE)
                .append(pageable)
                .build();

        return dataApiClient.getPaged("productcomments" + query, new ParameterizedTypeReference<List<ProductCommentsObject>>() {
        });

    }

    public ProductCommentsObject getById(String id) {
        return dataApiClient.get("productcomments/{id}", ProductCommentsObject.class, id);
    }

    public ProductCommentsObject createProductComments(ProductCommentsObject productCommentsObject) {
        productCommentsObject.setId(null);
        productCommentsObject.setCreatedDateTime(DateTime.now());
        return dataApiClient.post("productcomments", productCommentsObject, ProductCommentsObject.class);
    }

    public Long getProductCommentsCount(String productBaseId) {
        return dataApiClient.get("productcomments/count/{productBaseId}", Long.class, productBaseId);
    }

    public void deleteProductComments(String id) {
        dataApiClient.delete("productcomments/{id}", id);
    }

}
