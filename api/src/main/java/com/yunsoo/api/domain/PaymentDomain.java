package com.yunsoo.api.domain;

import com.yunsoo.api.payment.AlipayParameters;
import com.yunsoo.api.payment.ParameterNames;
import com.yunsoo.common.data.object.BrandObject;
import com.yunsoo.common.data.object.PaymentObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by  : Haitao
 * Created on  : 2016/4/5
 * Descriptions:
 */
@Component
public class PaymentDomain {

    @Autowired
    private RestClient dataAPIClient;

    @Value("${yunsoo.alipay.pid}")
    private String pid;

    @Value("${yunsoo.alipay.key}")
    private String key;

    @Value("${yunsoo.alipay.account_name}")
    private String alipayAccountName;

    @Value("${yunsoo.alipay.email}")
    private String alipayEmail;

    @Value("${yunsoo.alipay.direct_return_url}")
    private String alipayReturnUrl;
    @Value("${yunsoo.alipay.direct_notify_url}")
    private String alipayNotifyUrl;


    public PaymentObject createAlipayPayment(PaymentObject paymentObject) {
        paymentObject.setId(null);
        paymentObject.setCreatedDateTime(DateTime.now());
        return dataAPIClient.post("payment/brand/alipay", paymentObject, PaymentObject.class);
    }

    public void updateAlipayPayment(PaymentObject paymentObject) {
        dataAPIClient.put("payment/brand/alipay/{id}", paymentObject, paymentObject.getId());
    }


    public PaymentObject getPaymentById(String paymentId) {
        try {
            return dataAPIClient.get("payment/brand/{id}", PaymentObject.class, paymentId);
        } catch (NotFoundException ignored) {
            return null;
        }
    }


    public Map<String, String> getAlipaySubmitParametersByPaymentId(String paymentId) {

        AlipayParameters parameters = new AlipayParameters("create_direct_pay_by_user", pid, key);

        //order info
        String detail_data = "";
        Integer batchNum = 0;
        BigDecimal batchFee = new BigDecimal("0");
        PaymentObject paymentObject = dataAPIClient.get("payment/brand/{id}", PaymentObject.class, paymentId);
        String brandApplicationId = paymentObject.getBrandApplicationId();

        BrandObject brandObject = dataAPIClient.get("brand/{id}", BrandObject.class, brandApplicationId);

        //order info
        parameters.put(ParameterNames.OUT_TRADE_NO, paymentId);
        parameters.put(ParameterNames.SUBJECT, brandObject.getName() + "审核费用");
        parameters.put(ParameterNames.PAYMENT_TYPE, "1");
        parameters.put(ParameterNames.TOTAL_FEE, paymentObject.getPayTotals().toString());
        parameters.put(ParameterNames.SELLER_ID, pid);
        parameters.put(ParameterNames.RETURN_URL, alipayReturnUrl);
        parameters.put(ParameterNames.NOTIFY_URL, alipayNotifyUrl);

        return parameters.toMap();
    }

}

