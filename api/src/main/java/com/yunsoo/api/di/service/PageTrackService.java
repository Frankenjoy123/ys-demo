package com.yunsoo.api.di.service;

import com.yunsoo.api.client.DiApiClient;
import com.yunsoo.api.di.dto.PageTrackInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yqy09_000 on 2016/11/17.
 */
@Service
public class PageTrackService {

    @Autowired
    private DiApiClient diApiClient;

    public void trackPageView(PageTrackInfo pageInfo) {
        diApiClient.postAsync("track/page", pageInfo, Void.class);
    }
}
