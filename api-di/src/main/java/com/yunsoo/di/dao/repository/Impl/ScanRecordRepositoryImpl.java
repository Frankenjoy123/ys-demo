package com.yunsoo.di.dao.repository.impl;

import com.yunsoo.di.dao.entity.ScanRecordLocationAnalysisEntity;
import com.yunsoo.di.dao.repository.ScanRecordRepository;
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
 * Created on:   2016/11/1
 * Descriptions:
 */
@Repository
public class ScanRecordRepositoryImpl implements ScanRecordRepository {

    @PersistenceContext(unitName = "di")
    private EntityManager entityManager;

    @Override
    public List<ScanRecordLocationAnalysisEntity> consumerLocationCount(String orgId, String productBaseId, String batchId, DateTime startDateTime, DateTime endDateTime) {
        String sql = "select case when pc.province is null or pc.province ='' then '未公开省份'  else  pc.province END as provinceA, case when pc.city is null or pc.city = '' then '未公开城市' else pc.city END as cityA,ev.product_base_id as product, count(1), count(distinct ev.user_id, ev.ys_id) from di_event ev  LEFT JOIN lu_province_city pc ON ev.location_id=pc.id where ev.name= :eventName" +
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
        sql = sql + " group by location_id";

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
            entity.setProductBaseId(productBaseId);
            entity.setPv(((BigInteger) d[3]).intValue());
            entity.setUv(((BigInteger) d[4]).intValue());
            list.add(entity);
        }
        return list;
    }
}

