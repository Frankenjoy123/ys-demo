package com.yunsoo.api.domain;

import com.yunsoo.api.payment.AlipayParameters;
import com.yunsoo.api.payment.ParameterNames;
import com.yunsoo.common.data.object.BrandApplicationObject;
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
    private RestClient dataApiClient;

    @Autowired
    private BrandApplicationDomain brandApplicationDomain;

    @Value("${yunsoo.alipay.pid}")
    private String pid;

    @Value("${yunsoo.alipay.key}")
    private String key;

    @Value("${yunsoo.alipay.account_name}")
    private String alipayAccountName;

    @Value("${yunsoo.alipay.email}")
    private String alipayEmail;

    @Value("${yunsoo.alipay.amount}")
    private String alipayAmount;

    @Value("${yunsoo.alipay.direct_return_url}")
    private String alipayReturnUrl;

    @Value("${yunsoo.alipay.direct_notify_url}")
    private String alipayNotifyUrl;


    public PaymentObject createAlipayPayment(PaymentObject paymentObject) {
        paymentObject.setId(null);
        paymentObject.setCreatedDateTime(DateTime.now());
        return dataApiClient.post("payment/brand/alipay", paymentObject, PaymentObject.class);
    }

    public void updateAlipayPayment(PaymentObject paymentObject) {
        dataApiClient.put("payment/brand/alipay/{id}", paymentObject, paymentObject.getId());
    }


    public PaymentObject getPaymentById(String paymentId) {
        try {
            return dataApiClient.get("payment/brand/{id}", PaymentObject.class, paymentId);
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
        PaymentObject paymentObject = dataApiClient.get("payment/brand/{id}", PaymentObject.class, paymentId);
        String brandApplicationId = paymentObject.getBrandApplicationId();

        BrandApplicationObject brandApplicationObject = brandApplicationDomain.getBrandApplicationById(brandApplicationId);

        //order info
        parameters.put(ParameterNames.OUT_TRADE_NO, paymentId);
        parameters.put(ParameterNames.SUBJECT, brandApplicationObject.getBrandName() + "审核费用");
        parameters.put(ParameterNames.PAYMENT_TYPE, "1");
        parameters.put(ParameterNames.TOTAL_FEE, paymentObject.getPayTotals().toString());
        parameters.put(ParameterNames.SELLER_ID, pid);
        parameters.put(ParameterNames.RETURN_URL, alipayReturnUrl);
        parameters.put(ParameterNames.NOTIFY_URL, alipayNotifyUrl);

        return parameters.toMap();
    }

    public BigDecimal getAlipayAmount() {
        BigDecimal amount = new BigDecimal(alipayAmount);
        return amount;
    }

}

