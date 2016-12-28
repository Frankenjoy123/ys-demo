package com.yunsoo.api.rabbit.third.service;

import com.yunsoo.api.rabbit.Constants;
import com.yunsoo.api.rabbit.cache.annotation.ObjectCacheConfig;
import com.yunsoo.api.rabbit.client.ThirdApiClient;
import com.yunsoo.api.rabbit.domain.OrgBrandDomain;
import com.yunsoo.api.rabbit.domain.ProductDomain;
import com.yunsoo.api.rabbit.third.dto.*;
import com.yunsoo.common.data.object.OrgBrandObject;
import com.yunsoo.common.util.RandomUtils;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Random;

/**
 * Created by:   yan
 * Created on:   12/7/2016
 * Descriptions:
 */
@Service
@ObjectCacheConfig
public class WeChatService {

    @Autowired
    private ThirdApiClient thirdApiClient;

    @Autowired
    private ProductDomain productDomain;

    @Autowired
    private OrgBrandDomain orgBrandDomain;

    public Map<String, Object> getConfig(String orgId, String url) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("url", url)
                .build();
        return thirdApiClient.get("wechat/jssdk/config" + query, new ParameterizedTypeReference<Map<String, Object>>() {
        });
    }

    public boolean sendRedPack(WeChatRedPackRequest redPackRequest) {
        return thirdApiClient.post("wechat/redpack", redPackRequest, boolean.class);
    }

    public String createQRCode(String key, String orgId){
        Integer scenarioId = Integer.parseInt(RandomUtils.generateString(8, RandomUtils.NUMERIC_CHARS ));
        productDomain.getKeyFromRadis(scenarioId, key);
        return thirdApiClient.post("wechat/qrcode/{id}?org_id={orgId}", null, String.class, scenarioId, orgId);
    }

    public WeChatUser getWeChatUser(String openId, String orgId){
        return thirdApiClient.get("wechat/user/{id}?org_id={orgId}", WeChatUser.class, openId, orgId);
    }

    @Cacheable(key="T(com.yunsoo.api.rabbit.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).WECHAT.toString(), #orgId)")
    public WeChatServerConfig getOrgIdHasWeChatSettings(String orgId){
        WeChatServerConfig config = thirdApiClient.get("wechat/server/config/{id}", WeChatServerConfig.class, orgId);
        if(StringUtils.hasText(config.getAppId()))
            return  config;

        OrgBrandObject object = orgBrandDomain.getOrgBrandById(orgId);
        config = thirdApiClient.get("wechat/server/config/{id}", WeChatServerConfig.class, object.getCarrierId());
        if(StringUtils.hasText(config.getAppId()))
            return config;

        config = thirdApiClient.get("wechat/server/config/{id}", WeChatServerConfig.class, Constants.Ids.YUNSU_ORG_ID);
        if(StringUtils.hasText(config.getAppId()))
            return config;

        return null;
    }

    public WeChatUser getWebUser(String code, Boolean detailsFlag, String orgId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("code", code).append("org_id", orgId)
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
}
