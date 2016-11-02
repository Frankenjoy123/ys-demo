package com.yunsoo.di.dao.repository.Impl;

import com.yunsoo.di.dao.entity.UserProfileLocationCountEntity;
import com.yunsoo.di.dao.entity.UserProfileTagCountEntity;
import com.yunsoo.di.dao.repository.UserProfileRepository;
import org.eclipse.jetty.util.StringUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   xiaowu
 * Created on:   2016/11/1
 * Descriptions:
 */
@Repository
public class UserProfileRepositoryImpl implements UserProfileRepository{

    @PersistenceContext(unitName = "di")
    private EntityManager entityManager;

    private static final String UNKNOWN="Unknown";

    /**
     * 用户扫码时间分析
     * @param orgId
     * @param startDateTime 为扫码时间
     * @param endDateTime 为扫码时间
     * @return
     */
    @Override
    public List<UserProfileTagCountEntity> queryUserProfileTimeUsage(String orgId, DateTime startDateTime, DateTime endDateTime) {
        HashMap<String, Object> parameters = new HashMap<>();

        String sql = "select hour(convert_tz(ev.event_datetime,'+00:00','+08:00')) as hourNum, count(distinct u.id) " +
                "from di_event ev " +
                "inner join di_user u on ev.ys_id = u.ys_id and ev.user_id = u.user_id and ev.org_id = u.org_id " +
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
            int hourNum = ((Integer)data[0]).intValue();
            int hourCount =((BigInteger)data[1]).intValue();
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

    @Override
    public List<UserProfileTagCountEntity> queryUserProfileDeviceUsage(String orgId) {
        HashMap<String, Object> parameters = new HashMap<>();

        String sql = "SELECT ua.os, count(1) FROM di.di_user u " +

                "left join di.di_event ev on u.latest_scan_id=ev.event_id  and ev.name='scan' "+

                "left join di.lu_user_agent ua on ev.user_agent_id=ua.id "+

                "where u.org_id = :orgId group by ua.os;";

        parameters.put("orgId", orgId);
        //bypass datetime 限制
        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List<Object[]> queryList = query.getResultList();
        HashMap<String , Integer> deviceCountMap=new LinkedHashMap<>();
        List<UserProfileTagCountEntity> list = new ArrayList<>();

        for(Object[] obj : queryList)
        {
            //合并null和unknown计数
            if (StringUtils.isEmpty((String) obj[0]) || ((String) obj[0]).equals(UNKNOWN)){
                int unknownCount=deviceCountMap.getOrDefault(UNKNOWN, 0);
                deviceCountMap.put(UNKNOWN,((BigInteger)obj[1]).intValue()+unknownCount);
            }
            else {
                deviceCountMap.put((String) obj[0],((BigInteger)obj[1]).intValue());
            }
        }


        return deviceCountMap.entrySet().stream().map(t -> {
            UserProfileTagCountEntity item = new UserProfileTagCountEntity();
            item.setTag(t.getKey());
            item.setCount(t.getValue());
            return item;
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserProfileTagCountEntity> queryUserProfileGenderUsage(String orgId) {
        HashMap<String, Object> parameters = new HashMap<>();

        String sql = "SELECT ifnull(sex, 2) as gender, count(1) FROM di.di_user " +
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
            int gender = ((BigDecimal)obj[0]).intValue();
            int count = ((BigInteger)obj[1]).intValue();
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

        String sql = "SELECT (tag.name) as area, count(1) FROM di.di_user u " +
                "left join lu_province_city pc on u.location_id = pc.id " +
                "left join lu_tag tag on pc.tag_id = tag.id " +
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
            int count = ((BigInteger)obj[1]).intValue();

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
                "from di_user u left JOIN  lu_province_city pc ON u.location_id=pc.id where u.org_id = :orgId " +
                "group by u.location_id";

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
            int count = ((BigInteger)obj[2]).intValue();

            UserProfileLocationCountEntity item = new UserProfileLocationCountEntity();
            item.setProvince(province);
            item.setCity(city);
            item.setCount(count);
            list.add(item);
        }
        return list;
    }
}
