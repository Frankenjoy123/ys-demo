package com.yunsoo.di.api.controller;

import com.yunsoo.di.dao.entity.PageTrackInfoEntity;
import com.yunsoo.di.dao.entity.PageViewDailyEntity;
import com.yunsoo.di.dao.repository.PageTrackRepository;
import com.yunsoo.di.dto.PageTrackInfo;
import com.yunsoo.di.dto.PageViewDaily;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yqy09_000 on 2016/11/17.
 */
@RestController
@RequestMapping("/track")
public class PageTrackController {


    @Autowired
    private PageTrackRepository pageTrackRepository;

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void track(@RequestBody PageTrackInfo pageTrackInfo)
    {
        PageTrackInfoEntity entity = toPageTrackInfoEntity(pageTrackInfo);
        pageTrackRepository.save(entity);
    }

    @RequestMapping(value = "/daily_report", method = RequestMethod.GET)
    public List<PageViewDaily> track(@RequestParam(value = "host_url") String hostUrl, @RequestParam(value = "date_from")
                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate dateFrom,
                      @RequestParam(value = "date_end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate dateEnd)
    {
        DateTime startDateTime = dateFrom.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        DateTime endDateTime = dateEnd.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusDays(1);
        List<PageViewDailyEntity> entities =  pageTrackRepository.query(hostUrl, startDateTime, endDateTime);
        return entities.stream().map(PageTrackController::toPageViewDaily).collect(Collectors.toList());
    }

    @RequestMapping(value = "/page_view_total", method = RequestMethod.GET)
    public int[] track(@RequestParam(value = "host_url") String hostUrl)
    {
        int[] data =  pageTrackRepository.totalPageView(hostUrl);
        return data;
    }

    private static PageViewDaily toPageViewDaily(PageViewDailyEntity entity)
    {
        PageViewDaily daily = new PageViewDaily();
        daily.setPv(entity.getPv());
        daily.setUv(entity.getUv());
        daily.setDate(entity.getDate().toString("yyyy-MM-dd"));
        return daily;
    }

    private static PageTrackInfoEntity toPageTrackInfoEntity(PageTrackInfo pageTrackInfo) {
        PageTrackInfoEntity entity = new PageTrackInfoEntity();
        entity.setYsId(pageTrackInfo.getYsId());
        entity.setUserAgent(pageTrackInfo.getUserAgent());
        entity.setUrl(pageTrackInfo.getUrl());
        entity.setIp(pageTrackInfo.getIp());
        entity.setAddress(pageTrackInfo.getAddress());
        entity.setProvince(pageTrackInfo.getProvince());
        entity.setCity(pageTrackInfo.getCity());
        entity.setAction(pageTrackInfo.getAction());
        entity.setActionData(pageTrackInfo.getActionData());
        entity.setCreatedDateTime(DateTime.now());
        entity.setId(0L);
        entity.setPageId(pageTrackInfo.getPageId());
        return entity;
    }


}
