package com.yunsoo.di.api.controller;

import com.yunsoo.di.dao.entity.DrawReportEntity;
import com.yunsoo.di.dao.repository.DrawAnalysisRepository;
import com.yunsoo.di.dto.DrawAnalysisReport;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yqy09_000 on 2016/11/14.
 */
@RestController
@RequestMapping("/draw_analysis")
public class DrawAnalysisController {

    @Autowired
    private DrawAnalysisRepository drawAnalysisRepository;

    /**
     *
     * @param orgId org_id 为企业id
     * @param marketingId m_id 为营销id
     * @param orgBypass by
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "")
    public List<DrawAnalysisReport> getDrawAnalysisReportBy(
            @RequestParam(value = "org_id") String orgId,
            @RequestParam(value = "m_id") String marketingId,
            @RequestParam(value = "by", required = false) boolean orgBypass,
            @RequestParam(value = "ds")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startDate,
            @RequestParam(value = "de") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endDate
    ) {

        DateTime startDateTime = startDate.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        DateTime endDateTime = endDate.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusDays(1);
        List<DrawReportEntity> list = drawAnalysisRepository.getDrawReportBy(orgId,marketingId,orgBypass,startDateTime, endDateTime);
        return list.stream().map(DrawAnalysisController::toDrawAnalysisReport).collect(Collectors.toList());

    }

    @RequestMapping(value = "prize_rank")
    public List<DrawAnalysisReport> getDrawPrizeRankBy(
            @RequestParam(value = "m_id") String marketingId,
            @RequestParam(value = "ds",required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startDate,
            @RequestParam(value = "de",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endDate
    ) {

        DateTime startDateTime = startDate==null?null:startDate.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        DateTime endDateTime = endDate==null?null:endDate.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusDays(1);
        List<DrawReportEntity> list = drawAnalysisRepository.getDrawPrizeRankBy(marketingId,startDateTime, endDateTime);
        return list.stream().map(DrawAnalysisController::toDrawAnalysisReport).collect(Collectors.toList());

    }


    private static DrawAnalysisReport toDrawAnalysisReport(DrawReportEntity drawReportEntity) {
        DrawAnalysisReport report = new DrawAnalysisReport();
        report.setDrawName(drawReportEntity.getDrawRuleName());
        report.setCount(drawReportEntity.getCount());
        report.setId(drawReportEntity.getId());
        return report;
    }
}
