package com.yunsoo.data.service.repository.impl;

import com.yunsoo.data.service.entity.EMREventEntity;
import com.yunsoo.data.service.entity.MarketUserLocationAnalysisEntity;
import com.yunsoo.data.service.entity.ScanRecordLocationAnalysisEntity;
import com.yunsoo.data.service.repository.CustomEMREventRepository;
import org.joda.time.DateTime;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yqy09_000 on 5/31/2016.
 */
public class EMREventRepositoryImpl implements CustomEMREventRepository {

    @PersistenceContext(unitName = "master")
    private EntityManager entityManager;

    @Override
    public int[] scanCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return query("scan", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 0);
    }

    @Override
    public int[] wxCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return query("scan", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 1);
    }

    @Override
    public int[] drawCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return query("draw", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 2);
    }

    @Override
    public int[] winCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return query("draw", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 3);
    }


    @Override
    public int[] rewardCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return query("draw", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 4);
    }

    @Override
    public int[] shareCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return query("share", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 0);
    }

    @Override
    public int[] storeUrlCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return query("store_url", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 0);
    }

    @Override
    public int[] commentCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return query("comment", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 0);
    }

    @Override
    public List<String[]> scanDailyCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return queryDailyEventCount("scan", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 0);
    }

    @Override
    public List<String[]> shareDailyCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return queryDailyEventCount("share", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 0);
    }

    @Override
    public List<String[]> storeUrlDailyCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return queryDailyEventCount("store_url", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 0);
    }

    @Override
    public List<String[]> commentDailyCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return queryDailyEventCount("comment", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 0);
    }


    //region 用于统计比如全国数据，排除任何group by, 次数，人数
    @Override
    public List<int[]> eventLocationCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        int[] scanCount = queryEventCount("scan", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 0);
        int[] shareCount = queryEventCount("share", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 0);
        int[] storeUrlCount = queryEventCount("store_url", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 0);
        int[] commentCount = queryEventCount("comment", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 0);
        List<int[]> list = new ArrayList<>();
        list.add(scanCount);
        list.add(shareCount);
        list.add(storeUrlCount);
        list.add((commentCount));
        return list;
    }

    private int[] queryEventCount(String eventName, String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd, int i) {
        String sql = "select count(1), count(distinct u.id) from emr_event ev inner join emr_user u on ev.org_id = u.org_id and (ev.user_id = u.user_id and ev.ys_id = u.ys_id) where ev.name= :eventName" +
                " and ev.org_id =:orgId";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("eventName", eventName);
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
            sql = sql + " and ev.event_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStart.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEnd != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeEnd.toString())) {
            sql = sql + " and ev.event_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", createdDateTimeEnd.toString("yyyy-MM-dd"));
        }

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        Object[] data = (Object[]) query.getSingleResult();
        int[] array = new int[2];
        array[0] = ((Number) data[0]).intValue();
        array[1] = ((Number) data[1]).intValue();
        return array;
    }
    // endregion

    @Override
    public List<String[]> scanLocationCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return queryEventLocationCount("scan", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 0);
    }

    @Override
    public List<String[]> shareLocationCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return queryEventLocationCount("share", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 0);
    }

    @Override
    public List<String[]> storeUrlLocationCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return queryEventLocationCount("store_url", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 0);
    }

    @Override
    public List<String[]> commentLocationCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return queryEventLocationCount("comment", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 0);
    }

    @Override
    public List<ScanRecordLocationAnalysisEntity> consumerLocationCount(String orgId, String productBaseId, String batchId, DateTime startDateTime, DateTime endDateTime) {
        String sql = "select case when ev.province is null or ev.province ='' then '未公开省份'  else  ev.province END as provinceA, case when ev.city is null or ev.city = '' then '未公开城市' else ev.city END as cityA,ev.product_base_id as product, count(1), count(distinct ev.user_id, ev.ys_id) from emr_event ev  where ev.name= :eventName" +
                " and ev.org_id =:orgId";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("orgId", orgId);
        parameters.put("eventName", "scan");
        if (!StringUtils.isEmpty(productBaseId)) {
            sql = sql + " and ev.product_base_id = :productBaseId";
            parameters.put("productBaseId", productBaseId);
        }
        if (startDateTime != null && !org.springframework.util.StringUtils.isEmpty(startDateTime.toString())) {
            sql = sql + " and ev.event_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", startDateTime.toString("yyyy-MM-dd HH:mm:ss"));
        }
        if (endDateTime != null && !org.springframework.util.StringUtils.isEmpty(endDateTime.toString())) {
            sql = sql + " and ev.event_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", endDateTime.toString("yyyy-MM-dd HH:mm:ss"));
        }
        sql = sql + " group by provinceA, cityA";

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List<Object[]> data = query.getResultList();
        List<ScanRecordLocationAnalysisEntity> list = new ArrayList<>();
        for (Object[] d : data) {
            ScanRecordLocationAnalysisEntity entity = new ScanRecordLocationAnalysisEntity();
            entity.setProvince((String) d[0]);
            entity.setCity((String) d[1]);
            entity.setProductBaseId((String) d[2]);
            entity.setPv(((Number) d[3]).intValue());
            entity.setUv(((Number) d[4]).intValue());
            list.add(entity);
        }
        return list;
    }

    @Override
    public EMREventEntity recentlyConsumptionEvent(String orgId, String userId, String ysId) {

        String sql = "select ev.* from emr_event ev inner join (select min(id) as first from emr_event where name ='scan' group by product_key) as ev2" +
                " on ev.id = ev2.first  where ev.org_id =:orgId and ev.user_id=:userId and ev.ys_id=:ysId order by ev.id desc limit 1";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("orgId", orgId);
        parameters.put("userId", userId == null ? "" : userId);
        parameters.put("ysId", ysId == null ? "" : ysId);

        Query query = entityManager.createNativeQuery(sql, EMREventEntity.class);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List list = query.getResultList();
        if (list.isEmpty()) return null;
        return (EMREventEntity) list.get(0);
    }

    @Override
    public int periodConsumptionCount(String orgId, String userId, String ysId, DateTime eventDateTimeStart, DateTime eventDateTimeEnd) {
        String sql = "select count(1) from emr_event ev inner join (select min(id) as first from emr_event where name ='scan' group by product_key) as ev2" +
                " on ev.id = ev2.first  where ev.org_id =:orgId and ev.user_id=:userId and ev.ys_id=:ysId" +
                " and ev.event_datetime <:eventDateTimeEnd and ev.event_datetime >=:eventDateTimeStart";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("orgId", orgId);
        parameters.put("userId", userId == null ? "" : userId);
        parameters.put("ysId", ysId == null ? "" : ysId);
        parameters.put("eventDateTimeStart", eventDateTimeStart.toString("yyyy-MM-dd HH:mm:ss"));
        parameters.put("eventDateTimeEnd", eventDateTimeEnd.toString("yyyy-MM-dd HH:mm:ss"));

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        Number value = (Number) query.getSingleResult();

        return value.intValue();
    }


    private int[] query(String action, String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd, int level) {
        String sql = "select count(1), count(distinct u.id) from emr_event ev inner join emr_user u on ev.org_id = u.org_id and (ev.user_id = u.user_id and ev.ys_id = u.ys_id) where ev.name= :eventName" +
                " and ev.org_id =:orgId ";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("eventName", action);
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
            sql = sql + " and ev.event_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStart.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEnd != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeEnd.toString())) {
            sql = sql + " and ev.event_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", createdDateTimeEnd.toString("yyyy-MM-dd"));
        }

        if (level > 0) {
            sql = sql + " and u.wx_openid is not null";
        }

        if (level > 1) {
            sql = sql + " and ev.is_priced is not null";
        }
        if (level > 2) {
            sql = sql + " and ev.is_priced = 1";
        }

        if (level > 3) {
            sql = sql + " and ev.price_status_code in ('submit','paid')";
        }

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        Object[] data = (Object[]) query.getSingleResult();
        int[] array = new int[2];
        array[0] = ((Number) data[0]).intValue();
        array[1] = ((Number) data[1]).intValue();
        return array;
    }

    private List<String[]> queryDailyEventCount(String action, String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd, int level) {
        String sql = "select DATE_FORMAT(event_datetime,'%Y-%m-%d'),count(1), count(distinct u.id) from emr_event ev inner join emr_user u on ev.org_id = u.org_id and (ev.user_id = u.user_id and ev.ys_id = u.ys_id) where ev.name= :eventName" +
                " and ev.org_id =:orgId ";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("eventName", action);
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
            sql = sql + " and ev.event_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStart.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEnd != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeEnd.toString())) {
            sql = sql + " and ev.event_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", createdDateTimeEnd.toString("yyyy-MM-dd"));
        }

        if (level > 0) {
            sql = sql + " and u.wx_openid is not null";
        }

        if (level > 1) {
            sql = sql + " and ev.is_priced is not null";
        }
        if (level > 2) {
            sql = sql + " and ev.is_priced = 1";
        }

        if (level > 3) {
            sql = sql + " and ev.price_status_code in ('submit','paid')";
        }
        sql = sql + " group by DATE_FORMAT(event_datetime,'%Y-%m-%d')";

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List<Object[]> data = query.getResultList();
        List<String[]> list = new ArrayList<>();
        for (Object[] item : data) {
            String[] temp = new String[3];
            temp[0] = (String) item[0];
            temp[1] = (String) item[1].toString();
            temp[2] = (String) item[2].toString();
            list.add(temp);
        }
        return list;
    }

    private List<String[]> queryEventLocationCount(String action, String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd, int level) {
        String sql = "select case when ev.province is null or ev.province ='' then '未公开省份'  else  ev.province END as provinceA, case when ev.city is null or ev.city = '' then '未公开城市' else ev.city END as cityA,ev.product_base_id as product, count(1), count(distinct u.id) from emr_event ev inner join emr_user u on ev.org_id = u.org_id and (ev.user_id = u.user_id and ev.ys_id = u.ys_id) where ev.name= :eventName" +
                " and ev.org_id =:orgId";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("eventName", action);
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
            sql = sql + " and ev.event_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStart.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEnd != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeEnd.toString())) {
            sql = sql + " and ev.event_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", createdDateTimeEnd.toString("yyyy-MM-dd"));
        }

        if (level > 0) {
            sql = sql + " and u.wx_openid is not null";
        }

        if (level > 1) {
            sql = sql + " and ev.is_priced is not null";
        }
        if (level > 2) {
            sql = sql + " and ev.is_priced = 1";
        }

        if (level > 3) {
            sql = sql + " and ev.price_status_code in ('submit','paid')";
        }

        sql = sql + " group by provinceA, cityA";

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List<Object[]> data = query.getResultList();
        List<String[]> list = new ArrayList<>();
        for (Object[] item : data) {
            String[] temp = new String[5];
            temp[0] = (String) item[0];
            temp[1] = (String) item[1];
            temp[2] = (String) item[2];
            temp[3] = (String) item[3].toString();
            temp[4] = (String) item[4].toString();
            list.add(temp);
        }
        return list;
    }

    @Override
    public List<MarketUserLocationAnalysisEntity> queryRewardLocationReport(String marketingId) {
        String sql = "select lu.province, lu.city, count(1) from emr_event ev left join lu_province_city lu on ev.province = lu.province and ev.city = lu.city " +
                " where ev.name='draw' and ev.is_priced = 1 and ev.marketing_id =:marketingId" +
                " group by lu.province, lu.city ";

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
