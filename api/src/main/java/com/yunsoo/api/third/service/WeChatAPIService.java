package com.yunsoo.api.third.service;

import com.yunsoo.api.Constants;
import com.yunsoo.api.cache.annotation.ObjectCacheConfig;
import com.yunsoo.api.client.ThirdApiClient;
import com.yunsoo.api.domain.MarketingDomain;
import com.yunsoo.api.domain.OrganizationBrandDomain;
import com.yunsoo.api.third.dto.*;
import com.yunsoo.common.data.object.MarketingObject;
import com.yunsoo.common.data.object.OrgBrandObject;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by:   Admin
 * Created on:   6/29/2016
 * Descriptions:
 */
@Service
@ObjectCacheConfig
public class WeChatAPIService {

    @Autowired
    private ThirdApiClient thirdApiClient;

    @Autowired
    private MarketingDomain marketingDomain;

    @Autowired
    private OrganizationBrandDomain organizationBrandDomain;


    public WeChatAccessToken getUserAccessTokenByOrgId(String orgId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .build();

        return thirdApiClient.get("wechat/token" + query, WeChatAccessToken.class);
    }

    //只应用于yunsu公众号
    public WeChatWebAccessToken getWebToken(String code){
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("code", code)
                .build();
        WeChatWebAccessToken weChatAccessToken = thirdApiClient.get("wechat/web_token" + query, WeChatWebAccessToken.class);
        return weChatAccessToken;
    }

    public WeChatUser getWebUser(String code, Boolean detailsFlag) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("code", code)
                .build();
        WeChatWebAccessToken weChatAccessToken = thirdApiClient.get("wechat/web_token" + query, WeChatWebAccessToken.class);
        if (detailsFlag) {
            query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                    .append("access_token", weChatAccessToken.getAccessToken())
                    .append("openid", weChatAccessToken.getOpenId())
                    .build();

            return thirdApiClient.get("wechat/user" + query, WeChatUser.class);
        } else {
            WeChatUser user = new WeChatUser();
            user.setOpenId(weChatAccessToken.getOpenId());
            return user;
        }
    }


    public WeChatOpenIdList getOpenIds(String appId, String secret) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("app_id", appId).append("app_secret", secret)
                .build();
        return thirdApiClient.get("wechat/openid_list" + query, WeChatOpenIdList.class);
    }

    public Map<String, Object> getConfig(String orgId, String url) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("url", url)
                .build();
        return thirdApiClient.get("wechat/jssdk/config" + query, new ParameterizedTypeReference<Map<String, Object>>() {
        });
    }

    public Map<String, Object> getPayConfig(String orgId, String openId, String marketingId,
                                            String nonceString, long timestamp, String notifyUrl) {

        MarketingObject marketingObject = marketingDomain.getMarketingById(marketingId);
        WeChatOrderRequest request = new WeChatOrderRequest();
        request.setId(marketingId);
        request.setNonceString(nonceString);
        request.setOpenId(openId);
        request.setPrice(new BigDecimal(marketingObject.getBudget() * 100));
        request.setProdName(marketingObject.getName());
        request.setNotifyUrl(notifyUrl);
        request.setOrderType("marketing");
        String preOrderId = thirdApiClient.post("wechat/unified", request, String.class);

        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("pre_pay_id", preOrderId)
                .append("nonce_str", nonceString)
                .append("timestamp", timestamp)
                .append("org_id", orgId)
                .build();
        return thirdApiClient.get("wechat/jssdk/pay_config" + query, new ParameterizedTypeReference<Map<String, Object>>() {
        });
    }

    @Cacheable(key="T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).WECHAT.toString(), #orgId)")
    public String getOrgIdHasWeChatSettings(String orgId){
        if(Constants.Ids.YUNSU_ORGID.equals(orgId))
            return orgId;
        WeChatServerConfig config = thirdApiClient.get("wechat/server/config/{id}", WeChatServerConfig.class, orgId);
        if(StringUtils.hasText(config.getAppId()))
            return  orgId;

        OrgBrandObject object = organizationBrandDomain.getOrgBrandObjectById(orgId);
        config = thirdApiClient.get("wechat/server/config/{id}", WeChatServerConfig.class, object.getCarrierId());
        if(StringUtils.hasText(config.getAppId()))
            return config.getOrgId();

        return Constants.Ids.YUNSU_ORGID;
    }


}
