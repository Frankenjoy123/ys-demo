package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.MarketingDomain;
import com.yunsoo.api.rabbit.domain.ProductDomain;
import com.yunsoo.api.rabbit.dto.*;
import com.yunsoo.api.rabbit.security.TokenAuthenticationService;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.*;
import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.exception.ErrorResultException;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.RestErrorResultException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Haitao
 * Created on  : 2016/1/25
 * Descriptions:
 */

@RestController
@RequestMapping(value = "/marketing")
public class MarketingController {

    @Autowired
    private MarketingDomain marketingDomain;

    @Autowired
    private ProductDomain productDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "draw/{key}", method = RequestMethod.GET)
    public MktDrawRecord getMktDrawRecordByProductKey(@PathVariable String key) {
        if (key == null) {
            throw new BadRequestException("product key can not be null");
        }
        MktDrawRecordObject mktDrawRecordObject = marketingDomain.getMktDrawRecordByProductKey(key);
        if (mktDrawRecordObject != null) {
            return new MktDrawRecord(mktDrawRecordObject);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "drawPrize/{key}", method = RequestMethod.GET)
    public MktDrawPrize getMktDrawPrizeByProductKey(@PathVariable String key) {
        if (key == null) {
            throw new BadRequestException("product key can not be null");
        }
        MktDrawPrizeObject mktDrawPrizeObject = marketingDomain.getMktDrawPrizeByProductKey(key);
        if (mktDrawPrizeObject != null) {
            MktDrawPrize prize = new MktDrawPrize(mktDrawPrizeObject);
            MktDrawRuleObject rule = marketingDomain.getDrawRuleById(prize.getDrawRuleId());
            if(StringUtils.hasText(rule.getConsumerRightId())){
                MktConsumerRightObject right = marketingDomain.getConsumerRightById(rule.getConsumerRightId());
                prize.setMktConsumerRight(new MktConsumerRight(right));
            }
            return prize;
        } else {
            return null;
        }
    }

    @RequestMapping(value = "draw", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MktDrawRecord createMktDrawRecord(@RequestBody MktDrawRecord mktDrawRecord) {
        if (mktDrawRecord == null) {
            throw new BadRequestException("marketing draw record can not be null");
        }
        String productKey = mktDrawRecord.getProductKey();
        ProductObject product = productDomain.getProduct(productKey);
        if (product == null) {
            throw new NotFoundException("product can not be found by the key");
        }
        MktDrawRecordObject mktDrawRecordObject = mktDrawRecord.toMktDrawRecordObject();
        // String currentUserId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        //  mktDrawRecordObject.setUserId(currentUserId);

        MktDrawRecordObject newMktDrawRecordObject = marketingDomain.createMktDrawRecord(mktDrawRecordObject);
        return new MktDrawRecord(newMktDrawRecordObject);
    }

    @RequestMapping(value = "drawPrize", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MktDrawPrize createMktDrawPrize(@RequestBody MktDrawPrize mktDrawPrize) {
        if (mktDrawPrize == null) {
            throw new BadRequestException("marketing draw record can not be null");
        }
        String productKey = mktDrawPrize.getProductKey();
        ProductObject product = productDomain.getProduct(productKey);
        if (product == null) {
            throw new NotFoundException("product can not be found by the key");
        }
        MktDrawPrizeObject mktDrawPrizeObject = mktDrawPrize.toMktDrawPrizeObject();

        MktDrawPrizeObject newMktDrawPrizeObject = marketingDomain.createMktDrawPrize(mktDrawPrizeObject);
        return new MktDrawPrize(newMktDrawPrizeObject);
    }

    @RequestMapping(value = "drawPrize", method = RequestMethod.PUT)
    public void updateMktDrawPrize(@RequestBody MktDrawPrize mktDrawPrize) {
        if (mktDrawPrize == null) {
            throw new BadRequestException("marketing draw record can not be null");
        }
        boolean result = true;

        MktDrawPrizeObject currentPrize = marketingDomain.getMktDrawPrizeByProductKey(mktDrawPrize.getProductKey());
        if(currentPrize.getStatusCode().equals(LookupCodes.MktDrawPrizeStatus.CREATED) ||
                currentPrize.getStatusCode().equals(LookupCodes.MktDrawPrizeStatus.SUBMIT) ) {
            MktDrawPrizeObject mktDrawPrizeObject = mktDrawPrize.toMktDrawPrizeObject();
            mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.SUBMIT);
            if (LookupCodes.MktPrizeType.MOBILE_FEE.equals(mktDrawPrize.getPrizeTypeCode())) {
                boolean isMobileFeeSuccess = marketingDomain.createMobileOrder(mktDrawPrize.getDrawRecordId());
                if (!isMobileFeeSuccess) {
                    mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.FAILED);
                    result = false;
                } else {
                    mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.PAID);
                    mktDrawPrizeObject.setPaidDateTime(DateTime.now());
                }
            } else if (LookupCodes.MktPrizeType.MOBILE_DATA.equals(mktDrawPrize.getPrizeTypeCode())) {
                boolean isMobileDataSuccess = marketingDomain.createMobileDataFlow(mktDrawPrize.getDrawRecordId());
                if (!isMobileDataSuccess) {
                    mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.FAILED);
                    result = false;
                } else {
                    mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.PAID);
                    mktDrawPrizeObject.setPaidDateTime(DateTime.now());
                }
            }
            marketingDomain.updateMktDrawPrize(mktDrawPrizeObject);
        }
        else
            throw new RestErrorResultException(new ErrorResult(5002, "prize had been sent"));


        if(!result)
            throw new RestErrorResultException(new ErrorResult(5003, "send prize failed"));
    }

    @RequestMapping(value = "drawPrize/invalid", method = RequestMethod.PUT)
    public void updateFailedMktDrawPrize(@RequestBody MktDrawPrize mktDrawPrize) {
        if (mktDrawPrize == null) {
            throw new BadRequestException("marketing draw record can not be null");
        }
        MktDrawPrizeObject mktDrawPrizeObject = mktDrawPrize.toMktDrawPrizeObject();
        mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.INVALID);
        marketingDomain.updateMktDrawPrize(mktDrawPrizeObject);

    }


    @RequestMapping(value = "drawPrize/{key}/sms", method = RequestMethod.PUT)
    public boolean sendPrizeSMS(@PathVariable(value = "key") String productKey,
                                @RequestParam(value = "mobile") String mobile) {

        MktDrawPrizeObject mktDrawPrizeObject = marketingDomain.getMktDrawPrizeByProductKey(productKey);
        boolean isSMSSent = marketingDomain.sendVerificationCode(mobile, LookupCodes.SMSTemplate.SENDPRIZE);
        if (isSMSSent) {
            mktDrawPrizeObject.setMobile(mobile);
            marketingDomain.updateMktDrawPrize(mktDrawPrizeObject);
        }
        return isSMSSent;
    }

    @RequestMapping(value = "drawPrize/{key}/smsverfiy", method = RequestMethod.PUT)
    public boolean validatePrizeVerificationCode(@PathVariable(value = "key") String productKey,
                                                 @RequestParam(value = "mobile") String mobile,
                                                 @RequestParam(value = "verification_code") String verificationCode) {

        MktDrawPrizeObject mktDrawPrizeObject = marketingDomain.getMktDrawPrizeByProductKey(productKey);
        boolean isVerified = marketingDomain.validateVerificationCode(mobile, verificationCode);
        if (isVerified) {
            mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.SUBMIT);
            marketingDomain.updateMktDrawPrize(mktDrawPrizeObject);
        }
        return isVerified;
    }





    @RequestMapping(value = "drawPrize/paid", method = RequestMethod.PUT)
    public void updateMktDrawPrizeAfterPaid(@RequestBody MktDrawPrize mktDrawPrize) {
        if (mktDrawPrize == null) {
            throw new BadRequestException("marketing draw record can not be null");
        }
        MktDrawPrizeObject mktDrawPrizeObject = mktDrawPrize.toMktDrawPrizeObject();
        mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.PAID);
        mktDrawPrizeObject.setPaidDateTime(DateTime.now());
        marketingDomain.updateMktDrawPrize(mktDrawPrizeObject);

    }

    @RequestMapping(value = "drawPrize/{id}/random", method = RequestMethod.GET)
    public MktDrawRule getRandomPrizeAmount(@PathVariable(value = "id") String marketingId) {
        if (marketingId == null)
            throw new BadRequestException("marketing id can not be null");

        MktDrawRuleObject mktDrawRuleObject = marketingDomain.getMktRandomPrize(marketingId);
        if (mktDrawRuleObject != null) {
            MktDrawRule mktDrawRule = new MktDrawRule(mktDrawRuleObject);
            String consumerRightId = mktDrawRuleObject.getConsumerRightId();
            if (consumerRightId != null) {
                MktConsumerRightObject consumerRightObject = marketingDomain.getConsumerRightById(consumerRightId);
                if (consumerRightObject != null) {
                    mktDrawRule.setMktConsumerRight(new MktConsumerRight(consumerRightObject));
                }
            }
            return mktDrawRule;

        } else {
            return new MktDrawRule(mktDrawRuleObject);
        }
//        return new MktDrawRule(marketingDomain.getMktRandomPrize(marketingId));
    }

    @RequestMapping(value = "drawRule/{id}", method = RequestMethod.GET)
    public List<MktDrawRule> getMarketingRuleList(@PathVariable(value = "id") String marketingId) {
        if (marketingId == null)
            throw new BadRequestException("marketing id can not be null");

        return marketingDomain.getRuleList(marketingId).stream().map(MktDrawRule::new).collect(Collectors.toList());
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Marketing getMarketing(@PathVariable(value = "id") String marketingId) {
        MarketingObject marketingObject = marketingDomain.getMarketingById(marketingId);
        if (marketingObject == null) {
            throw new NotFoundException("marketing not found");
        }
        return new Marketing(marketingObject);
    }

}
