package com.yunsoo.di.api.controller;

import com.yunsoo.di.dao.entity.PageTrackInfoEntity;
import com.yunsoo.di.dao.repository.PageTrackRepository;
import com.yunsoo.di.dto.PageTrackInfo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
        return entity;
    }


}
