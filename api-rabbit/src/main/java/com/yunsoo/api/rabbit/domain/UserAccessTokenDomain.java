package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.client.WXAPIClient;
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
    private RestClient dataAPIClient;

    @Autowired
    private WXAPIClient wxapiClient;

    public UserAccessTokenObject getUserAccessTokenObject(String orgId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .build();

        UserAccessTokenObject userAccessTokenObject = dataAPIClient.get("userAccessToken" + query, UserAccessTokenObject.class);
        if (userAccessTokenObject == null || userAccessTokenObject.getExpiredDatetime() == null || userAccessTokenObject.getExpiredDatetime().isBeforeNow()) {
            AccessToken accessToken = wxapiClient.get("token?grant_type=client_credential&appid=wx89c1685a0c14e8bf&secret=c1e1d31fac7e0e31a64417ecef3b3682", AccessToken.class);
            if (accessToken != null && accessToken.getAccessToken() != null) {
                JsApi_Ticket jsApi_ticket = wxapiClient.get("ticket/getticket?access_token=" + accessToken.getAccessToken() + "&type=jsapi", JsApi_Ticket.class);
                if (jsApi_ticket != null && jsApi_ticket.getTicket() != null) {
                    UserAccessTokenObject newUserATObj = new UserAccessTokenObject();
                    newUserATObj.setId(null);
                    newUserATObj.setCreatedDateTime(DateTime.now());
                    newUserATObj.setAccessToken(accessToken.getAccessToken());
                    newUserATObj.setJsapiTicket(jsApi_ticket.getTicket());
                    newUserATObj.setOrgId(orgId);
                    newUserATObj.setExpiredDatetime(DateTime.now().plusSeconds(jsApi_ticket.getExpiresIn().intValue() - 200));

                    return dataAPIClient.post("userAccessToken", newUserATObj, UserAccessTokenObject.class);
                }
            }
        } else {
            return userAccessTokenObject;
        }

        return null;
    }
}
