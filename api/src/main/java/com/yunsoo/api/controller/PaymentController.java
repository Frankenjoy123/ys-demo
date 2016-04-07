package com.yunsoo.api.controller;

import com.yunsoo.api.domain.BrandDomain;
import com.yunsoo.api.domain.PaymentDomain;
import com.yunsoo.api.dto.Payment;
import com.yunsoo.api.payment.AlipayNotify;
import com.yunsoo.api.payment.ParameterNames;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.PaymentObject;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by  : haitao
 * Created on  : 2016/4/5
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/payment")
public class PaymentController {

    @Autowired
    private PaymentDomain paymentDomain;

    @Autowired
    private BrandDomain brandDomain;


    //create brand payment record
    @RequestMapping(value = "/brand/alipay/{id}", method = RequestMethod.GET)
    public Map<String, String> getAlipaySubmitParametersByBrandApplicationId(@PathVariable String id) {
        PaymentObject paymentObject = new PaymentObject();
        paymentObject.setBrandApplicationId(id);
        paymentObject.setStatusCode(LookupCodes.PaymentStatus.CREATED);
        paymentObject.setCreatedDateTime(DateTime.now());
        paymentObject.setPayTotals(paymentDomain.getAlipayAmount());
        paymentObject.setTradeNo(id);
        paymentObject.setTypeCode(LookupCodes.PaymentType.ALIPAY);
        PaymentObject pObject = paymentDomain.createAlipayPayment(paymentObject);
        String paymentId = pObject.getId();
        Map<String, String> parametersMap = paymentDomain.getAlipaySubmitParametersByPaymentId(paymentId);
        return parametersMap;
    }

    @RequestMapping(value = "/brand/alipay/return", method = RequestMethod.GET)
    public Payment returnFromAlipay(@RequestParam("is_success") String isSuccess,
                                    @RequestParam(ParameterNames.OUT_TRADE_NO) String paymentId,
                                    HttpServletRequest request) {
        if (paymentId == null) {
            return null;
        }
        PaymentObject paymentObject = paymentDomain.getPaymentById(paymentId);

        if (isSuccess.equals("T")) {
            Map<String, String> parametersMap = AlipayNotify.getRequestParam(request.getParameterMap());

            String tradeNo = request.getParameter("trade_no");
            String tradeStatus = request.getParameter("trade_status");
            if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS")) {
                paymentObject.setPaidDateTime(DateTime.now());
                paymentObject.setStatusCode(LookupCodes.PaymentStatus.PAID);
                paymentDomain.updateAlipayPayment(paymentObject);
            } else {
                paymentObject.setStatusCode(LookupCodes.PaymentStatus.FAILED);
                paymentDomain.updateAlipayPayment(paymentObject);
            }
        }
        return new Payment(paymentObject);
    }

    @RequestMapping(value = "/brand/alipay/notify", method = RequestMethod.GET)
    public void notifyFromAlipay(@RequestParam(value = ParameterNames.OUT_TRADE_NO, required = false) String paymentId,
                                 HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (paymentId == null) {
            return;
        }
        PaymentObject paymentObject = paymentDomain.getPaymentById(paymentId);

        String tradeNo = request.getParameter("trade_no");
        String tradeStatus = request.getParameter("trade_status");

        if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS")) {
            paymentObject.setPaidDateTime(DateTime.now());
            paymentObject.setStatusCode(LookupCodes.PaymentStatus.PAID);
            paymentDomain.updateAlipayPayment(paymentObject);
        } else {
            paymentObject.setStatusCode(LookupCodes.PaymentStatus.FAILED);
            paymentDomain.updateAlipayPayment(paymentObject);
        }
        response.getWriter().print("success");
    }

}
