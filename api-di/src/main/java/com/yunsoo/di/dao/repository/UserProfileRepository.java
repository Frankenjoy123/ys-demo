package com.yunsoo.di.dao.repository;


import com.yunsoo.di.dao.entity.UserProfileLocationCountEntity;
import com.yunsoo.di.dao.entity.UserProfileTagCountEntity;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by:   xiaowu
 * Created on:   2016/11/1
 * Descriptions:
 */

public interface UserProfileRepository {

    List<UserProfileTagCountEntity> queryUserProfileTimeUsage(String orgId, DateTime startDateTime, DateTime endDateTime);
    List<UserProfileTagCountEntity> queryUserProfileDeviceUsage(String orgId);
    List<UserProfileTagCountEntity> queryUserProfileGenderUsage(String orgId);
    List<UserProfileTagCountEntity> queryUserProfileAreaReport(String orgId);
    List<UserProfileLocationCountEntity> queryUserProfileLocationReport(String orgId);

}
