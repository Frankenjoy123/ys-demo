package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.dto.basic.UserOrganizationFollowing;
import com.yunsoo.api.rabbit.dto.basic.UserProductFollowing;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.UserOrganizationFollowingObject;
import com.yunsoo.common.data.object.UserProductFollowingObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/4/24
 * Descriptions:
 */
@Component
public class UserFollowDomain {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private OrganizationDomain organizationDomain;

    @Autowired
    private ProductBaseDomain productBaseDomain;

    @Autowired
    private ProductCommentsDomain productCommentsDomain;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserFollowDomain.class);


    public UserOrganizationFollowingObject ensureUserOrganizationFollowing(String userId, String orgId) {
        Assert.hasText(userId);
        Assert.hasText(orgId);
        List<UserOrganizationFollowingObject> objects = dataAPIClient.get("UserOrganizationFollowing?user_id={userId}&org_id={orgId}",
                new ParameterizedTypeReference<List<UserOrganizationFollowingObject>>() {
                }, userId, orgId);
        if (objects.size() == 0) {
            UserOrganizationFollowingObject object = new UserOrganizationFollowingObject();
            object.setUserId(userId);
            object.setOrgId(orgId);
            return dataAPIClient.post("UserOrganizationFollowing", object, UserOrganizationFollowingObject.class);
        } else {
            return objects.get(0);
        }
    }

    public UserProductFollowingObject ensureUserProductFollowing(String userId, String productBaseId) {
        Assert.hasText(userId);
        Assert.hasText(productBaseId);
        List<UserProductFollowingObject> objects = dataAPIClient.get("UserProductFollowing?user_id={userId}&product_base_id={productBaseId}",
                new ParameterizedTypeReference<List<UserProductFollowingObject>>() {
                }, userId, productBaseId);
        if (objects.size() == 0) {
            UserProductFollowingObject object = new UserProductFollowingObject();
            object.setUserId(userId);
            object.setProductBaseId(productBaseId);
            return dataAPIClient.post("UserProductFollowing", object, UserProductFollowingObject.class);
        } else {
            return objects.get(0);
        }
    }

    public Page<UserOrganizationFollowing> getUserOrganizationFollowingsByUserId(String userId, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("user_id", userId)
                .append(pageable)
                .build();

        Page<UserOrganizationFollowingObject> userFollowingList = dataAPIClient.getPaged("UserOrganizationFollowing" + query,
                new ParameterizedTypeReference<List<UserOrganizationFollowingObject>>() {
                }, userId);

        //fill organization
        Page<UserOrganizationFollowing> resultPage = userFollowingList.map(UserOrganizationFollowing::new);
        for (UserOrganizationFollowing userFollowing : resultPage) {
            OrganizationObject org = organizationDomain.getById(userFollowing.getOrgId());
            if (org != null) {
                userFollowing.setOrgName(org.getName());
                userFollowing.setOrgDesc(org.getDescription());
            } else {
                userFollowing.setOrgId(null);
            }
        }
        resultPage.getContent().removeIf(item -> item.getOrgId() == null);
        return resultPage;
    }

    public Page<UserProductFollowing> getUserProductFollowingsByUserId(String userId, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("user_id", userId)
                .append(pageable)
                .build();

        Page<UserProductFollowingObject> userFollowingList = dataAPIClient.getPaged("UserProductFollowing" + query,
                new ParameterizedTypeReference<List<UserProductFollowingObject>>() {
                }, userId);

        //fill product
        Page<UserProductFollowing> resultPage = userFollowingList.map(UserProductFollowing::new);
        for (UserProductFollowing userFollowing : resultPage) {
            ProductBaseObject productBase = productBaseDomain.getProductBaseById(userFollowing.getProductBaseId());
            if (productBase != null) {
                userFollowing.setProductName(productBase.getName());
                userFollowing.setProductBaseDesc(productBase.getDescription());
                userFollowing.setOrgId(productBase.getOrgId());
                userFollowing.setCommentsCount(productCommentsDomain.getProductCommentsCount(userFollowing.getProductBaseId()));
            } else {
                userFollowing.setProductBaseId(null);
            }
        }
        resultPage.getContent().removeIf(item -> item.getProductBaseId() == null);
        return resultPage;
    }


    public void deleteUserOrganizationFollowing(String userId, String orgId) {
        dataAPIClient.delete("UserOrganizationFollowing?user_id={userId}&org_id={orgId}", userId, orgId);
    }

    public void deleteUserProductFollowing(String userId, String productBaseId) {
        dataAPIClient.delete("UserProductFollowing?user_id={userId}&product_base_id={productBaseId}", userId, productBaseId);
    }

}
