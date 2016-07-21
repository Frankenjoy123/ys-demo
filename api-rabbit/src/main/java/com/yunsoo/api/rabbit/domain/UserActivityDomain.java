package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.client.DataApiClient;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.UserActivityObject;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by:   Lijian
 * Created on:   2015/8/27
 * Descriptions:
 */
@Component
public class UserActivityDomain {

    private static final int[] SIGN_IN_POINTS_REWARDS = new int[]{10, 15, 20, 25, 30};

    @Autowired
    private DataApiClient dataApiClient;

    @Autowired
    private UserPointDomain userPointDomain;


    public int getSignInContinuousDays(String userId) {
        UserActivityObject userActivityObject = getUserActivityByUserId(userId);
        if (userActivityObject == null) {
            return 0;
        }
        return getSignInContinuousDays(userActivityObject);
    }

    public int signIn(String userId) {
        UserActivityObject userActivityObject = getUserActivityByUserId(userId);
        int days, points = 0;
        if (userActivityObject == null) {
            days = 1;
            points = getPointsRewardsForSignIn(days);
            userActivityObject = new UserActivityObject();
            userActivityObject.setUserId(userId);
            userActivityObject.setLastSignInContinuousDays(days);
            userActivityObject.setLastSignInDateTime(DateTime.now());
            dataApiClient.put("useractivity/{userId}", userActivityObject, userId);
        } else {
            if (userActivityObject.getLastSignInDateTime() == null
                    || userActivityObject.getLastSignInContinuousDays() == null
                    || userActivityObject.getLastSignInContinuousDays() <= 0) {
                days = 1;
                points = getPointsRewardsForSignIn(days);
            } else {
                int daysTillNow = daysTillNow(userActivityObject.getLastSignInDateTime());
                if (daysTillNow <= 0) {
                    //duplicated sign in today
                    days = userActivityObject.getLastSignInContinuousDays();
                    points = 0;
                } else {
                    days = daysTillNow == 1 ? userActivityObject.getLastSignInContinuousDays() + 1 : 1;
                    points = getPointsRewardsForSignIn(days);
                }
            }
            userActivityObject.setLastSignInContinuousDays(days);
            userActivityObject.setLastSignInDateTime(DateTime.now());
            dataApiClient.patch("useractivity/{userId}", userActivityObject, userId);
        }

        if (points > 0) {
            //points rewards
            points = getPointsRewardsForSignIn(days);
            userPointDomain.addPointToUser(userId, points, LookupCodes.UserPointTransactionType.SIGN_IN_REWARDS);
        }

        return days;
    }


    private UserActivityObject getUserActivityByUserId(String userId) {
        try {
            return dataApiClient.get("useractivity/{userId}", UserActivityObject.class, userId);

        } catch (NotFoundException ignored) {
            return null;
        }
    }

    private int getSignInContinuousDays(UserActivityObject userActivityObject) {
        Integer days = userActivityObject.getLastSignInContinuousDays();
        DateTime lastSignInDateTime = userActivityObject.getLastSignInDateTime();
        if (days != null
                && days > 0
                && lastSignInDateTime != null
                && daysTillNow(lastSignInDateTime) <= 1) {
            return days;
        } else {
            return 0;
        }
    }

    /**
     * calculate on China local time, +8 timezone
     *
     * @param lastSignInDateTime not null
     */
    private int daysTillNow(DateTime lastSignInDateTime) {
        return Days.daysBetween(lastSignInDateTime.toDateTime(DateTimeZone.forOffsetHours(8)).toLocalDate(),
                DateTime.now().toDateTime(DateTimeZone.forOffsetHours(8)).toLocalDate()).getDays();
    }

    private int getPointsRewardsForSignIn(int days) {
        return days <= 0 ? 0
                : days > SIGN_IN_POINTS_REWARDS.length ? SIGN_IN_POINTS_REWARDS[SIGN_IN_POINTS_REWARDS.length - 1]
                : SIGN_IN_POINTS_REWARDS[days - 1];
    }

}
