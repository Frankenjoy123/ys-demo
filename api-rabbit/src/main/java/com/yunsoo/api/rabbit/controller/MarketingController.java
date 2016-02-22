package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.MarketingDomain;
import com.yunsoo.api.rabbit.domain.ProductDomain;
import com.yunsoo.api.rabbit.dto.MktDrawPrize;
import com.yunsoo.api.rabbit.dto.MktDrawRecord;
import com.yunsoo.api.rabbit.dto.Product;
import com.yunsoo.api.rabbit.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.MktDrawPrizeObject;
import com.yunsoo.common.data.object.MktDrawRecordObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "drawprize/{key}", method = RequestMethod.GET)
    public MktDrawPrize getMktDrawPrizeByProductKey(@PathVariable String key) {
        if (key == null) {
            throw new BadRequestException("product key can not be null");
        }
        MktDrawPrizeObject mktDrawPrizeObject = marketingDomain.getMktDrawPrizeByProductKey(key);
        if (mktDrawPrizeObject != null) {
            return new MktDrawPrize(mktDrawPrizeObject);
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
        Product product = productDomain.getProductByKey(productKey);
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
        Product product = productDomain.getProductByKey(productKey);
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
        MktDrawPrizeObject mktDrawPrizeObject = mktDrawPrize.toMktDrawPrizeObject();
        marketingDomain.updateMktDrawPrize(mktDrawPrizeObject);
    }


}
