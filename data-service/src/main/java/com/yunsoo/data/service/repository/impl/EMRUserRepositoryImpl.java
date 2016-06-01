package com.yunsoo.data.service.repository.impl;


import com.amazonaws.util.StringUtils;
import com.yunsoo.data.service.entity.EMRUserEntity;
import com.yunsoo.data.service.repository.CustomEMRUserRepository;

import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yqy09_000 on 5/31/2016.
 */
public class EMRUserRepositoryImpl implements CustomEMRUserRepository {

    @PersistenceContext(unitName = "master")
    private EntityManager entityManager;

    @Override
    public List<EMRUserEntity> findEventUsersFilterByScan(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd, Pageable pageable) {
        String sql = "SELECT distinct usr.* FROM emr_event ev inner join emr_user usr on ev.org_id = usr.org_id and (ev.user_id = usr.user_id or ev.ys_id = usr.ys_id)"  +
                " where ev.name = 'scan' and ev.org_id = :orgId";

        HashMap<String, Object> parameters = new HashMap<>();
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
        String sql = "SELECT count(distinct usr.id) FROM emr_event ev inner join emr_user usr on (ev.user_id = usr.user_id or ev.ys_id = usr.ys_id) and ev.org_id = usr.org_id" +
                " where ev.name = 'scan' and ev.org_id = :orgId";

        HashMap<String, Object> parameters = new HashMap<>();
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

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }

        BigInteger value = (BigInteger)query.getSingleResult();
        return value.intValue();
    }
}
