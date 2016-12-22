package com.yunsoo.di.dao.repository.Impl;

import com.yunsoo.di.dao.entity.PageViewDailyEntity;
import com.yunsoo.di.dao.repository.PageViewAnalysisRepository;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yqy09_000 on 2016/11/30.
 */
public class PageTrackRepositoryImpl implements PageViewAnalysisRepository {

    @PersistenceContext(unitName = "di")
    private EntityManager entityManager;

    @Override
    public List<PageViewDailyEntity> query(String hostUrl, DateTime startDateTime, DateTime endDateTime) {
        String sql = "SELECT date(convert_tz(created_datetime, '+00:00','+00:08')) as view_date, count(1) as pv, count(distinct ys_id) as uv FROM di.page_view\n" +
                "where url like :hostUrl and created_datetime >=:ds and created_datetime < :de \n" +
                "group by view_date\n";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("hostUrl",hostUrl +"%");
        parameters.put("ds", startDateTime.toString("yyyy-MM-dd"));
        parameters.put("de", endDateTime.toString("yyyy-MM-dd"));

        Query query =  entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List<Object[]> data =  query.getResultList();
        List<PageViewDailyEntity> list = new ArrayList<>();
        for (Object[] d : data) {
            PageViewDailyEntity entity = new PageViewDailyEntity();
            entity.setDate(new LocalDate(((Date)d[0]).getTime()));
           entity.setPv(((Number) d[1]).intValue());
            entity.setUv(((Number) d[2]).intValue());
            list.add(entity);
        }
        return list;
    }

    @Override
    public int[] totalPageView(String hostUrl) {
        String sql = "SELECT count(1) as pv, count(distinct ys_id) as uv FROM di.page_view\n" +
                "where url like :hostUrl";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("hostUrl",hostUrl +"%");

        Query query =  entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        Object[] data = (Object[]) query.getSingleResult();
        int[] result = new int[2];
        result[0] = ((Number)data[0]).intValue();
        result[1] = ((Number)data[1]).intValue();
        return result;
    }

}
