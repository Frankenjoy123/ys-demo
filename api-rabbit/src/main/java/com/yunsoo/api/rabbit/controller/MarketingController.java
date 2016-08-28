package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.MarketingDomain;
import com.yunsoo.api.rabbit.domain.ProductBaseDomain;
import com.yunsoo.api.rabbit.domain.ProductDomain;
import com.yunsoo.api.rabbit.dto.*;
import com.yunsoo.api.rabbit.security.TokenAuthenticationService;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.*;
import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.util.KeyGenerator;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ConflictException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.RestErrorResultException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    private ProductBaseDomain productBaseDomain;


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
            if (StringUtils.hasText(rule.getConsumerRightId())) {
                MktConsumerRightObject right = marketingDomain.getConsumerRightById(rule.getConsumerRightId());
                prize.setMktConsumerRight(new MktConsumerRight(right));
            }
            return prize;
        } else {
            return null;
        }
    }

    @RequestMapping(value = "drawPrize/contact/{id}", method = RequestMethod.GET)
    public MktPrizeContact getMktPrizeContactById(@PathVariable String id) {
        MktPrizeContactObject mktPrizeContactObject = marketingDomain.getMktPrizeContactById(id);

        if (mktPrizeContactObject != null) {
            return new MktPrizeContact(mktPrizeContactObject);
        } else {
            return null;
        }
    }


    //本接口应该遵循幂等原则，使用PUT方法
    @RequestMapping(value = "drawPrize/{id}/contact", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MktPrizeContact createMktPrizeContact(@PathVariable(value = "id") String prizeId, @RequestBody MktPrizeContact mktPrizeContact) {
        if (mktPrizeContact == null) {
            throw new BadRequestException("marketing prize contact can not be null");
        }
        mktPrizeContact.setMktPrizeId(prizeId);

        MktDrawPrizeObject mktDrawPrizeObject = marketingDomain.getMktDrawPrizeByPrizeId(prizeId);
        if (mktDrawPrizeObject == null) {
            throw new NotFoundException("marketing draw prize can not be found");
        }

        MktPrizeContactObject mktPrizeContactObject = mktPrizeContact.toMktPrizeContactObject();
        //todo 需要修改
        //把prizeid作为凭证号，一个prizeid对应的领奖人信息，只能被创建一次，假定每次put的contact信息相同，N次创建和一次创建的结果应该相同
        MktPrizeContactObject newObject = marketingDomain.createMktPrizeContact(mktPrizeContactObject);

        mktDrawPrizeObject.setPrizeContactId(newObject.getId());
        mktDrawPrizeObject.setPrizeAccountName(mktPrizeContact.getName());
        mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.SUBMIT);
        mktDrawPrizeObject.setPrizeAccount(mktPrizeContact.getPhone());
        mktDrawPrizeObject.setMobile(mktPrizeContact.getPhone());
        marketingDomain.updateMktDrawPrize(mktDrawPrizeObject);



        return new MktPrizeContact(newObject);
    }

    //本接口遵循幂等原则
    @RequestMapping(value = "drawPrize/{id}/contact", method = RequestMethod.PUT)
    public void updateMktPrizeContact(@PathVariable(value = "id") String prizeId, @RequestBody MktPrizeContact mktPrizeContact) {
        if (mktPrizeContact == null) {
            throw new BadRequestException("marketing prize contact can not be null");
        }
        mktPrizeContact.setMktPrizeId(prizeId);

        MktDrawPrizeObject mktDrawPrizeObject = marketingDomain.getMktDrawPrizeByPrizeId(prizeId);
        if ((mktDrawPrizeObject == null) || (mktDrawPrizeObject.getPrizeContactId() == null) || (mktDrawPrizeObject.getPrizeContactId().equals(""))) {
            throw new BadRequestException("Invalid operation: update marketing prize contact error");
        }

        MktPrizeContactObject mktPrizeContactObject = mktPrizeContact.toMktPrizeContactObject();
        marketingDomain.updateMktPrizeContact(mktPrizeContactObject);
    }


    @RequestMapping(value = "drawPrize", method = RequestMethod.PUT)
    public void updateMktDrawPrize(@RequestBody MktDrawPrize mktDrawPrize) {
        if (mktDrawPrize == null) {
            throw new BadRequestException("marketing draw record can not be null");
        }
        boolean result = true;

        MktDrawPrizeObject currentPrize = marketingDomain.getMktDrawPrizeByProductKey(mktDrawPrize.getProductKey());
        if (currentPrize != null && (LookupCodes.MktDrawPrizeStatus.CREATED.equals(currentPrize.getStatusCode()))) {
            MktDrawPrizeObject mktDrawPrizeObject = mktDrawPrize.toMktDrawPrizeObject();
            mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.SUBMIT);
            mktDrawPrizeObject.setDrawRecordId(currentPrize.getDrawRecordId());
            if (LookupCodes.MktPrizeType.MOBILE_FEE.equals(currentPrize.getPrizeTypeCode())) {
                marketingDomain.updateMktDrawPrize(mktDrawPrizeObject);
                boolean isMobileFeeSuccess = marketingDomain.createMobileOrder(mktDrawPrize.getDrawRecordId());
                if (!isMobileFeeSuccess) {
                    mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.FAILED);
                    result = false;
                } else {
                    mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.PAID);
                    mktDrawPrizeObject.setPaidDateTime(DateTime.now());
                }
            } else if (LookupCodes.MktPrizeType.MOBILE_DATA.equals(currentPrize.getPrizeTypeCode())) {
                marketingDomain.updateMktDrawPrize(mktDrawPrizeObject);
                boolean isMobileDataSuccess = marketingDomain.createMobileDataFlow(mktDrawPrize.getDrawRecordId());
                if (!isMobileDataSuccess) {
                    mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.FAILED);
                    result = false;
                } else {
                    mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.PAID);
                    mktDrawPrizeObject.setPaidDateTime(DateTime.now());
                }
            } else if (LookupCodes.MktPrizeType.COUPON.equals(currentPrize.getPrizeTypeCode())) {
                mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.PAID);
                mktDrawPrizeObject.setPaidDateTime(DateTime.now());

            }
            marketingDomain.updateMktDrawPrize(mktDrawPrizeObject);


            MktDrawRecordObject record = marketingDomain.getMktDrawRecordByProductKey(mktDrawPrize.getProductKey());
            record.setYsid(mktDrawPrize.getYsid());
            marketingDomain.updateMktDrawRecord(record);

        } else
            throw new RestErrorResultException(new ErrorResult(5002, "prize had been sent"));


        if (!result)
            throw new RestErrorResultException(new ErrorResult(5003, "send prize failed"));
    }

    @RequestMapping(value = "drawPrize/invalid", method = RequestMethod.PUT)
    public void updateFailedMktDrawPrize(@RequestBody MktDrawPrize mktDrawPrize) {
        if (mktDrawPrize == null) {
            throw new BadRequestException("marketing draw record can not be null");
        }
        MktDrawPrizeObject currentPrize = marketingDomain.getMktDrawPrizeByProductKey(mktDrawPrize.getProductKey());
        if (currentPrize != null && (LookupCodes.MktDrawPrizeStatus.CREATED.equals(currentPrize.getStatusCode()) ||
                LookupCodes.MktDrawPrizeStatus.SUBMIT.equals(currentPrize.getStatusCode()))) {
            MktDrawPrizeObject mktDrawPrizeObject = mktDrawPrize.toMktDrawPrizeObject();
            mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.INVALID);
            mktDrawPrizeObject.setDrawRecordId(currentPrize.getDrawRecordId());
            marketingDomain.updateMktDrawPrize(mktDrawPrizeObject);
        }

    }

    @RequestMapping(value = "consumer/redeemcode/{key}", method = RequestMethod.GET)
    public MktConsumerRightRedeemCode getConsumerRedeemCodeByIdAndPrizeId(@PathVariable String key) {
        MktConsumerRightRedeemCodeObject mktConsumerRightRedeemCodeObject = marketingDomain.getMktConsumerRightRedeemCodeByProductKey(key);

        if (mktConsumerRightRedeemCodeObject != null) {
            return new MktConsumerRightRedeemCode(mktConsumerRightRedeemCodeObject);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "consumer/redeemcode/generate/{key}", method = RequestMethod.GET)
    public MktConsumerRightRedeemCode getRandomConsumerRedeemCodeByIdAndPrizeId(@PathVariable String key,
                                                                                @RequestParam(value = "draw_rule_id") String drawRuleId) {
        MktConsumerRightRedeemCodeObject mktConsumerRightRedeemCodeObject = marketingDomain.getRandomMktConsumerRightRedeemCodeByProductKey(key, drawRuleId);

        if (mktConsumerRightRedeemCodeObject != null) {
            return new MktConsumerRightRedeemCode(mktConsumerRightRedeemCodeObject);
        } else {
            return null;
        }
    }


    @RequestMapping(value = "consumer/key/{key}", method = RequestMethod.GET)
    public MktConsumerRight getMktConsumerRightByProductKey(@PathVariable String key) {
        if (key == null) {
            throw new BadRequestException("product key can not be null");
        }
        MktConsumerRightObject mktConsumerRightObject = marketingDomain.getConsumerRightByProductKey(key);
        if (mktConsumerRightObject != null) {
            return new MktConsumerRight(mktConsumerRightObject);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "validate/key/{key}", method = RequestMethod.GET)
    public String getMarketingValidateByProductKey(@PathVariable String key) {
        if (key == null) {
            throw new BadRequestException("product key can not be null");
        }

        //search product by key
        ProductObject productObject = getProductByKey(key);

        ProductKeyBatchObject productKeyBatchObject = productDomain.getProductKeyBatch(productObject.getProductKeyBatchId());

        if (productKeyBatchObject != null) {
            //marketing info
            String marketingId = productKeyBatchObject.getMarketingId();
            if (marketingId != null) {
                MarketingObject marketingObject = marketingDomain.getMarketingById(marketingId);
                long now = DateTime.now().getMillis();
                if (marketingObject != null
                        && (marketingObject.getStartDateTime() == null || marketingObject.getStartDateTime().getMillis() <= now)
                        && (marketingObject.getEndDateTime() == null || marketingObject.getEndDateTime().getMillis() >= now)
                        && LookupCodes.MktStatus.PAID.equals(marketingObject.getStatusCode())) {
                    return LookupCodes.MktVerifyStatus.VALID;
                } else {
                    return LookupCodes.MktVerifyStatus.INVALID;
                }
            } else {
                return LookupCodes.MktVerifyStatus.INVALID;
            }
        } else {
            throw new BadRequestException("product key batch not found");
        }


    }



    @RequestMapping(value = "drawPrize/{key}/sms", method = RequestMethod.PUT)
    public boolean sendPrizeSMS(@PathVariable(value = "key") String productKey,
                                @RequestParam(value = "mobile") String mobile) {

        return marketingDomain.sendVerificationCode(mobile, LookupCodes.SMSTemplate.SENDPRIZE);
    }

    @RequestMapping(value = "drawPrize/{key}/smsverfiy", method = RequestMethod.PUT)
    public boolean validatePrizeVerificationCode(@PathVariable(value = "key") String productKey,
                                                 @RequestParam(value = "mobile") String mobile,
                                                 @RequestParam(value = "verification_code") String verificationCode) {

        return marketingDomain.validateVerificationCode(mobile, verificationCode);
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

    @RequestMapping(value = "drawPrize/{id}/random", method = RequestMethod.POST)
    public MktDrawRule getRandomPrizeAmount(@PathVariable(value = "id") String marketingId, @RequestBody MktDraw mktDraw) {
        if (marketingId == null)
            throw new BadRequestException("marketing id can not be null");

        if(mktDraw == null || mktDraw.getPrize() == null || mktDraw.getRecord() == null)
            throw new BadRequestException("prize content can not be null");

        String key = mktDraw.getRecord().getProductKey();

        ProductObject product = productDomain.getProduct(key);
        if (product == null) {
            throw new NotFoundException("product can not be found by the key");
        }

        MktDrawRecordObject currentRecord = marketingDomain.getMktDrawRecordByProductKey(key);
        if(currentRecord != null)
            throw new ConflictException("key already prized");

        MktDrawRuleObject mktDrawRuleObject = marketingDomain.getMktRandomPrize(marketingId, mktDraw.getRecord().getScanRecordId());
        if (mktDrawRuleObject != null) {
            MktDrawPrize prize = mktDraw.getPrize();
            prize.setPrizeTypeCode(mktDrawRuleObject.getPrizeTypeCode());

            MktDrawRule mktDrawRule = new MktDrawRule(mktDrawRuleObject);
            String consumerRightId = mktDrawRuleObject.getConsumerRightId();
            if (consumerRightId != null) {
                MktConsumerRightObject consumerRightObject = marketingDomain.getConsumerRightById(consumerRightId);
                if (consumerRightObject != null) {
                    mktDrawRule.setMktConsumerRight(new MktConsumerRight(consumerRightObject));
                    prize.setPrizeTypeCode(consumerRightObject.getTypeCode());
                }
            }

            //save prize
            MktDrawRecord record = mktDraw.getRecord();
            record.setIsPrized(mktDrawRuleObject.getAmount() > 0 ? true: false);
            MktDrawRecordObject saveRecord = marketingDomain.createMktDrawRecord(record.toMktDrawRecordObject());

            setAccount(prize);
            prize.setDrawRecordId(saveRecord.getId());
            prize.setDrawRuleId(mktDrawRule.getId());
            prize.setAmount(mktDrawRule.getAmount());
            marketingDomain.createMktDrawPrize(prize.toMktDrawPrizeObject());

            return mktDrawRule;

        } else {
            return new MktDrawRule(mktDrawRuleObject);
        }
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

    @RequestMapping(value = "drawPrize/{id}/top", method = RequestMethod.GET)
    public List<MktDrawPrize> getTop10MarketingPrizeList(@PathVariable(value = "id") String marketingId, @RequestParam(value = "ys_id", required = false)String ysId) {
        if (marketingId == null)
            throw new BadRequestException("marketing id can not be null");

        List<MktDrawPrizeObject> mktDrawPrizeObjectList = marketingDomain.getTop10PrizeList(marketingId, ysId);
        List<MktDrawPrize> mktDrawPrizeList = new ArrayList<>();

        mktDrawPrizeObjectList.forEach(object -> {
            MktDrawPrize mktDrawPrize = new MktDrawPrize(object);
            MktDrawRuleObject mktDrawRuleObject = marketingDomain.getDrawRuleById(object.getDrawRuleId());
            MktConsumerRightObject mktConsumerRightObject = marketingDomain.getConsumerRightById(mktDrawRuleObject.getConsumerRightId());
            if (mktConsumerRightObject != null) {
                mktDrawPrize.setMktConsumerRight(new MktConsumerRight(mktConsumerRightObject));
            }
            mktDrawPrizeList.add(mktDrawPrize);
        });

        return mktDrawPrizeList;
    }

    private ProductObject getProductByKey(String key) {
        if (!KeyGenerator.validate(key)) {
            throw new NotFoundException("product not found");
        }
        ProductObject productObject = productDomain.getProduct(key);
        if (productObject == null || productObject.getProductBaseId() == null) {
            throw new NotFoundException("product not found");
        }
        return productObject;
    }


    private void setAccount(MktDrawPrize prize){
        prize.setAccountType(prize.getPrizeTypeCode());

        if(LookupCodes.MktPrizeType.MOBILE_FEE.equals(prize.getPrizeTypeCode()) || LookupCodes.MktPrizeType.MOBILE_DATA.equals(prize.getPrizeTypeCode()) ){
            prize.setAccountType("mobile");
            prize.setPrizeAccountName("手机用户");
        }
        else if (LookupCodes.MktPrizeType.COUPON.equals(prize.getPrizeTypeCode())){
            if(prize.getPrizeAccountName() == null)
            prize.setPrizeAccountName("手机用户");
        }
    }

}
