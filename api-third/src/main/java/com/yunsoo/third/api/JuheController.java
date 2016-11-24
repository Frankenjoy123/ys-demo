package com.yunsoo.third.api;

import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnprocessableEntityException;
import com.yunsoo.third.dao.entity.ThirdSmsTemplateEntity;
import com.yunsoo.third.dao.repository.SMSTemplateRepository;
import com.yunsoo.third.dto.juhe.JuheIPResult;
import com.yunsoo.third.dto.juhe.JuheSMSResult;
import com.yunsoo.third.service.JuheService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.logging.Logger;

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
    public JuheIPResult getLocationByIp(@RequestParam("ip") String ip){
        JuheIPResult result = juheService.getIP(ip);
        if(result.getErrorCode() == 0)
            return  result;
        else{
            log.error("get ip error. reason: " + result.getReason()  + ". ip: " + ip);
            return null;
        }
    }

    @RequestMapping(value = "/sms", method = RequestMethod.POST)
    public boolean sendSMS(@RequestParam("mobile") String mobile, @RequestParam("temp_name") String tempName, @RequestParam(value = "variables", required = false) String... variables) {

        ThirdSmsTemplateEntity templateEntity = juheService.getSMSTemplate(tempName);
        if (templateEntity == null)
            throw new NotFoundException("the related template not found");
        String[] keys = templateEntity.getVariable().split(",");
        if (keys.length != variables.length)
            throw new BadRequestException("error parameter variables");

        HashMap<String, String> map = new HashMap<>();
        int index = 0;
        for (String key : keys) {
            map.put(key, variables[index]);
            index++;
        }
        try {
            JuheSMSResult result = juheService.sendSMS(mobile, String.valueOf(templateEntity.getSupplierId()), map);
            if (result.getErrorCode() == 0) {
                return true;
            }
            else{
                log.error("send sms message error. reason: " + result.getReason()  + "mobile: " + mobile + ", temp_name: " + tempName + ", variables: " + variables.toString());
                return false;
            }

        } catch (UnsupportedEncodingException e) {
            throw new UnprocessableEntityException("encode paramenter error for sms sending in juhe");
        }
    }

    @RequestMapping(value = "/sms/verify", method = RequestMethod.GET)
    public boolean VerifyCode(@RequestParam("mobile") String mobile, @RequestParam("verification_code") String verificationCode) {
        if ((mobile == null) || (verificationCode == null)) {
            throw new BadRequestException("mobile phone or verification code can not be null");
        }
        return juheService.verifySMSCode(mobile, verificationCode);
    }

}
