package com.yunsoo.di.dao.repository;

import org.joda.time.DateTime;

import java.util.List;


public interface CustomerEventRepository {
    /**
     * 消费者行为分析（漏斗），扫码
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStart
     * @param createdDateTimeEnd
     * @param wxUser
     * @return
     */
    int[] scanCount(String orgId, String productBaseId,
                    String province, String city,
                    DateTime createdDateTimeStart,
                    DateTime createdDateTimeEnd ,
                    Boolean wxUser);

    /**
     * 消费者行为分析（漏斗），抽奖
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStart
     * @param createdDateTimeEnd
     * @param wxUser
     * @return
     */
    int[] drawCount(String orgId, String productBaseId,
                    String province, String city,
                    DateTime createdDateTimeStart,
                    DateTime createdDateTimeEnd,
                    Boolean wxUser);

    /**
     * 消费者行为分析（漏斗），中奖
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStart
     * @param createdDateTimeEnd
     * @param wxUser
     * @return
     */
    int[] winCount(String orgId, String productBaseId,
                   String province, String city,
                   DateTime createdDateTimeStart,
                   DateTime createdDateTimeEnd,
                   Boolean wxUser);

    /**
     * 消费者行为分析（漏斗），兑奖
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStart
     * @param createdDateTimeEnd
     * @param wxUser
     * @return
     */
    int[] rewardCount(String orgId, String productBaseId,
                      String province, String city,
                      DateTime createdDateTimeStart,
                      DateTime createdDateTimeEnd,
                      Boolean wxUser);

    /**
     * 客户成功看板，每日扫码次数、人次
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStart
     * @param createdDateTimeEnd
     * @return
     */
    List<String[]> scanDailyCount(String orgId, String productBaseId,
                                  String province, String city,
                                  DateTime createdDateTimeStart,
                                  DateTime createdDateTimeEnd);

    /**
     * 客户成功看板，每日分享次数、人次
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStart
     * @param createdDateTimeEnd
     * @return
     */
    List<String[]> shareDailyCount(String orgId, String productBaseId,
                                   String province, String city,
                                   DateTime createdDateTimeStart,
                                   DateTime createdDateTimeEnd);

    /**
     * 客户成功看板，每日收藏次数、人次
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStart
     * @param createdDateTimeEnd
     * @return
     */
    List<String[]> storeUrlDailyCount(String orgId, String productBaseId,
                                      String province, String city,
                                      DateTime createdDateTimeStart,
                                      DateTime createdDateTimeEnd);

    /**
     * 客户成功看板，每日评论次数、人次
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStart
     * @param createdDateTimeEnd
     * @return
     */
    List<String[]> commentDailyCount(String orgId, String productBaseId,
                                     String province, String city,
                                     DateTime createdDateTimeStart,
                                     DateTime createdDateTimeEnd);

    /**
     * 客户成功看板，地区分析，总的扫码、分享、导流、评论次数、人次
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStart
     * @param createdDateTimeEnd
     * @return
     */
    List<int[]> eventLocationCount(String orgId, String productBaseId,
                                   String province, String city,
                                   DateTime createdDateTimeStart,
                                   DateTime createdDateTimeEnd);

    /**
     * 客户成功看板，地区分析,扫码次数、人次
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStart
     * @param createdDateTimeEnd
     * @return
     */
    List<String[]> scanLocationCount(String orgId, String productBaseId,
                                     String province, String city,
                                     DateTime createdDateTimeStart,
                                     DateTime createdDateTimeEnd);

    /**
     * 客户成功看板，地区分析,分享次数、人次
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStart
     * @param createdDateTimeEnd
     * @return
     */
    List<String[]> shareLocationCount(String orgId, String productBaseId,
                                      String province, String city,
                                      DateTime createdDateTimeStart,
                                      DateTime createdDateTimeEnd);

    /**
     * 客户成功看板，地区分析,导流次数、人次
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStart
     * @param createdDateTimeEnd
     * @return
     */
    List<String[]> storeUrlLocationCount(String orgId, String productBaseId,
                                         String province, String city,
                                         DateTime createdDateTimeStart,
                                         DateTime createdDateTimeEnd);

    /**
     * 客户成功看板，地区分析,评论次数、人次
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStart
     * @param createdDateTimeEnd
     * @return
     */
    List<String[]> commentLocationCount(String orgId, String productBaseId,
                                        String province, String city,
                                        DateTime createdDateTimeStart,
                                        DateTime createdDateTimeEnd);

}
