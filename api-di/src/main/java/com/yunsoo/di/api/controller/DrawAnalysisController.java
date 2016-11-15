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

    private static DrawAnalysisReport toDrawAnalysisReport(DrawReportEntity drawReportEntity) {
        DrawAnalysisReport report = new DrawAnalysisReport();
        report.setDrawName(drawReportEntity.getDrawRuleName());
        report.setCount(drawReportEntity.getCount());
        report.setId(drawReportEntity.getId());
        return report;
    }
}
