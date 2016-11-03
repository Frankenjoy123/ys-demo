package com.yunsoo.di.dao.repository;

import com.yunsoo.di.dao.entity.MarketUserLocationAnalysisEntity;
import com.yunsoo.di.dao.entity.UserProfileLocationCountEntity;
import com.yunsoo.di.dao.entity.UserProfileTagCountEntity;
import org.joda.time.DateTime;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by yunsu on 2016/11/2.
 */
public interface MarketUserRepository {

    List<UserProfileTagCountEntity> queryMarketUserAreaAnalysis( String orgId, DateTime startTime, DateTime endTime, String marketingId);

    List<UserProfileTagCountEntity> queryMarketUserDeviceAnalysis( String orgId, DateTime startTime, DateTime endTime, String marketingId);

    List<UserProfileTagCountEntity> queryMarketUserGenderAnalysis( String orgId, DateTime startTime, DateTime endTime, String marketingId);

    List<UserProfileTagCountEntity> queryMarketUserUsageAnalysis( String orgId, DateTime startTime, DateTime endTime, String marketingId);

    List<UserProfileLocationCountEntity> queryMarketUserLocationAnalysis( String orgId, DateTime startTime, DateTime endTime, String marketingId);

    List<MarketUserLocationAnalysisEntity> queryRewardLocationReport(String marketingId);




}
