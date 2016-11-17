package com.yunsoo.di.dao.repository.Impl;

import com.yunsoo.di.dao.entity.RankUserEntity;
import com.yunsoo.di.dao.repository.RankRepository;
import com.yunsoo.di.dto.RankUserObject;
import org.springframework.data.domain.Pageable;
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
 * Created on:   2016/11/17
 * Descriptions:
 */
@Repository
public class RankRepositoryImpl implements RankRepository {

    @PersistenceContext(unitName = "di")
    private EntityManager entityManager;

    @Override
    public List<RankUserEntity> getRankUsers(String orgId, Integer limit, Integer threshold, String productBaseId, Pageable pageable) {

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("orgId", orgId);
        parameters.put("threshold",threshold);

        StringBuilder builder=new StringBuilder("select t.count , t.user_id , t.ys_id, t.org_id, u.name, pc.province , pc.city  from " +
                "(SELECT count(1) as count, up.user_id, up.ys_id, up.org_id " +
                "FROM di.di_user_product up where up.org_id=:orgId " );

        if (!StringUtils.isEmpty(productBaseId)){
            builder.append(" and up.product_base_id=:productBaseId");
            parameters.put("productBaseId",productBaseId);
        }

        builder.append(" group by up.user_id, up.ys_id, up.org_id  having count>=:threshold " +
                "order by count desc ");

        if (limit!=null){
            builder.append(" limit :limit");
            parameters.put("limit", limit);
        }

        builder.append(" ) as t ");

        builder.append(" inner join di.di_user u on u.user_id=t.user_id and u.ys_id=t.ys_id and u.org_id=:orgId " +
                "inner join di.di_event ev on u.latest_scan_id=ev.event_id and ev.name='scan' " +
                "inner join di.lu_province_city pc on pc.id=ev.location_id " +
                "order by count desc ");

        Query query = entityManager.createNativeQuery(builder.toString());
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        if (pageable != null) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }

        List<Object[]> queryList = query.getResultList();
        List<RankUserEntity> list = new ArrayList<>();

        for(Object[] obj : queryList)
        {
            RankUserEntity rankUserEntity=new RankUserEntity();
            rankUserEntity.setCount(((Number) obj[0]).intValue());
            rankUserEntity.setUserId((String) obj[1]);
            rankUserEntity.setYsId((String) obj[2]);
            rankUserEntity.setOrgId((String) obj[3]);
            rankUserEntity.setName((String) obj[4]);
            rankUserEntity.setProvince((String) obj[5]);
            rankUserEntity.setCity((String) obj[6]);
            list.add(rankUserEntity);
        }

        return list;

    }

    @Override
    public int getRankUsersCount(String orgId, Integer limit, Integer threshold, String productBaseId) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("orgId", orgId);
        parameters.put("threshold",threshold);

        StringBuilder builder=new StringBuilder("select count(*) from ("
                +"SELECT count(1) as count " +
                "FROM di.di_user_product up where up.org_id=:orgId " );

        if (!StringUtils.isEmpty(productBaseId)){
            builder.append(" and up.product_base_id=:productBaseId");
            parameters.put("productBaseId",productBaseId);
        }

        builder.append(" group by up.user_id, up.ys_id, up.org_id  having count>=:threshold " );

        if (limit!=null){
            builder.append(" limit :limit");
            parameters.put("limit", limit);
        }

        builder.append(") as t");

        Query query = entityManager.createNativeQuery(builder.toString());
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }

        Number number= (Number) query.getSingleResult();
        return number.intValue();
    }
}
