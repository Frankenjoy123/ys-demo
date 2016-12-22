package com.yunsoo.data.service.repository.impl;


import com.yunsoo.data.service.entity.EMRUserEntity;
import com.yunsoo.data.service.entity.EMRUserProductEventStatistics;
import com.yunsoo.data.service.entity.UserProfileLocationCountEntity;
import com.yunsoo.data.service.entity.UserProfileTagCountEntity;
import com.yunsoo.data.service.repository.CustomEMRUserRepository;
import org.hibernate.annotations.common.util.StringHelper;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yqy09_000 on 5/31/2016.
 */
public class EMRUserRepositoryImpl implements CustomEMRUserRepository {

    @PersistenceContext(unitName = "master")
    private EntityManager entityManager;

    @Override
    public List<EMRUserEntity> findEventUsersFilterByScan(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd, Pageable pageable) {
        String sql = "SELECT distinct usr.* FROM emr_event ev inner join emr_user usr on ev.org_id = usr.org_id and (ev.user_id = usr.user_id and ev.ys_id = usr.ys_id)" +
                " where ev.name = 'scan' and ev.org_id = :orgId";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("orgId", orgId);
        if (!StringUtils.isEmpty(productBaseId)) {
            sql = sql + " and ev.product_base_id = :productBaseId";
            parameters.put("productBaseId", productBaseId);
        }
        if (!StringUtils.isEmpty(province)) {
            sql = sql + " and ev.province like :province";
            parameters.put("province", "%" + province + "%");
        }
        if (!org.springframework.util.StringUtils.isEmpty(city)) {
            sql = sql + " and ev.city like :city";
            parameters.put("city", "%" + city + "%");
        }
        if (createdDateTimeStart != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeStart.toString())) {
            sql = sql + " and ev.scan_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStart.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEnd != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeEnd.toString())) {
            sql = sql + " and ev.scan_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", createdDateTimeEnd.toString("yyyy-MM-dd"));
        }
        sql = sql + " order by usr.join_datetime desc";

        Query query = entityManager.createNativeQuery(sql, EMRUserEntity.class);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        if (pageable != null) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }

        List list = query.getResultList();
        return list;
    }

    @Override
    public int countEventUsersFilterByScan(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        String sql = "SELECT count(distinct usr.id) FROM emr_event ev inner join emr_user usr on (ev.user_id = usr.user_id and ev.ys_id = usr.ys_id) and ev.org_id = usr.org_id" +
                " where ev.name = 'scan' and ev.org_id = :orgId";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("orgId", orgId);
        if (!StringUtils.isEmpty(productBaseId)) {
            sql = sql + " and ev.product_base_id = :productBaseId";
            parameters.put("productBaseId", productBaseId);
        }
        if (!StringUtils.isEmpty(province)) {
            sql = sql + " and ev.province like :province";
            parameters.put("province", "%" + province + "%");
        }
        if (!org.springframework.util.StringUtils.isEmpty(city)) {
            sql = sql + " and ev.city like :city";
            parameters.put("city", "%" + city + "%");
        }
        if (createdDateTimeStart != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeStart.toString())) {
            sql = sql + " and ev.scan_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStart.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEnd != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeEnd.toString())) {
            sql = sql + " and ev.scan_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", createdDateTimeEnd.toString("yyyy-MM-dd"));
        }

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }

        Number value = (Number) query.getSingleResult();
        return value.intValue();
    }


    @Override
    public List<EMRUserProductEventStatistics> queryUserEventStatistics(String orgId, String userId, String ysId, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        HashMap<String, Object> parameters = new HashMap<>();

        String sql = "select ev.product_base_id, ev.product_name,ev.org_id, " +
                "sum(case when ev.name = 'scan' then 1 else 0 end) as scan_count, " +
                "sum(case when ev.name='draw' then 1 else 0 end) as draw_count, " +
                "sum(case when ev.name ='draw' and ev.is_priced = 1 then 1 else 0 end) as win_count, " +
                "sum(case when ev.name='draw' and ev.price_status_code in ('paid','submit') then 1 else 0 end) as reward_count, " +
                "sum(case when ev.name = 'comment' then 1 else 0 end) as comment_count, " +
                "sum(case when ev.name = 'share' then 1 else 0 end) as share_count, " +
                "sum(case when ev.name = 'store_url' then 1 else 0 end) as store_count " +
                "from emr_event ev where ev.org_id =:orgId";
        parameters.put("orgId", orgId);

        if (!StringHelper.isEmpty(userId)) {
            sql = sql +
                    " and ev.user_id = :userId";
            parameters.put("userId", userId);
        } else if (!StringHelper.isEmpty(ysId)) {
            sql = sql + " and ev.ys_id = :ysId";
            parameters.put("ysId", ysId);
        }

        if (createdDateTimeStart != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeStart.toString())) {
            sql = sql + " and ev.event_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStart.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEnd != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeEnd.toString())) {
            sql = sql + " and ev.event_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", createdDateTimeEnd.toString("yyyy-MM-dd"));
        }
        sql = sql + " group by ev.product_base_id";

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List<Object[]> queryList = query.getResultList();
        List<EMRUserProductEventStatistics> resultList = new ArrayList<>();
        for (Object[] data : queryList) {
            EMRUserProductEventStatistics item = new EMRUserProductEventStatistics();
            item.setProductBaseId((String) data[0]);
            item.setProductName((String) data[1]);
            item.setOrgId((String) data[2]);
            item.setScanCount(((Number) data[3]).intValue());
            item.setDrawCount(((Number) data[4]).intValue());
            item.setWinCount(((Number) data[5]).intValue());
            item.setRewardCount(((Number) data[6]).intValue());
            item.setCommentCount(((Number) data[7]).intValue());
            item.setShareCount(((Number) data[8]).intValue());
            item.setStoreCount(((Number) data[9]).intValue());
            resultList.add(item);
        }
        return resultList;
    }

    // region 用户属性分析

    /**
     * 用户扫码时间分析，可扩展为事件时间分析
     * @param orgId
     * @param startDateTime 为扫码时间
     * @param endDateTime 为扫码时间
     * @return
     */
    @Override
    public List<UserProfileTagCountEntity> queryUserProfileTimeUsage(String orgId, DateTime startDateTime, DateTime endDateTime) {
        HashMap<String, Object> parameters = new HashMap<>();

        String sql = "select hour(convert_tz(ev.event_datetime,'+00:00','+08:00')) as hourNum, count(distinct eu.id) " +
                "from emr_event ev " +
                "inner join emr_user eu on ev.ys_id = eu.ys_id and ev.user_id = eu.user_id and ev.org_id = eu.org_id " +
                "where ev.name='scan' and ev.org_id = :orgId";

        parameters.put("orgId", orgId);

        if (startDateTime != null) {
            sql = sql + " and ev.event_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", startDateTime.toString("yyyy-MM-dd HH:mm:ss"));
        }
        if (endDateTime != null) {
            sql = sql + " and ev.event_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", endDateTime.toString("yyyy-MM-dd HH:mm:ss"));
        }
        sql = sql + " group by hourNum";

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List<Object[]> queryList = query.getResultList();

        HashMap<String, Integer> timespanCount = new LinkedHashMap<>();
        timespanCount.put(UserProfileTagCountEntity.TimeSpan.T00_06, 0);
        timespanCount.put(UserProfileTagCountEntity.TimeSpan.T06_08, 0);
        timespanCount.put(UserProfileTagCountEntity.TimeSpan.T08_12, 0);
        timespanCount.put(UserProfileTagCountEntity.TimeSpan.T12_14, 0);
        timespanCount.put(UserProfileTagCountEntity.TimeSpan.T14_16, 0);
        timespanCount.put(UserProfileTagCountEntity.TimeSpan.T16_18, 0);
        timespanCount.put(UserProfileTagCountEntity.TimeSpan.T18_22, 0);
        timespanCount.put(UserProfileTagCountEntity.TimeSpan.T22_24, 0);

        for (Object[] data : queryList) {
            int hourNum = ((Number)data[0]).intValue();
            int hourCount =((Number)data[1]).intValue();
            if (hourNum < 6) {
                int count = timespanCount.get(UserProfileTagCountEntity.TimeSpan.T00_06);
                count += hourCount;
                timespanCount.put(UserProfileTagCountEntity.TimeSpan.T00_06, count);
            } else if (hourNum >= 6 && hourNum < 8) {
                int count = timespanCount.get(UserProfileTagCountEntity.TimeSpan.T06_08);
                count += hourCount;
                timespanCount.put(UserProfileTagCountEntity.TimeSpan.T06_08, count);
            } else if (hourNum >= 8 && hourNum < 12) {
                int count = timespanCount.get(UserProfileTagCountEntity.TimeSpan.T08_12);
                count += hourCount;
                timespanCount.put(UserProfileTagCountEntity.TimeSpan.T08_12, count);
            } else if (hourNum >= 12 && hourNum < 14) {
                int count = timespanCount.get(UserProfileTagCountEntity.TimeSpan.T12_14);
                count += hourCount;
                timespanCount.put(UserProfileTagCountEntity.TimeSpan.T12_14, count);
            } else if (hourNum >= 14 && hourNum < 16) {
                int count = timespanCount.get(UserProfileTagCountEntity.TimeSpan.T14_16);
                count += hourCount;
                timespanCount.put(UserProfileTagCountEntity.TimeSpan.T14_16, count);
            } else if (hourNum >= 16 && hourNum < 18) {
                int count = timespanCount.get(UserProfileTagCountEntity.TimeSpan.T16_18);
                count += hourCount;
                timespanCount.put(UserProfileTagCountEntity.TimeSpan.T16_18, count);
            } else if (hourNum >= 18 && hourNum < 22) {
                int count = timespanCount.get(UserProfileTagCountEntity.TimeSpan.T18_22);
                count += hourCount;
                timespanCount.put(UserProfileTagCountEntity.TimeSpan.T18_22, count);
            } else {
                int count = timespanCount.get(UserProfileTagCountEntity.TimeSpan.T22_24);
                count += hourCount;
                timespanCount.put(UserProfileTagCountEntity.TimeSpan.T22_24, count);
            }
        }
        return timespanCount.entrySet().stream().map(t -> {
            UserProfileTagCountEntity item = new UserProfileTagCountEntity();
            item.setTag(t.getKey());
            item.setCount(t.getValue());
            return item;
        }).collect(Collectors.toList());
    }

    /**
     * 用户设备分析
     * @param orgId
     * @return
     */
    @Override
    public List<UserProfileTagCountEntity> queryUserProfileDeviceUsage(String orgId) {
        HashMap<String, Object> parameters = new HashMap<>();

        String sql = "SELECT latest_event_device, count(1) FROM yunsoo2015DB.emr_user eu " +
                "where org_id = :orgId group by latest_event_device;";

        parameters.put("orgId", orgId);
        //bypass datetime 限制
        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List<Object[]> queryList = query.getResultList();
        List<UserProfileTagCountEntity> list = new ArrayList<>();
        for(Object[] obj : queryList)
        {
            UserProfileTagCountEntity item = new UserProfileTagCountEntity();
            item.setCount(((Number)obj[1]).intValue());
            item.setTag((String) obj[0]);
            list.add(item);
        }
        return list;
    }

    /**
     * 用户性别分析
     * @param orgId
     * @return
     */
    @Override
    public List<UserProfileTagCountEntity> queryUserProfileGenderUsage(String orgId) {
        HashMap<String, Object> parameters = new HashMap<>();

        String sql = "SELECT ifnull(sex, 2) as gender, count(1) FROM yunsoo2015DB.emr_user " +
                "where org_id = :orgId group by gender";

        parameters.put("orgId", orgId);
        //bypass datetime 限制
        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List<Object[]> queryList = query.getResultList();
        List<UserProfileTagCountEntity> list = new ArrayList<>();
        for(Object[] obj : queryList)
        {
            int gender = ((Number)obj[0]).intValue();
            int count = ((Number)obj[1]).intValue();
            String genderString = null;
            switch (gender)
            {
                case 0:
                    genderString = "男";
                    break;
                case 1:
                    genderString = "女";
                    break;
                default:
                    genderString = "未知";
                    break;
            }

            UserProfileTagCountEntity item = new UserProfileTagCountEntity();
            item.setCount(count);
            item.setTag(genderString);
            list.add(item);
        }
        return list;
    }

    /**
     * 用户城市分布分析
     * @param orgId
     * @return
     */
    @Override
    public List<UserProfileTagCountEntity> queryUserProfileAreaReport(String orgId) {
        HashMap<String, Object> parameters = new HashMap<>();

        String sql = "SELECT (tag.name) as area, count(1) FROM yunsoo2015DB.emr_user eu " +
                "left join lu_province_city lu on eu.city = lu.city " +
                "left join lu_tag tag on lu.tag_id = tag.id " +
                "where org_id = :orgId group by area;";

        parameters.put("orgId", orgId);
        //bypass datetime 限制
        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List<Object[]> queryList = query.getResultList();
        List<UserProfileTagCountEntity> list = new ArrayList<>();
        for(Object[] obj : queryList)
        {
            String tagName = obj[0] == null ? "其他地区(港澳台，国外等)" : (String)obj[0];
            int count = ((Number)obj[1]).intValue();

            UserProfileTagCountEntity item = new UserProfileTagCountEntity();
            item.setCount(count);
            item.setTag(tagName);
            list.add(item);
        }
        return list;
    }

    @Override
    public List<UserProfileLocationCountEntity> queryUserProfileLocationReport(String orgId) {
        HashMap<String, Object> parameters = new HashMap<>();
        String sql = "select case when province is null or province ='' then '未公开省份'  else  province END as provinceA, case when city is null or city = '' then '未公开城市' else city END as cityA, count(1) " +
                "from emr_user where org_id = :orgId " +
                "group by provinceA, cityA";

        String sqlProvince = "select case when province is null or province ='' then '未公开省份'  else  province END as provinceA,  count(1) " +
                "from emr_user where org_id = :orgId " +
                "group by provinceA";

        parameters.put("orgId", orgId);
        //bypass datetime 限制
        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List<Object[]> queryList = query.getResultList();
        List<UserProfileLocationCountEntity> list = new ArrayList<>();
        for(Object[] obj : queryList)
        {
            String province = (String)obj[0];
            String city = (String)obj[1];
            int count = ((Number)obj[2]).intValue();

            UserProfileLocationCountEntity item = new UserProfileLocationCountEntity();
            item.setProvince(province);
            item.setCity(city);
            item.setCount(count);
            list.add(item);
        }
        return list;
    }


    // endregion
}
