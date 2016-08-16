package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.client.WeChatApiClient;
import com.yunsoo.api.rabbit.dto.AccessToken;
import com.yunsoo.api.rabbit.dto.JsApi_Ticket;
import com.yunsoo.common.data.object.UserAccessTokenObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Admin on 6/29/2016.
 */
@Component
public class UserAccessTokenDomain {

    @Autowired
    private RestClient dataApiClient;

    @Autowired
    private WeChatApiClient weChatApiClient;

    public UserAccessTokenObject getUserAccessTokenObject(String orgId, String appId, String secret) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .build();

        UserAccessTokenObject userAccessTokenObject = dataApiClient.get("userAccessToken" + query, UserAccessTokenObject.class);
        if (userAccessTokenObject == null || userAccessTokenObject.getExpiredDatetime() == null || userAccessTokenObject.getExpiredDatetime().isBeforeNow()) {
            AccessToken accessToken = weChatApiClient.get("token?grant_type=client_credential&appid=" + appId + "&secret=" + secret, AccessToken.class);
            if (accessToken != null && accessToken.getAccessToken() != null) {
                JsApi_Ticket jsApi_ticket = weChatApiClient.get("ticket/getticket?access_token=" + accessToken.getAccessToken() + "&type=jsapi", JsApi_Ticket.class);
                if (jsApi_ticket != null && jsApi_ticket.getTicket() != null) {
                    UserAccessTokenObject newUserATObj = new UserAccessTokenObject();
                    newUserATObj.setId(null);
                    newUserATObj.setCreatedDateTime(DateTime.now());
                    newUserATObj.setAccessToken(accessToken.getAccessToken());
                    newUserATObj.setJsapiTicket(jsApi_ticket.getTicket());
                    newUserATObj.setOrgId(orgId);
                    newUserATObj.setExpiredDatetime(DateTime.now().plusSeconds(jsApi_ticket.getExpiresIn().intValue() - 200));

                    return dataApiClient.post("userAccessToken", newUserATObj, UserAccessTokenObject.class);
                }
            }
        } else {
            return userAccessTokenObject;
        }

        return null;
    }
}
