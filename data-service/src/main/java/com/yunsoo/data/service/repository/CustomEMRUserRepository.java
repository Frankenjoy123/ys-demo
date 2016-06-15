package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.EMRUserEntity;
import com.yunsoo.data.service.entity.EMRUserProductEventStatistics;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by yqy09_000 on 5/31/2016.
 */
public interface CustomEMRUserRepository {
    List<EMRUserEntity> findEventUsersFilterByScan(String orgId,
                                                   String productBaseId,
                                                   String province, String city,
                                                   DateTime createdDateTimeStart,
                                                   DateTime createdDateTimeEnd,
                                                   Pageable pageable);

    int  countEventUsersFilterByScan( String orgId,
                                      String productBaseId,
                                     String province,  String city,
                                     DateTime createdDateTimeStart,
                                    DateTime createdDateTimeEnd);

    List<EMRUserProductEventStatistics> queryUserEventStatistics(String orgId, String userId, String ysId, DateTime createdDateTimeStart,
                                                                 DateTime createdDateTimeEnd);

}
