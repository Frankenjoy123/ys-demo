package com.yunsoo.api.controller.analysis;

import com.yunsoo.api.di.dto.DrawAnalysisReport;
import com.yunsoo.api.di.service.DrawAnalysisService;
import com.yunsoo.api.util.AuthUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yqy09_000 on 2016/11/14.
 */
@RestController
@RequestMapping("/analysis/draw")
public class DrawAnalysisController {

    @Autowired
    private DrawAnalysisService drawAnalysisService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<DrawAnalysisReport> getById(@RequestParam(value = "m_id") String marketingId,
                                            @RequestParam(value = "by", required = false) boolean orgByPass,
                                            @RequestParam(value = "ds", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startDate,
                                            @RequestParam(value = "de", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endDate
    ) {
        String orgId = "";
        if (!orgByPass) {
            orgId = AuthUtils.getCurrentAccount().getOrgId();
        }
        LocalDate now = LocalDate.now();
        if (startDate == null) {
            startDate = now.plusDays(-90);
        }
        if (endDate == null) {
            endDate = now.plusDays(-1);
        }

        return drawAnalysisService.getDrawAnalysisReport(orgId, marketingId,orgByPass, startDate, endDate);
    }
}
