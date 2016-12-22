package com.yunsoo.di.dao.repository;

import com.yunsoo.di.dao.entity.PageViewDailyEntity;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by yqy09_000 on 2016/11/30.
 */
public interface PageViewAnalysisRepository {
    List<PageViewDailyEntity> query(String hostUrl, DateTime startDateTime, DateTime endDateTime);
    int[] totalPageView(String hostUrl);
}
