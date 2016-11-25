package com.yunsoo.di.dao.repository.Impl;

import com.yunsoo.di.dao.entity.MarketUserLocationAnalysisEntity;
import com.yunsoo.di.dao.entity.UserProfileLocationCountEntity;
import com.yunsoo.di.dao.entity.UserProfileTagCountEntity;
import com.yunsoo.di.dao.repository.MarketUserRepository;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
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
 * Created by:   xiaowu
 * Created on:   2016/11/2
 * Descriptions:
 */
@Repository
public class MarketUserRepositoryImpl implements MarketUserRepository {

    @PersistenceContext(unitName = "di")
    private EntityManager entityManager;

    private static final String UNKNOWN="Unknown";

    @Override
    public List<UserProfileTagCountEntity> queryMarketUserAreaAnalysis(String orgId, DateTime startTime, DateTime endTime, String marketingId) {
        HashMap<String, Object> parameters = new HashMap<>();

        String sql = "SELECT (tag.name) as area, count(1) FROM di.mkt_draw_record dr " +
                "left join di.di_event e on dr.scan_record_id = e.event_id and e.name='scan' "+
                "left join lu_province_city pc on e.location_id = pc.id " +
                "left join lu_tag tag on pc.tag_id = tag.id " +
                "where org_id = :orgId";

        parameters.put("orgId", orgId);
        if (startTime != null) {
            sql = sql + " and e.event_datetime >=:startTime";
            parameters.put("startTime", startTime.toString("yyyy-MM-dd HH:mm:ss"));
        }
        if (endTime != null) {
            sql = sql + " and e.event_datetime <=:endTime";
            parameters.put("endTime", endTime.toString("yyyy-MM-dd HH:mm:ss"));
        }
        if (marketingId !=null){
            sql = sql + " and dr.marketing_id =:marketingId";
            parameters.put("marketingId", marketingId);
        }

        sql = sql + " group by area ORDER BY tag.id;";


        //bypass datetime 限制
        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List<Object[]> queryList = query.getResultList();
        List<UserProfileTagCountEntity> list = new ArrayList<>();
        for(Object[] obj : queryList)
        {
            String tagName = (String)obj[0];
            int count = ((Number)obj[1]).intValue();
            UserProfileTagCountEntity item = new UserProfileTagCountEntity();
            item.setCount(count);
            item.setTag(tagName);
            list.add(item);
        }
        return list;
    }


    @Override
    public List<UserProfileTagCountEntity> queryMarketUserDeviceAnalysis(String orgId, DateTime startTime, DateTime endTime, String marketingId) {

        HashMap<String, Object> parameters = new HashMap<>();

        String sql = "SELECT ua.os, count(1) FROM di.mkt_draw_record dr " +

                "left join di.di_event e on dr.scan_record_id = e.event_id and e.name='scan' "+

                "left join di.lu_user_agent ua on e.user_agent_id=ua.id "+

                "where org_id = :orgId ";

        parameters.put("orgId", orgId);
        if (startTime != null) {
            sql = sql + " and e.event_datetime >=:startTime";
            parameters.put("startTime", startTime.toString("yyyy-MM-dd HH:mm:ss"));
        }
        if (endTime != null) {
            sql = sql + " and e.event_datetime <=:endTime";
            parameters.put("endTime", endTime.toString("yyyy-MM-dd HH:mm:ss"));
        }
        if (marketingId !=null){
            sql = sql + " and dr.marketing_id =:marketingId";
            parameters.put("marketingId", marketingId);
        }

        sql = sql + " group by ua.os;";

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
                deviceCountMap.put(UNKNOWN,((Number)obj[1]).intValue()+unknownCount);
            }
            else {
                deviceCountMap.put((String) obj[0],((Number)obj[1]).intValue());
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
    public List<UserProfileTagCountEntity> queryMarketUserGenderAnalysis(String orgId, DateTime startTime, DateTime endTime, String marketingId) {
        HashMap<String, Object> parameters = new HashMap<>();

        String sql = "SELECT ifnull(sex, 2) as gender, count(1) FROM di.mkt_draw_record dr " +
                "inner join di.di_event e on e.event_id=dr.scan_record_id and e.name='scan' "+
                "inner JOIN di.di_user u ON e.user_id=u.user_id and e.ys_id=u.ys_id and e.org_id=u.org_id "+
                "where u.org_id = :orgId ";

        parameters.put("orgId", orgId);
        if (startTime != null) {
            sql = sql + " and dr.created_datetime >=:startTime";
            parameters.put("startTime", startTime.toString("yyyy-MM-dd HH:mm:ss"));
        }
        if (endTime != null) {
            sql = sql + " and dr.created_datetime <=:endTime";
            parameters.put("endTime", endTime.toString("yyyy-MM-dd HH:mm:ss"));
        }
        if (marketingId !=null){
            sql = sql + " and dr.marketing_id =:marketingId";
            parameters.put("marketingId", marketingId);
        }

        sql = sql + " group by gender;";
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

    @Override
    public List<UserProfileTagCountEntity> queryMarketUserUsageAnalysis(String orgId, DateTime startTime, DateTime endTime, String marketingId) {
        HashMap<String, Object> parameters = new HashMap<>();

        String sql = "select hour(convert_tz(dr.created_datetime,'+00:00','+08:00')) as hourNum, count(1) " +
                "FROM di.mkt_draw_record dr " +
                "inner join di.di_event e on dr.scan_record_id = e.event_id and e.name='scan' "+
                "where e.org_id = :orgId";

        parameters.put("orgId", orgId);

        if (startTime != null) {
            sql = sql + " and dr.created_datetime >=:startTime";
            parameters.put("startTime", startTime.toString("yyyy-MM-dd HH:mm:ss"));
        }
        if (endTime != null) {
            sql = sql + " and dr.created_datetime <=:endTime";
            parameters.put("endTime", endTime.toString("yyyy-MM-dd HH:mm:ss"));
        }
        if (marketingId !=null){
            sql = sql + " and dr.marketing_id =:marketingId";
            parameters.put("marketingId", marketingId);
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

    @Override
    public List<UserProfileLocationCountEntity> queryMarketUserLocationAnalysis(String orgId, DateTime startTime, DateTime endTime, String marketingId) {
        HashMap<String, Object> parameters = new HashMap<>();
        String sql = "select case when province is null or province ='' then '未公开省份'  else  province END as provinceA, case when city is null or city = '' then '未公开城市' else city END as cityA, count(1) " +
                "FROM di.mkt_draw_record dr " +
                "left join di.di_event e on dr.scan_record_id = e.event_id and e.name='scan' "+
                "left JOIN  lu_province_city pc ON e.location_id=pc.id where e.org_id = :orgId " ;

        parameters.put("orgId", orgId);
        if (startTime != null) {
            sql = sql + " and dr.created_datetime >=:startTime";
            parameters.put("startTime", startTime.toString("yyyy-MM-dd HH:mm:ss"));
        }
        if (endTime != null) {
            sql = sql + " and dr.created_datetime <=:endTime";
            parameters.put("endTime", endTime.toString("yyyy-MM-dd HH:mm:ss"));
        }
        if (marketingId !=null){
            sql = sql + " and dr.marketing_id =:marketingId";
            parameters.put("marketingId", marketingId);
        }
        sql = sql + " group by e.location_id";


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

    @Override
    public List<MarketUserLocationAnalysisEntity> queryRewardLocationReport(String marketingId) {
        String sql = "select lu.province, lu.city, count(1) from di.mkt_draw_record dr LEFT JOIN di.di_event ev ON dr.scan_record_id=ev.event_id  and ev.name='scan' "
                +"left join lu_province_city lu on ev.location_id = lu.id " +
                " where dr.isPrized = 1 and dr.marketing_id =:marketingId " +
                " group by lu.id ";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("marketingId", marketingId);
        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List<Object[]> data = query.getResultList();
        List<MarketUserLocationAnalysisEntity> list = new ArrayList<>();
        for (Object[] item : data) {
            MarketUserLocationAnalysisEntity entity = new MarketUserLocationAnalysisEntity();
            entity.setProvince(item[0] == null ? "未知地区" : (String) item[0]);
            entity.setCity(item[1] == null ? "未知地区" : (String) item[1]);
            entity.setCount(((Number) item[2]).intValue());
            list.add(entity);
        }
        return list;
    }


}
