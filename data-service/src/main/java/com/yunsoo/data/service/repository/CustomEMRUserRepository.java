package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.EMRUserEntity;
import com.yunsoo.data.service.entity.EMRUserProductEventStatistics;
import com.yunsoo.data.service.entity.UserProfileLocationCountEntity;
import com.yunsoo.data.service.entity.UserProfileTagCountEntity;
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

    List<UserProfileTagCountEntity> queryUserProfileTimeUsage(String orgId, DateTime startDateTime, DateTime endDateTime);
    List<UserProfileTagCountEntity> queryUserProfileDeviceUsage(String orgId);
    List<UserProfileTagCountEntity> queryUserProfileGenderUsage(String orgId);
    List<UserProfileTagCountEntity> queryUserProfileAreaReport(String orgId);
    List<UserProfileLocationCountEntity> queryUserProfileLocationReport(String orgId);

}
