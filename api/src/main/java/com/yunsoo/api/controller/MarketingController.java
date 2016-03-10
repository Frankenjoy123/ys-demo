package com.yunsoo.api.controller;

import com.yunsoo.api.domain.*;
import com.yunsoo.api.dto.Marketing;
import com.yunsoo.api.dto.MktDrawPrize;
import com.yunsoo.api.dto.MktDrawRule;
import com.yunsoo.api.dto.ScanRecord;
import com.yunsoo.api.payment.ParameterNames;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.*;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by  : haitao
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
    private ProductKeyDomain productKeyDomain;

    @Autowired
    private UserScanDomain userScanDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "drawRule", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MktDrawRule createMktDrawRule(@RequestBody MktDrawRule mktDrawRule) {
        if (mktDrawRule == null) {
            throw new BadRequestException("marketing draw record can not be null");
        }
        String marketingId = mktDrawRule.getMarketingId();
        MarketingObject marketingObject = marketingDomain.getMarketingById(marketingId);
        if (marketingObject == null) {
            throw new NotFoundException("marketing can not be found by the id");
        }
        MktDrawRuleObject mktDrawRuleObject = mktDrawRule.toMktDrawRuleObject();
        String currentUserId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        mktDrawRuleObject.setCreatedAccountId(currentUserId);
        mktDrawRuleObject.setCreatedDateTime(DateTime.now());

        MktDrawRuleObject newMktDrawRuleObject = marketingDomain.createMktDrawRule(mktDrawRuleObject);
        return new MktDrawRule(newMktDrawRuleObject);
    }



    //query marketing plan by org id
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Marketing> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                       Pageable pageable,
                                       HttpServletResponse response) {
        orgId = fixOrgId(orgId);
        Page<MarketingObject> marketingPage = marketingDomain.getMarketingByOrgId(orgId, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", marketingPage.toContentRange());
        }

        List<Marketing> marketingList = new ArrayList<>();
        marketingPage.getContent().forEach(object -> {
            Marketing marketing = new Marketing(object);
            ProductBaseObject pbo = productBaseDomain.getProductBaseById(object.getProductBaseId());
            if (pbo != null) {
                marketing.setProductBaseName(pbo.getName());
            }
            marketingList.add(marketing);
        });

        return marketingList;
    }

    //create marketing plan
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Marketing createMarketing(@RequestParam(value = "batchId", required = false) String batchId,
                                     @RequestBody Marketing marketing) {
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        MarketingObject marketingObject = marketing.toMarketingObject();
        marketingObject.setCreatedAccountId(currentAccountId);
        marketingObject.setCreatedDateTime(DateTime.now());
        if (StringUtils.hasText(marketing.getOrgId()))
            marketingObject.setOrgId(marketing.getOrgId());
        else
            marketingObject.setOrgId(tokenAuthenticationService.getAuthentication().getDetails().getOrgId());

        MarketingObject mktObject = new MarketingObject();
        if (batchId != null) {
            ProductKeyBatchObject batchObject = productKeyDomain.getPkBatchById(batchId);
            if (batchObject != null) {
                marketingObject.setOrgId(batchObject.getOrgId());
                marketingObject.setProductBaseId(batchObject.getProductBaseId());
                mktObject = marketingDomain.createMarketing(marketingObject);

                batchObject.setMarketingId(mktObject.getId());

                productKeyDomain.updateProductKeyBatch(batchObject);

            } else {
                mktObject = marketingDomain.createMarketing(marketingObject);
            }
        } else {
            mktObject = marketingDomain.createMarketing(marketingObject);
        }
        return new Marketing(mktObject);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public void enableMarketing(@PathVariable(value = "id")String id){
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        MarketingObject marketingObject = marketingDomain.getMarketingById(id);
        if(marketingObject != null) {
            marketingObject.setStatusCode(LookupCodes.MktStatus.AVAILABLE);
            marketingObject.setModifiedDateTime(DateTime.now());
            marketingObject.setModifiedAccountId(currentAccountId);
            marketingDomain.updateMarketing(marketingObject);
        }
    }

    @RequestMapping(value = "drawPrize/marketing", method = RequestMethod.GET)
    public List<MktDrawPrize> getMktDrawPrizeByFilter(@RequestParam(value = "marketing_id") String marketingId,
                                                      @RequestParam(value = "account_type", required = false) String accountType,
                                                      @RequestParam(value = "status_code", required = false) String statusCode,
                                                      Pageable pageable,
                                                      HttpServletResponse response) {
        if (marketingId == null || marketingId.isEmpty()) {
            throw new BadRequestException("marketing id is not valid");
        }

        Page<MktDrawPrizeObject> mktDrawPrizePage = marketingDomain.getMktDrawPrizeByFilter(marketingId, accountType, statusCode, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", mktDrawPrizePage.toContentRange());
        }

        List<MktDrawPrize> mktDrawPrizeList = new ArrayList<>();
        mktDrawPrizePage.getContent().forEach(object -> {
            MktDrawPrize mktDrawPrize = new MktDrawPrize(object);
            MarketingObject mkto = marketingDomain.getMarketingById(object.getMarketingId());
            if (mkto != null) {
                String productBaseId = mkto.getProductBaseId();
                ProductBaseObject pbo = productBaseDomain.getProductBaseById(productBaseId);
                mktDrawPrize.setProductBaseName(pbo.getName());
            }
            String scanRecordId = object.getScanRecordId();
            UserScanRecordObject userScanRecordObject = userScanDomain.getScanRecordById(scanRecordId);
            if (userScanRecordObject != null) {
                mktDrawPrize.setScanRecord(new ScanRecord(userScanRecordObject));
            }
            mktDrawPrizeList.add(mktDrawPrize);
        });

        return mktDrawPrizeList;
    }


    //delete marketing plan by id
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMarketing(@PathVariable(value = "id") String id) {
        MarketingObject marketingObject = marketingDomain.getMarketingById(id);
        if (marketingObject != null) {
            marketingDomain.deleteMarketingById(id);
        }
    }

    //alipay batch transfer
    @RequestMapping(value = "alipay_batchtransfer", method = RequestMethod.GET)
    public Map<String, String> batchTransfer() {
        Map<String, String> parametersMap = marketingDomain.getAlipayBatchTansferParameters();
        List<String> keys = new ArrayList<>(parametersMap.keySet());
        keys.sort(null);
        return parametersMap;

    }

    @RequestMapping(value = "alipay/notify", method = RequestMethod.POST)
    public void alipayNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> parametersMap = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();

        String notifyType = request.getParameter(ParameterNames.NOTIFY_TYPE);
        List<String> drawRecordIds = new ArrayList<String>();
        String batchNo = request.getParameter("batch_no");

        if (notifyType.equals("batch_trans_notify")) {
            String successDetails = request.getParameter("success_details");
            String failDetails = request.getParameter("fail_details");

            if (successDetails != null) {
                String[] orderPaymentDetails = successDetails.split("\\|");
                for (int i = 0; i < orderPaymentDetails.length; i++) {
                    String[] orderPaymentDetail = orderPaymentDetails[i].split("\\^");
                    drawRecordIds.add(orderPaymentDetail[0]);
                }
                if (drawRecordIds.size() != 0) {
                    for (String id : drawRecordIds) {
                        MktDrawPrizeObject mktDrawPrizeObject = marketingDomain.getMktDrawPrizeById(id);
                        mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.PAID);
                        mktDrawPrizeObject.setPaidDateTime(DateTime.now());
                        marketingDomain.updateMktDrawPrize(mktDrawPrizeObject);
                    }
                }
                response.getWriter().print("success");
            }

        }

    }


    private String fixOrgId(String orgId) {
        if (orgId == null || "current".equals(orgId)) {
            //current orgId
            return tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }
        return orgId;
    }

}
