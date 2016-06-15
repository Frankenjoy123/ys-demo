package com.yunsoo.data.service.repository.impl;


import com.amazonaws.util.StringUtils;
import com.yunsoo.data.service.entity.EMRUserEntity;
import com.yunsoo.data.service.entity.EMRUserProductEventStatistics;
import com.yunsoo.data.service.repository.CustomEMRUserRepository;

import org.hibernate.annotations.common.util.StringHelper;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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


    @Override
    public List<EMRUserProductEventStatistics> queryUserEventStatistics(String orgId, String userId, String ysId, DateTime createdDateTimeStart, DateTime createdDateTimeEnd) {
        HashMap<String, Object> parameters = new HashMap<>();

        String sql = "select ev.product_base_id, ev.product_name,ev.org_id, " +
                "sum(case when ev.name = 'scan' then 1 else 0 end) as scan_count, " +
                "sum(case when ev.name='draw' then 1 else 0 end) as draw_count, " +
                "sum(case when ev.name ='draw' and ev.is_priced = 1 then 1 else 0 end) as win_count, " +
                "sum(case when ev.name='draw' and ev.price_status_code in ('paid','submit') then 1 else 0 end) as reward_count  from emr_event ev where ev.org_id =:orgId";
        parameters.put("orgId", orgId);

        if(!StringHelper.isEmpty(userId))
        {
            sql = sql +
                    " and ev.user_id = :userId";
            parameters.put("userId",userId);
        }else if(!StringHelper.isEmpty(ysId))
        {
            sql = sql + " and ev.ys_id = :ysId";
            parameters.put("ysId",ysId);
        }

        if (createdDateTimeStart != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeStart.toString())) {
            sql = sql + " and ev.scan_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStart.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEnd != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeEnd.toString())) {
            sql = sql + " and ev.scan_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", createdDateTimeEnd.toString("yyyy-MM-dd"));
        }
        sql = sql + " group by ev.product_base_id";

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List<Object[]> queryList =  query.getResultList();
        List<EMRUserProductEventStatistics> resultList = new ArrayList<>();
        for(Object[] data : queryList)
        {
            EMRUserProductEventStatistics item = new EMRUserProductEventStatistics();
            item.setProductBaseId((String)data[0]);
            item.setProductName((String) data[1]);
            item.setOrgId((String) data[2]);
            item.setScanCount(((BigDecimal) data[3]).intValue());
            item.setDrawCount(((BigDecimal) data[4]).intValue());
            item.setWinCount(((BigDecimal) data[5]).intValue());
            item.setRewardCount(((BigDecimal) data[6]).intValue());
            resultList.add(item);
        }
        return resultList;
    }


}
