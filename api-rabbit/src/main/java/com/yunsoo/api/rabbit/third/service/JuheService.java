package com.yunsoo.api.rabbit.third.service;

import com.yunsoo.api.rabbit.client.ThirdApiClient;
import com.yunsoo.api.rabbit.third.dto.JuheMobileLocation;
import com.yunsoo.api.rabbit.third.dto.JuheOrder;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yan on 12/7/2016.
 */
@Service
public class JuheService {

    @Autowired
    private ThirdApiClient thirdApiClient;

    public JuheOrder saveMobileData(String mobile, String orderId, Integer dataFlowId){
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("data_flow_id", dataFlowId)
                .append("order_id", orderId)
                .build();
        return thirdApiClient.post("juhe/{mobile}/mobile_data" + query, null, JuheOrder.class, mobile);
    }

    public JuheOrder saveMobileFee(String mobile, String orderId,  Integer amount){
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("amount", amount)
                .append("order_id", orderId)
                .build();
        return  thirdApiClient.post("juhe/{mobile}/mobile_fee" + query, null, JuheOrder.class, mobile);

    }

    public JuheMobileLocation getMobileLocation(String mobile){
        return  thirdApiClient.get("juhe/{mobile}/location", JuheMobileLocation.class, mobile);

    }

    public boolean sendVerificationCode(String mobile, String templateName) {
        return thirdApiClient.post("juhe/{mobile}/sms_send?temp_name={name}", null, Boolean.class, mobile, templateName);
    }

    public boolean validateVerificationCode(String mobile, String verificationCode) {
        return thirdApiClient.post("juhe/{mobile}/sms_verify?verification_code={code}", null, Boolean.class, mobile, verificationCode);
    }
}
