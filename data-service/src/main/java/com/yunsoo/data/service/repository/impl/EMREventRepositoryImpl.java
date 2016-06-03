package com.yunsoo.data.service.repository.impl;

import com.amazonaws.util.StringUtils;
import com.yunsoo.data.service.entity.EMREventEntity;
import com.yunsoo.data.service.entity.EMRUserEntity;
import com.yunsoo.data.service.repository.CustomEMREventRepository;
import com.yunsoo.data.service.repository.EMREventRepository;
import org.joda.time.DateTime;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.HashMap;

/**
 * Created by yqy09_000 on 5/31/2016.
 */
public class EMREventRepositoryImpl implements CustomEMREventRepository {

    @PersistenceContext(unitName = "master")
    private EntityManager entityManager;

    @Override
    public int[] scanCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
      return query("scan",orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 0);
    }
    @Override
    public int[] wxCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return query("scan",orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 1);
    }

    @Override
    public int[] drawCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return query("draw",orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 2);
    }

    @Override
    public int[] winCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return query("draw",orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 3);
    }



    @Override
    public int[] rewardCount(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        return query("draw",orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, 4);
    }

    private int[] query(String action, String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd, int level) {
        String sql = "select count(1), count(distinct u.id) from emr_event ev inner join emr_user u on ev.org_id = u.org_id and (ev.user_id = u.user_id or ev.ys_id = u.ys_id) where ev.name= :eventName" +
                " and ev.org_id =:orgId ";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("eventName", action);
        parameters.put("orgId", orgId);
        if (!StringUtils.isNullOrEmpty(productBaseId)) {
            sql = sql + " and ev.product_base_id = :productBaseId";
            parameters.put("productBaseId", productBaseId);
        }
        if (!StringUtils.isNullOrEmpty(province)) {
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
       Object[] data =  (Object[])query.getSingleResult();
       int[] array = new int[2];
        array[0] = ((BigInteger)data[0]).intValue();
        array[1] = ((BigInteger)data[1]).intValue();
        return array;
    }
}
