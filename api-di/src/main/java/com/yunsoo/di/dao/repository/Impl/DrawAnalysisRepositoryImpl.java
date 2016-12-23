package com.yunsoo.di.dao.repository.Impl;

import com.yunsoo.di.dao.entity.DrawReportEntity;
import com.yunsoo.di.dao.repository.DrawAnalysisRepository;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
            entity.setCount(((Number) d[2]).intValue());
            list.add(entity);
        }
        return list;
    }

    @Override
    public List<DrawReportEntity> getDrawPrizeRankBy(String marketingId, DateTime startDateTime, DateTime endDateTime) {
        HashMap<String, Object> parameters = new HashMap<>();
        String sql = "SELECT ev.value as rule_id, rl.comments, count(1) as count FROM  user_event ev " +
                "inner join  mkt_draw_rule rl on ev.value=rl.id and rl.is_equal=0 " +
                "where rl.marketing_id=:marketingId  " ;

        parameters.put("marketingId",marketingId);

        if (startDateTime!=null){
            sql=sql+" and ev.created_datetime>=:startDateTime";
            parameters.put("startDateTime", startDateTime.toString("yyyy-MM-dd"));
        }

        if (endDateTime!=null){
            sql=sql+ " and ev.created_datetime<=:endDateTime";
            parameters.put("endDateTime", endDateTime.toString("yyyy-MM-dd"));
        }

        sql=sql+" group by ev.value;";


        Query query =  entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List<Object[]> data = query.getResultList();
        List<DrawReportEntity> list = new ArrayList<>();
        for (Object[] d : data) {
            DrawReportEntity entity = new DrawReportEntity();
            entity.setId((String) d[0]);
            entity.setDrawRuleName((String) d[1]);
            entity.setCount(((Number) d[2]).intValue());
            list.add(entity);
        }
        return list;
    }

    @Override
    public List<DrawReportEntity> getMappedDrawReportReportBy(String marketingId, DateTime startDateTime, DateTime endDateTime) {
        HashMap<String, Object> parameters = new HashMap<>();
        String sql = "select mdr.comments, mdr.id, sum(ifnull(ddp.count, 0))  from mkt_draw_rule mdr                  \n" +
                "inner join marketing_arj_draw_rule mp on mdr.id = mp.draw_rule_id and mp.active = 1 \n" +
                "inner join di_daily_draw_price ddp on ddp.draw_rule_id = mp.map_draw_rule_id  and ddp.draw_date >= :ds and ddp.draw_date <:de \n" +
                "where mdr.marketing_id = :marketing_id \n" +
                "group by mdr.id";

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
            entity.setCount(((Number) d[2]).intValue());
            list.add(entity);
        }
        return list;
    }

}
