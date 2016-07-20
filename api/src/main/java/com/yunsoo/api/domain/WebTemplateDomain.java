package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.WebTemplateObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Admin on 7/20/2016.
 */
@Component
public class WebTemplateDomain {

    @Autowired
    private RestClient dataAPIClient;

    public List<WebTemplateObject> getWebTemplateList(String typeCode, String restriction) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("type_code", typeCode)
                .append("restriction", restriction)
                .build();

        return dataAPIClient.get("/webTemplate" + query, new ParameterizedTypeReference<List<WebTemplateObject>>() {
        });
    }
}
