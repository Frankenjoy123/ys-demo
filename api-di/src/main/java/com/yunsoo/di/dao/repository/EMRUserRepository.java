package com.yunsoo.di.dao.repository;

import com.yunsoo.di.dao.entity.EMRUserEntity;
import com.yunsoo.di.dao.entity.EMRUserProductEventStatistics;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by xiaowu on 2016/11/8.
 */
public interface EMRUserRepository {


    /**
     *  消费者行为分析，扫描用户列表
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStart
     * @param createdDateTimeEnd
     * @param wxUser 判断是否为微信用户
     * @param pageable
     * @return
     */
    List<EMRUserEntity> findEventUsersFilterByScan(String orgId,
                                                   String productBaseId,
                                                   String province, String city,
                                                   DateTime createdDateTimeStart,
                                                   DateTime createdDateTimeEnd,
                                                   Boolean wxUser,
                                                   Pageable pageable);

    /**
     *  消费者行为分析，抽奖用户列表
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStart
     * @param createdDateTimeEnd
     * @param wxUser 判断是否为微信用户
     * @param pageable
     * @return
     */
    List<EMRUserEntity> findEventUsersFilterByDraw(String orgId,
                                                   String productBaseId,
                                                   String province, String city,
                                                   DateTime createdDateTimeStart,
                                                   DateTime createdDateTimeEnd,
                                                   Boolean wxUser,
                                                   Pageable pageable);

    /**
     *  消费者行为分析，中奖用户列表
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStart
     * @param createdDateTimeEnd
     * @param wxUser 判断是否为微信用户
     * @param pageable
     * @return
     */
    List<EMRUserEntity> findEventUsersFilterByWin(String orgId,
                                                   String productBaseId,
                                                   String province, String city,
                                                   DateTime createdDateTimeStart,
                                                   DateTime createdDateTimeEnd,
                                                   Boolean wxUser,
                                                   Pageable pageable);
    /**
     *  消费者行为分析，兑奖用户列表
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStart
     * @param createdDateTimeEnd
     * @param wxUser 判断是否为微信用户
     * @param pageable
     * @return
     */
    List<EMRUserEntity> findEventUsersFilterByReward(String orgId,
                                                    String productBaseId,
                                                    String province, String city,
                                                    DateTime createdDateTimeStart,
                                                    DateTime createdDateTimeEnd,
                                                    Boolean wxUser,
                                                    Pageable pageable);

    int countUsersByFilter (String orgId,Boolean sex,  String phone, String name, String province, String city, Integer ageStart,  Integer ageEnd,
                            DateTime createdDateTimeStart, DateTime createdDateTimeEnd, List<String> userTags,
                            boolean userTagsIgnored, Boolean wxUser);

    List<EMRUserEntity> findUsersByFilter( String orgId,Boolean sex,  String phone, String name, String province, String city, Integer ageStart,  Integer ageEnd,
                                        DateTime createdDateTimeStart, DateTime createdDateTimeEnd, List<String> userTags,
                                        boolean userTagsIgnored, Boolean wxUser, Pageable pageable);

    EMRUserEntity getUser( String orgId,  String userId,  String ysId);

    List<EMRUserEntity> findEventUsersFilterByWX(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStartTo, DateTime createdDateTimeEndTo, Pageable pageable);
    int countUsersByFilterByWX (String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStartTo, DateTime createdDateTimeEndTo);

    List<EMRUserProductEventStatistics> queryUserEventStatistics(String orgId, String userId, String ysId, DateTime createdDateTimeStartTo, DateTime createdDateTimeEndTo);
}
