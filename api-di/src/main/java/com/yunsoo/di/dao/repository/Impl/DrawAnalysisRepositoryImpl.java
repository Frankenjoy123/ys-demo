package com.yunsoo.di.dao.repository.Impl;

import com.yunsoo.di.dao.entity.DrawReportEntity;
import com.yunsoo.di.dao.repository.DrawAnalysisRepository;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yqy09_000 on 2016/11/14.
 */
@Repository
public class DrawAnalysisRepositoryImpl implements DrawAnalysisRepository {

    @PersistenceContext(unitName = "di")
    private EntityManager entityManager;

    @Override
    public List<DrawReportEntity> getDrawReportBy(String orgId, String marketingId,boolean bypass, DateTime startDateTime, DateTime endDateTime) {
        HashMap<String, Object> parameters = new HashMap<>();
        String sql = "SELECT mdr.comments as 'draw_rule_name',mdr.id, sum(ifnull(ddp.count,0)) as count FROM mkt_draw_rule mdr \n" +
                "left join di_daily_draw_price ddp on mdr.id = ddp.draw_rule_id and ddp.draw_date >=:ds and ddp.draw_date <:de \n" +
                "where mdr.marketing_id = :marketing_id \n" +
                "group by mdr.id;";
        if(!bypass)
        {
            sql = "SELECT mdr.comments as 'draw_rule_name',mdr.id, sum(ifnull(ddp.count,0)) as count FROM mkt_draw_rule mdr inner join marketing mk on mk.id = mdr.marketing_id \n" +
                    "left join di_daily_draw_price ddp on mdr.id = ddp.draw_rule_id and ddp.draw_date >=:ds and ddp.draw_date <:de \n" +
                    "where mdr.marketing_id = :marketing_id and mk.org_id = :org_id \n" +
                    "group by mdr.id;";
            parameters.put("org_id",orgId);
        }

        parameters.put("ds", startDateTime.toString("yyyy-MM-dd"));
        parameters.put("de", endDateTime.toString("yyyy-MM-dd"));
        parameters.put("marketing_id",marketingId);
        Query query =  entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List<Object[]> data = query.getResultList();
        List<DrawReportEntity> list = new ArrayList<>();
        for (Object[] d : data) {
            DrawReportEntity entity = new DrawReportEntity();
            entity.setDrawRuleName((String) d[0]);
            entity.setId((String) d[1]);
            entity.setCount(((BigDecimal) d[2]).intValue());
            list.add(entity);
        }
        return list;
    }
}
