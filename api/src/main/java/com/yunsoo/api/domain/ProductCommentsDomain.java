package com.yunsoo.api.domain;

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
 * Created on  : 2015/8/24
 * Descriptions:
 */
@Component
public class ProductCommentsDomain {

    @Autowired
    private RestClient dataApiClient;

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

    public Long countProductCommentsByOrgId(String orgId) {
        Long totalQuantity = dataApiClient.get("productcomments/totalcount?org_id=" + orgId, Long.class);
        return totalQuantity;
    }


    public Long getProductCommentsNumber(String productBaseId) {
        return dataApiClient.get("productcomments/count/{productBaseId}", Long.class, productBaseId);
    }


}
