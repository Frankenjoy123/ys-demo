package com.yunsoo.di.dao.repository.Impl;

import com.yunsoo.di.dao.repository.CustomerEventRepository;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

    @Override
    public int[] scanCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd , Boolean wxUser) {
        return scanQuery( orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, wxUser);
    }

    @Override
    public int[] drawCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd ,Boolean wxUser) {
        return  drawQuery( orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, wxUser , 2);
    }

    @Override
    public int[] winCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd,Boolean wxUser) {
        return drawQuery( orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, wxUser , 3);
    }

    @Override
    public int[] rewardCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd, Boolean wxUser) {
        return prizedQuery(orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, wxUser);
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
        String sql = "select DATE_FORMAT(c.created_datetime,'%Y-%m-%d') as comment_date,count(1), count(distinct u.id)  " +
                "from di.product_comments c " +
                "inner join di.di_event ev on c.scan_record_id=ev.event_id " +
                "LEFT JOIN di.lu_province_city pc ON ev.location_id=pc.id " +
                "inner join di.di_user u on ev.org_id = u.org_id and (ev.user_id = u.user_id and ev.ys_id = u.ys_id) " +
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
            sql = sql + " and c.created_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStart.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEnd != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeEnd.toString())) {
            sql = sql + " and c.created_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", createdDateTimeEnd.toString("yyyy-MM-dd"));
        }
        sql = sql + " group by comment_date;";

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

    @Override
    public List<int[]> eventLocationCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        int[] scanCount = queryScanCount(orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd);
        int[] shareCount = queryEventCount("share", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd);
        int[] storeUrlCount = queryEventCount("store_url", orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd);
        int[] commentCount = queryCommentCount(orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd);
        List<int[]> list = new ArrayList<>();
        list.add(scanCount);
        list.add(shareCount);
        list.add(storeUrlCount);
        list.add((commentCount));
        return list;
    }

    @Override
    public List<String[]> scanLocationCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return queryScanLocationCount(orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd);
    }

    @Override
    public List<String[]> shareLocationCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return queryEventLocationCount("share",orgId,productBaseId,province,city,createdDateTimeStart,createdDateTimeEnd);
    }

    @Override
    public List<String[]> storeUrlLocationCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return queryEventLocationCount("store_url",orgId,productBaseId,province,city,createdDateTimeStart,createdDateTimeEnd);
    }

    @Override
    public List<String[]> commentLocationCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        String sql = "select case when pc.province is null or pc.province ='' then '未公开省份'  else  pc.province END as provinceA, case when pc.city is null or pc.city = '' then '未公开城市' else pc.city END as cityA, count(1), count(distinct u.id) " +
                "from di.product_comments c INNER join di.di_event ev on c.scan_record_id=ev.event_id "+
                "LEFT JOIN di.lu_province_city pc ON ev.location_id=pc.id "+
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
            sql = sql + " and c.created_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStart.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEnd != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeEnd.toString())) {
            sql = sql + " and c.created_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", createdDateTimeEnd.toString("yyyy-MM-dd"));
        }

        sql = sql + " group by pc.id";

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List<Object[]> data = query.getResultList();
        List<String[]> list = new ArrayList<>();
        for (Object[] item : data) {
            String[] temp = new String[4];
            temp[0] = (String) item[0];
            temp[1] = (String) item[1];
            temp[2] = String.valueOf(item[2]) ;
            temp[3] = String.valueOf(item[3]) ;
            list.add(temp);
        }
        return list;
    }

    private int[] scanQuery( String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd, Boolean wxUser) {
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

        if (wxUser!=null && wxUser){
            sql = sql + " and u.wx_openid is not null";
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

    private int[] drawQuery( String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd, Boolean wxUser ,  int level) {
        String sql = "select count(1), count(distinct u.id) from di.mkt_draw_record dr left join di.di_event ev on dr.scan_record_id=ev.event_id  " +
                "inner join di.di_user u on ev.org_id = u.org_id and (ev.user_id = u.user_id and ev.ys_id = u.ys_id) where ev.org_id =:orgId  ";

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

        if (wxUser!=null && wxUser){
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
        array[0] = ((Number) data[0]).intValue();
        array[1] = ((Number) data[1]).intValue();
        return array;
    }

    private int[] prizedQuery( String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd, Boolean wxUser) {
        String sql = "select count(1), count(distinct u.id) from di.mkt_draw_record dr left join di.di_event ev on dr.scan_record_id=ev.event_id INNER JOIN di.mkt_draw_prize dp ON dr.id=dp.draw_record_id " +
                "inner join di.di_user u on ev.org_id = u.org_id and (ev.user_id = u.user_id and ev.ys_id = u.ys_id) where ev.org_id =:orgId  and dr.isPrized = 1 and dp.status_code in ('submit','paid') ";

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

        if (wxUser!=null && wxUser){
            sql = sql + " and u.wx_openid is not null";
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


    private int[] queryScanCount( String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        String sql = "select count(1), count(distinct u.id) " +
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

    private int[] queryEventCount(String eventName, String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {

        String sql = "select count(1), count(distinct u.id) " +
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

    private int[] queryCommentCount( String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {

        String sql = "select count(1), count(distinct u.id) " +
                "from di.product_comments c left join di.di_event ev on c.scan_record_id=ev.event_id "+
                "LEFT JOIN di.lu_province_city pc ON ev.location_id=pc.id "+
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
            sql = sql + " and c.created_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStart.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEnd != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeEnd.toString())) {
            sql = sql + " and c.created_datetime <=:createdDateTimeEnd";
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


    private List<String[]> queryScanLocationCount( String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        String sql = "select case when pc.province is null or pc.province ='' then '未公开省份'  else  pc.province END as provinceA, case when pc.city is null or pc.city = '' then '未公开城市' else pc.city END as cityA, count(1), count(distinct u.id) " +
                "from di.di_event ev "+
                "inner join di.di_user u on ev.org_id = u.org_id and (ev.user_id = u.user_id and ev.ys_id = u.ys_id) "+
                "LEFT JOIN di.lu_province_city pc ON ev.location_id=pc.id "+
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

        sql = sql + " group by pc.id";

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List<Object[]> data = query.getResultList();
        List<String[]> list = new ArrayList<>();
        for (Object[] item : data) {
            String[] temp = new String[4];
            temp[0] = (String) item[0];
            temp[1] = (String) item[1];
            temp[2] = String.valueOf(item[2]) ;
            temp[3] = String.valueOf(item[3]) ;
            list.add(temp);
        }
        return list;
    }


    private List<String[]> queryEventLocationCount(String action, String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {

        String sql = "select case when pc.province is null or pc.province ='' then '未公开省份'  else  pc.province END as provinceA, case when pc.city is null or pc.city = '' then '未公开城市' else pc.city END as cityA, count(1), count(distinct u.id) " +
                "from di.user_event ue left join di.di_event ev on ue.scan_record_id=ev.event_id "+
                "LEFT JOIN di.lu_province_city pc ON ev.location_id=pc.id "+
                "inner join di.di_user u on ev.org_id = u.org_id and (ev.user_id = u.user_id and ev.ys_id = u.ys_id) "+
                "where ev.org_id = :orgId and ue.type_code= :eventName ";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("eventName", action);
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

        sql = sql + " group by pc.id";

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List<Object[]> data = query.getResultList();
        List<String[]> list = new ArrayList<>();
        for (Object[] item : data) {
            String[] temp = new String[4];
            temp[0] = (String) item[0];
            temp[1] = (String) item[1];
            temp[2] = String.valueOf(item[2]) ;
            temp[3] = String.valueOf(item[3]) ;
            list.add(temp);
        }
        return list;
    }

}
