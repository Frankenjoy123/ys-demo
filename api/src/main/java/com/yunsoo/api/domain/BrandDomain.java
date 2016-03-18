package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.BrandObject;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by yan on 3/17/2016.
 */
@Component
public class BrandDomain {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private RestClient dataAPIClient;

    public BrandObject createBrand(BrandObject object) {
        object.setId(null);
        object.setCreatedDateTime(DateTime.now());
        return dataAPIClient.post("brand", object, BrandObject.class);
    }

    public void updateBrand(BrandObject object) {
        dataAPIClient.put("brand", object, BrandObject.class);
    }

    public BrandObject getBrandById(String id) {
        try {
            return dataAPIClient.get("brand/{id}", BrandObject.class, id);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public Page<BrandObject> getBrandList(String name, String carrierId, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK).append("name", name)
                .append("carrier_id", carrierId).append(pageable)
                .build();
        return dataAPIClient.getPaged("brand" + query, new ParameterizedTypeReference<List<BrandObject>>() {
        }, name);
    }

}
