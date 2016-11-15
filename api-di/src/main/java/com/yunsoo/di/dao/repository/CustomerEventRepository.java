package com.yunsoo.di.dao.repository;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by yqy09_000 on 5/31/2016.
 */
public interface CustomerEventRepository {
    int[] scanCount(String orgId, String productBaseId,
                    String province, String city,
                    DateTime createdDateTimeStart,
                    DateTime createdDateTimeEnd ,
                    Boolean wxUser);

    int[] wxCount(String orgId, String productBaseId,
                  String province, String city,
                  DateTime createdDateTimeStart,
                  DateTime createdDateTimeEnd);

    int[] drawCount(String orgId, String productBaseId,
                    String province, String city,
                    DateTime createdDateTimeStart,
                    DateTime createdDateTimeEnd,
                    Boolean wxUser);

    int[] winCount(String orgId, String productBaseId,
                   String province, String city,
                   DateTime createdDateTimeStart,
                   DateTime createdDateTimeEnd,
                   Boolean wxUser);

    int[] rewardCount(String orgId, String productBaseId,
                      String province, String city,
                      DateTime createdDateTimeStart,
                      DateTime createdDateTimeEnd,
                      Boolean wxUser);

    int[] shareCount(String orgId, String productBaseId,
                     String province, String city,
                     DateTime createdDateTimeStart,
                     DateTime createdDateTimeEnd);

    int[] storeUrlCount(String orgId, String productBaseId,
                        String province, String city,
                        DateTime createdDateTimeStart,
                        DateTime createdDateTimeEnd);

    int[] commentCount(String orgId, String productBaseId,
                       String province, String city,
                       DateTime createdDateTimeStart,
                       DateTime createdDateTimeEnd);

    List<String[]> scanDailyCount(String orgId, String productBaseId,
                                  String province, String city,
                                  DateTime createdDateTimeStart,
                                  DateTime createdDateTimeEnd);

    List<String[]> shareDailyCount(String orgId, String productBaseId,
                                   String province, String city,
                                   DateTime createdDateTimeStart,
                                   DateTime createdDateTimeEnd);

    List<String[]> storeUrlDailyCount(String orgId, String productBaseId,
                                      String province, String city,
                                      DateTime createdDateTimeStart,
                                      DateTime createdDateTimeEnd);

    List<String[]> commentDailyCount(String orgId, String productBaseId,
                                     String province, String city,
                                     DateTime createdDateTimeStart,
                                     DateTime createdDateTimeEnd);

    List<int[]> eventLocationCount(String orgId, String productBaseId,
                                   String province, String city,
                                   DateTime createdDateTimeStart,
                                   DateTime createdDateTimeEnd);

    List<String[]> scanLocationCount(String orgId, String productBaseId,
                                     String province, String city,
                                     DateTime createdDateTimeStart,
                                     DateTime createdDateTimeEnd);

    List<String[]> shareLocationCount(String orgId, String productBaseId,
                                      String province, String city,
                                      DateTime createdDateTimeStart,
                                      DateTime createdDateTimeEnd);

    List<String[]> storeUrlLocationCount(String orgId, String productBaseId,
                                         String province, String city,
                                         DateTime createdDateTimeStart,
                                         DateTime createdDateTimeEnd);

    List<String[]> commentLocationCount(String orgId, String productBaseId,
                                        String province, String city,
                                        DateTime createdDateTimeStart,
                                        DateTime createdDateTimeEnd);

}
