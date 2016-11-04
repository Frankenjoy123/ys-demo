package com.yunsoo.di.dao.repository.Impl;

import com.yunsoo.di.dao.repository.CustomerEventRepository;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by:   xiaowu
 * Created on:   2016/11/3
 * Descriptions:
 */
@Repository
public class CustomerEventRepositoryImpl implements CustomerEventRepository{

    @PersistenceContext(unitName = "di")
    private EntityManager entityManager;

    private static final String WEXIN="wexin";

    @Override
    public int[] scanCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd , String scanSource) {
        return scanQuery( orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, scanSource);
    }

    @Override
    public int[] wxCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return scanQuery( orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, WEXIN);
    }

    @Override
    public int[] drawCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd ,String scanSource) {
        return  drawQuery( orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, scanSource , 2);
    }

    @Override
    public int[] winCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd,String scanSource) {
        return drawQuery( orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, scanSource , 3);
    }

    @Override
    public int[] rewardCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd, String scanSource) {
        return prizedQuery(orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, scanSource);
    }


    @Override
    public int[] shareCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return new int[0];
    }

    @Override
    public int[] storeUrlCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return new int[0];
    }

    @Override
    public int[] commentCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return new int[0];
    }

    @Override
    public List<String[]> scanDailyCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return queryDailyScanCount( orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd);
    }


    @Override
    public List<String[]> shareDailyCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return queryDailyEventCount("share", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd);
    }

    @Override
    public List<String[]> storeUrlDailyCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return queryDailyEventCount("store_url", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd);
    }

    @Override
    public List<String[]> commentDailyCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return queryDailyEventCount("comment", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd);
    }

    @Override
    public List<int[]> eventLocationCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return null;
    }

    @Override
    public List<String[]> scanLocationCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return null;
    }

    @Override
    public List<String[]> shareLocationCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return null;
    }

    @Override
    public List<String[]> storeUrlLocationCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return null;
    }

    @Override
    public List<String[]> commentLocationCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return null;
    }

    private int[] scanQuery( String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd, String scanSource) {
        String sql = "select count(1), count(distinct u.id) from di.di_event ev inner join di.di_user u on ev.org_id = u.org_id and (ev.user_id = u.user_id and ev.ys_id = u.ys_id) where ev.name= :eventName " +
                " and ev.org_id =:orgId ";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("eventName", "scan");
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

        if (!StringUtils.isEmpty(scanSource)&& scanSource.equals(WEXIN)){
            sql = sql + " and u.wx_openid is not null";
        }

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        Object[] data = (Object[]) query.getSingleResult();
        int[] array = new int[2];
        array[0] = ((BigInteger) data[0]).intValue();
        array[1] = ((BigInteger) data[1]).intValue();
        return array;
    }

    private int[] drawQuery( String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd, String scanSource ,  int level) {
        String sql = "select count(1), count(distinct u.id) from di.mkt_draw_record dr left join di.di_event ev on dr.scan_record_id=ev.event_id  " +
                "inner join di.di_user u on ev.org_id = u.org_id and (ev.user_id = u.user_id and ev.ys_id = u.ys_id) where ev.org_id =:orgId  ";

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
            sql = sql + " and ev.event_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStart.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEnd != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeEnd.toString())) {
            sql = sql + " and ev.event_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", createdDateTimeEnd.toString("yyyy-MM-dd"));
        }

        if (!StringUtils.isEmpty(scanSource)&& scanSource.equals(WEXIN)){
            sql = sql + " and u.wx_openid is not null";
        }

        if (level > 1) {
            sql = sql + " and dr.isPrized is not null";
        }
        if (level > 2) {
            sql = sql + " and dr.isPrized = 1";
        }

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        Object[] data = (Object[]) query.getSingleResult();
        int[] array = new int[2];
        array[0] = ((BigInteger) data[0]).intValue();
        array[1] = ((BigInteger) data[1]).intValue();
        return array;
    }

    private int[] prizedQuery( String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd, String scanSource) {
        String sql = "select count(1), count(distinct u.id) from di.mkt_draw_record dr left join di.di_event ev on dr.scan_record_id=ev.event_id INNER JOIN di.mkt_draw_prize dp ON dr.id=dp.draw_record_id " +
                "inner join di.di_user u on ev.org_id = u.org_id and (ev.user_id = u.user_id and ev.ys_id = u.ys_id) where ev.org_id =:orgId  and dr.isPrized = 1 and dp.status_code in ('submit','paid') ";

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
            sql = sql + " and ev.event_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStart.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEnd != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeEnd.toString())) {
            sql = sql + " and ev.event_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", createdDateTimeEnd.toString("yyyy-MM-dd"));
        }

        if (!StringUtils.isEmpty(scanSource)&& scanSource.equals(WEXIN)){
            sql = sql + " and u.wx_openid is not null";
        }

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        Object[] data = (Object[]) query.getSingleResult();
        int[] array = new int[2];
        array[0] = ((BigInteger) data[0]).intValue();
        array[1] = ((BigInteger) data[1]).intValue();
        return array;
    }

    private List<String[]> queryDailyScanCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        String sql = "select DATE_FORMAT(ev.event_datetime,'%Y-%m-%d'),count(1), count(distinct u.id) " +
                "from di.di_event ev LEFT JOIN di.lu_province_city pc ON ev.location_id=pc.id "+
                "inner join di.di_user u on ev.org_id = u.org_id and (ev.user_id = u.user_id and ev.ys_id = u.ys_id) "+
                "where ev.org_id = :orgId ";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("orgId", orgId);
        if (!StringUtils.isEmpty(productBaseId)) {
            sql = sql + " and ev.product_base_id = :productBaseId";
            parameters.put("productBaseId", productBaseId);
        }
        if (!StringUtils.isEmpty(province)) {
            sql = sql + " and pc.province like :province";
            parameters.put("province", "%" + province + "%");
        }
        if (!org.springframework.util.StringUtils.isEmpty(city)) {
            sql = sql + " and pc.city like :city";
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
        sql = sql + " group by DATE_FORMAT(ev.event_datetime,'%Y-%m-%d');";

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


    private List<String[]> queryDailyEventCount(String eventName, String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        String sql = "select DATE_FORMAT(ue.created_datetime,'%Y-%m-%d'),count(1), count(distinct u.id) " +
                "from di.user_event ue left join di.di_event ev on ue.scan_record_id=ev.event_id "+
                "LEFT JOIN di.lu_province_city pc ON ev.location_id=pc.id "+
                "inner join di.di_user u on ev.org_id = u.org_id and (ev.user_id = u.user_id and ev.ys_id = u.ys_id) "+
                "where ev.org_id = :orgId and ue.type_code= :eventName ";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("eventName", eventName);
        parameters.put("orgId", orgId);
        if (!StringUtils.isEmpty(productBaseId)) {
            sql = sql + " and ev.product_base_id = :productBaseId";
            parameters.put("productBaseId", productBaseId);
        }
        if (!StringUtils.isEmpty(province)) {
            sql = sql + " and pc.province like :province";
            parameters.put("province", "%" + province + "%");
        }
        if (!org.springframework.util.StringUtils.isEmpty(city)) {
            sql = sql + " and pc.city like :city";
            parameters.put("city", "%" + city + "%");
        }
        if (createdDateTimeStart != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeStart.toString())) {
            sql = sql + " and ue.created_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStart.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEnd != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeEnd.toString())) {
            sql = sql + " and ue.created_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", createdDateTimeEnd.toString("yyyy-MM-dd"));
        }
        sql = sql + " group by DATE_FORMAT(ue.created_datetime,'%Y-%m-%d');";

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

}
