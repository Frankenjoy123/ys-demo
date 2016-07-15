package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.EMREventEntity;
import com.yunsoo.data.service.entity.MarketUserLocationAnalysisEntity;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by yqy09_000 on 5/31/2016.
 */
public interface CustomEMREventRepository {
    int[] scanCount(String orgId, String productBaseId,
                    String province, String city,
                    DateTime createdDateTimeStart,
                    DateTime createdDateTimeEnd);

    int[] drawCount(String orgId, String productBaseId,
                    String province, String city,
                    DateTime createdDateTimeStart,
                    DateTime createdDateTimeEnd);
    int[] winCount(String orgId, String productBaseId,
                    String province, String city,
                    DateTime createdDateTimeStart,
                    DateTime createdDateTimeEnd);
    int[] wxCount(String orgId, String productBaseId,
                   String province, String city,
                   DateTime createdDateTimeStart,
                   DateTime createdDateTimeEnd);
    int[] rewardCount(String orgId, String productBaseId,
                  String province, String city,
                  DateTime createdDateTimeStart,
                  DateTime createdDateTimeEnd);

    EMREventEntity recentlyConsumptionEvent(String orgId, String userId, String ysId);
    int periodConsumptionCount(String orgId, String userId, String ysId,DateTime eventDateTimeStart, DateTime eventDateTimeEnd);
    List<MarketUserLocationAnalysisEntity> queryRewardLocationReport( String marketingId);
}
