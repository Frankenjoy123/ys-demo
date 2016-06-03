package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.EMRUserEntity;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

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

}
