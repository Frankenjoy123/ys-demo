package com.yunsoo.third.api;

import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.third.dto.juhe.*;
import com.yunsoo.third.service.JuheService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by yan on 11/2/2016.
 */
@RestController
@RequestMapping("juhe")
public class JuheController {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private JuheService juheService;

    @RequestMapping(value = "/ip", method = RequestMethod.GET)
    public JuheIPResult.IPObject getLocationByIp(@RequestParam("ip") String ip){
        JuheIPResult result = juheService.getIP(ip);
        if(result.getErrorCode() == 0)
            return  result.getResult();
        else{
            log.error("get ip error. reason: " + result.getReason()  + ". ip: " + ip);
            return null;
        }
    }

    @RequestMapping(value = "/{mobile}/sms_send", method = RequestMethod.POST)
    public boolean sendVerificationCode(@PathVariable("mobile") String mobile, @RequestParam("temp_name") String tempName){
        String ver_code = String.valueOf((int)((Math.random() * 9 + 1) * 100000));
        return juheService.sendVerificationCode(mobile, tempName, "juhe", ver_code);
    }


    @RequestMapping(value = "/{mobile}/sms_verify", method = RequestMethod.POST)
    public boolean VerifyCode(@PathVariable("mobile") String mobile, @RequestParam("verification_code") String verificationCode) {
        if ((mobile == null) || (verificationCode == null)) {
            throw new BadRequestException("mobile phone or verification code can not be null");
        }
        return juheService.verifySMSCode(mobile, verificationCode);
    }

    @RequestMapping(value = "/{mobile}/location", method = RequestMethod.GET)
    public JuheMobileLocationResult.LocationResultObject getMobileLocation(@PathVariable("mobile") String mobile){
        return juheService.getMobileLocation(mobile);
    }

    //充值
    @RequestMapping(value = "/{mobile}/mobile_fee", method = RequestMethod.POST)
    public JuheMobileOrderResult.OrderResultObject mobileOrder (@PathVariable("mobile") String mobile,
                                                                @RequestParam("amount") int amount,
                                                                @RequestParam("order_id") String id){
        return juheService.mobileOrder(mobile, amount, id);
    }

    @RequestMapping(value = "/{mobile}/mobile_data", method = RequestMethod.POST)
    public JuheMobileDataResult.DataResultObject mobileDataFlow (@PathVariable("mobile") String mobile,
                                                                 @RequestParam("data_flow_id") Integer dataFlowId,
                                                                 @RequestParam("order_id") String id){

        return juheService.mobileDataFlow(mobile, dataFlowId, id);

    }





}
