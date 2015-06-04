package com.yunsoo.api.controller;

import com.yunsoo.api.domain.LogisticsDomain;
import com.yunsoo.api.domain.ProductDomain;
import com.yunsoo.api.dto.*;
import com.yunsoo.common.util.DateTimeUtils;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/2/27
 * Descriptions:
 */

@RestController
@RequestMapping("/scan")
public class ScanController {

    @Autowired
    private RestClient dataAPIClient;
    @Autowired
    private LogisticsDomain logisticsDomain;
    @Autowired
    private ProductDomain productDomain;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScanController.class);


    //仅仅能够访问属于特定组织的Key
    @RequestMapping(value = "/{orgid}/{key}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'filterByOrg', 'scan:read')")
    public ScanResultWeb getDetailForWebByKey(@PathVariable(value = "orgid") String orgid,
                                              @PathVariable(value = "key") String key) {

        if (key == null || key.isEmpty()) {
            throw new BadRequestException("查询码不能为空！");
        }

        ScanResultWeb scanResult = new ScanResultWeb();
        scanResult.setKey(key);

        //set product information
        Product currentExistProduct = productDomain.getProductByKey(key);
        if (currentExistProduct == null) {
            //Not found by the product Key
            scanResult.setResultCode(0);
            scanResult.setMessage(String.format("该码 %s 不存在！", key));  //no such key in our Yunsoo Platform.
        } else {
            if (currentExistProduct.getOrgId() == orgid) {
                scanResult.setProduct(currentExistProduct);
                //retrieve scan records
                ScanRecord[] scanRecords = dataAPIClient.get("scan/filterby?productKey={productKey}&pageSize={pageSize}", ScanRecord[].class, key, Integer.MAX_VALUE);
                List<ScanRecord> scanRecordList = Arrays.asList(scanRecords == null ? new ScanRecord[0] : scanRecords);
                scanResult.setScanRecord(scanRecordList);
                scanResult.setScanCounter(scanRecordList.size() + 1); //设置当前是第几次被最终用户扫描 - 根据用户扫描记录表.

                //retrieve logistics information
                scanResult.setLogisticsList(getLogisticsInfo(key));
                scanResult.setResultCode(1); //产品码存在
                scanResult.setMessage("查询成功！");
            } else {
                scanResult.setResultCode(2); //无权查看别人的码
                scanResult.setMessage(String.format("无权查看，该码不属于贵公司！", key));
            }
        }
        return scanResult;
    }


    private List<Logistics> getLogisticsInfo(String key) {
        List<LogisticsPath> logisticsPaths;
        try {
            logisticsPaths = logisticsDomain.getLogisticsPathsOrderByStartDateTime(key);
        } catch (NotFoundException ex) {
            //to do: log
            LOGGER.warn("物流信息找不到 - Key = " + key);
            return null;
        }

        List<Logistics> logisticsList = new ArrayList<Logistics>();
        for (LogisticsPath path : logisticsPaths) {
            Logistics logistics = new Logistics();
            logistics.setOrgId(path.getStartCheckPointObject().getOrgId());
            logistics.setOrgName(path.getStartCheckPointOrgObject().getName());
            logistics.setMessage(path.getActionObject().getName());
            logistics.setLocation(path.getStartCheckPointObject().getName());
            logistics.setDateTime(DateTimeUtils.toString(path.getStartDate()));
            logisticsList.add(logistics);
        }
        return logisticsList;
    }

}
