package com.yunsoo.di.api.controller;

import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.di.dao.entity.*;
import com.yunsoo.di.dao.repository.*;
import com.yunsoo.di.dto.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xiaowu on 2016/10/26.
 */

@RestController
@RequestMapping("/scan")
public class ScanController {

    @Autowired
    private ScanRecordAnalysisRepository scanRecordAnalysisRepository;

    @Autowired
    private ScanRecordRepository scanRecordRepository;

    @Autowired
    private RankRepository rankRepository;


    @RequestMapping(value = "/scan_data", method = RequestMethod.GET)
    public List<ScanRecordAnalysisObject> query(@RequestParam(value = "org_id") String orgId,
                                                @RequestParam(value = "start_time")
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                @RequestParam(value = "end_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                @RequestParam(value = "batch_id", required = false) String batchId) {

        if (StringUtils.isEmpty(productBaseId)) {
            productBaseId = "All";
        }
        if (StringUtils.isEmpty(batchId))
            batchId = null;

        DateTime startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        DateTime endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        List<ScanRecordAnalysisEntity> list = scanRecordAnalysisRepository.query(orgId, startDateTime, endDateTime, productBaseId, batchId);
        return list.stream().map(ScanRecordAnalysisEntity::toDataObject).collect(Collectors.toList());
    }


    @RequestMapping(value = "/scan_data_location", method = RequestMethod.GET)
    public List<ScanRecordLocationAnalysisObject> queryWithLocation(@RequestParam(value = "org_id") String orgId,
                                                                    @RequestParam(value = "start_time")
                                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                                    @RequestParam(value = "end_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                                    @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                                    @RequestParam(value = "batch_id", required = false) String batchId) {
        if (StringUtils.isEmpty(productBaseId))
            productBaseId = null;
        if (StringUtils.isEmpty(batchId))
            batchId = null;

        DateTime startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        DateTime endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        List<ScanRecordLocationAnalysisEntity> list = scanRecordRepository.consumerLocationCount(orgId, productBaseId, batchId, startDateTime, endDateTime);
        return list.stream().map(ScanRecordLocationAnalysisEntity::toDataObject).collect(Collectors.toList());
    }


    @RequestMapping(value = "/rank", method = RequestMethod.GET)
    public List<RankUserObject> queryRankUser(@RequestParam(value = "org_id") String orgId,
                                              @RequestParam(value = "threshold") Integer threshold,
                                              @RequestParam(value = "limit",required = false) Integer limit,
                                              @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                              Pageable pageable,
                                              HttpServletResponse response) {

        if (StringUtils.isEmpty(productBaseId))
            productBaseId = null;

        List<RankUserEntity> list=rankRepository.getRankUsers(orgId,limit,threshold,productBaseId,pageable);

        int totalCount=rankRepository.getRankUsersCount(orgId,limit,threshold,productBaseId);

        Page<RankUserEntity> entityPage=new PageImpl<RankUserEntity>(list,pageable,totalCount);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }

        return entityPage.getContent().stream().map(RankUserEntity::toDataObject).collect(Collectors.toList());
    }

}
