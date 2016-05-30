package com.yunsoo.api.domain;

import com.yunsoo.api.dto.OrganizationCategory;
import com.yunsoo.common.data.object.OrganizationCategoryObject;
import com.yunsoo.common.web.client.RestClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by yan on 5/24/2016.
 */
@Component
public class OrganizationCategoryDomain {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private RestClient dataAPIClient;

    public List<OrganizationCategoryObject> getCategoriesByOrgId(String orgId){
        return dataAPIClient.get("orgcategory?org_id=" + orgId, new ParameterizedTypeReference<List<OrganizationCategoryObject>>() {
        });
    }

    public OrganizationCategoryObject getById(String id){
        return dataAPIClient.get("orgcategory/" + id, OrganizationCategoryObject.class);
    }

    public void saveList(String orgId, List<OrganizationCategory> orgCategoryList){
        List<OrganizationCategoryObject> objectList = new ArrayList<>();
        orgCategoryList.forEach(organizationCategory -> {
            OrganizationCategoryObject object=organizationCategory.toOrganizationCategoryObject();

            if(!(StringUtils.hasText(object.getId()) && StringUtils.hasText(object.getOrgId()))){
                object.setOrgId(orgId);
                object.setId(null);
            }

            objectList.add(object);

        });

        dataAPIClient.put("orgcategory", objectList);
    }


}
