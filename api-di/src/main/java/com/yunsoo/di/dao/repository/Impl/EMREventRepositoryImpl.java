package com.yunsoo.di.dao.repository.Impl;

import com.yunsoo.di.dao.entity.EMREventEntity;
import com.yunsoo.di.dao.repository.EMREventRepository;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yunsu on 2016/11/8.
 */

@Repository
public class EMREventRepositoryImpl implements EMREventRepository {
    @PersistenceContext(unitName = "di")
    private EntityManager entityManager;

    @Override
    public List<EMREventEntity> findByFilter(String orgId, String userId, String ysId, DateTime eventDateTimeStart, DateTime eventDateTimeEnd, Pageable pageable) {

        StringBuilder builder1=new StringBuilder("select ev.org_id, ev.user_id, ev.ys_id, ev.name as event_name, ev.product_base_id, ev.product_key, ev.key_batch_id, " );
        builder1.append(" 0 as is_priced, ev.event_datetime,  pc.province, pc.city, ev.ip, p.name, '' as marketing_id ");
        builder1.append("from  di_event ev left join  lu_province_city pc on ev.location_id = pc.id ");
        builder1.append("LEFT JOIN  product_base p on p.id=ev.product_base_id ");
        builder1.append("where ev.org_id =:orgId  and ev.ys_id =:ysId  and ev.user_id = :userId ");

        StringBuilder builder2=new StringBuilder("select ev.org_id, ev.user_id, ev.ys_id, 'draw' as event_name, ev.product_base_id, ev.product_key, ev.key_batch_id, ");
        builder2.append("dr.isPrized as is_priced, dr.created_datetime as event_datetime, pc.province, pc.city, ev.ip, p.name, dr.marketing_id " );
        builder2.append("from  mkt_draw_record dr ");
        builder2.append("left join  di_event ev on dr.scan_record_id=ev.event_id ");
        builder2.append("left join  lu_province_city pc on ev.location_id = pc.id ");
        builder2.append("LEFT JOIN  product_base p on p.id=ev.product_base_id ");
        builder2.append("where ev.org_id =:orgId and ev.ys_id=:ysId and ev.user_id= :userId ");

        HashMap<String, Object> parameters = new HashMap<>();

        if (eventDateTimeStart != null && !org.springframework.util.StringUtils.isEmpty(eventDateTimeStart.toString())) {
            builder1.append("and ev.event_datetime >=:eventDateTimeStart ");
            builder2.append("and dr.created_datetime >=:eventDateTimeStart ");
            parameters.put("eventDateTimeStart", eventDateTimeStart.toString("yyyy-MM-dd"));
        }

        if (eventDateTimeEnd != null && !org.springframework.util.StringUtils.isEmpty(eventDateTimeEnd.toString())) {
            builder1.append("and ev.event_datetime <=:eventDateTimeEnd ");
            builder2.append("and dr.created_datetime <=:eventDateTimeEnd ");
            parameters.put("eventDateTimeEnd", eventDateTimeEnd.toString("yyyy-MM-dd"));
        }

        StringBuilder sqlBuilder=new StringBuilder(builder1.toString());
        sqlBuilder.append(" union all ");
        sqlBuilder.append(builder2.toString());
        sqlBuilder.append(" order by event_datetime DESC ");

        if (userId==null){
            parameters.put("userId", "");
        }else {
            parameters.put("userId", userId);
        }

        if (orgId==null){
            parameters.put("orgId", "");
        }else {
            parameters.put("orgId", orgId);
        }

        if (ysId==null){
            parameters.put("ysId", "");
        }else {
            parameters.put("ysId", ysId);
        }

        Query query = entityManager.createNativeQuery(sqlBuilder.toString());

        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }

        if (pageable != null) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }

        List<Object[]> queryList = query.getResultList();

        List<EMREventEntity> list = new ArrayList<>();

        for(Object[] obj : queryList)
        {
            EMREventEntity eventEntity=new EMREventEntity();
            eventEntity.setOrgId((String) obj[0]);
            eventEntity.setUserId((String) obj[1]);
            eventEntity.setYsId((String) obj[2]);
            eventEntity.setName((String) obj[3]);
            eventEntity.setProductBaseId((String) obj[4]);
            eventEntity.setProductKey((String) obj[5]);
            eventEntity.setKeyBatchId((String) obj[6]);
            if (obj[7]!=null){
                eventEntity.setIsPriced(((Number) obj[7]).intValue());
            }
            Timestamp timestamp= (Timestamp) obj[8];
            eventEntity.setEventDateTime(LocalDateTime.fromDateFields(timestamp).toDateTime());
            eventEntity.setProvince((String) obj[9]);
            eventEntity.setCity((String) obj[10]);
            eventEntity.setIp((String) obj[11]);
            eventEntity.setProductName((String) obj[12]);
            eventEntity.setMarketingId((String) obj[13]);

            list.add(eventEntity);
        }
        return list;
    }

    @Override
    public int countEventByFilter(String orgId, String userId, String ysId, DateTime eventDateTimeStart, DateTime eventDateTimeEnd) {
        StringBuilder builder1=new StringBuilder("select count(1) " );
        builder1.append("from  di_event ev left join  lu_province_city pc on ev.location_id = pc.id ");
        builder1.append("LEFT JOIN  product_base p on p.id=ev.product_base_id ");
        builder1.append("where ev.org_id =:orgId  and ev.ys_id =:ysId  and ev.user_id = :userId ");

        StringBuilder builder2=new StringBuilder("select count(1) ");
        builder2.append("from  mkt_draw_record dr ");
        builder2.append("left join  di_event ev on dr.scan_record_id=ev.event_id ");
        builder2.append("left join  lu_province_city pc on ev.location_id = pc.id ");
        builder2.append("LEFT JOIN  product_base p on p.id=ev.product_base_id ");
        builder2.append("where ev.org_id =:orgId and ev.ys_id=:ysId and ev.user_id= :userId ");

        HashMap<String, Object> parameters = new HashMap<>();

        if (eventDateTimeStart != null && !org.springframework.util.StringUtils.isEmpty(eventDateTimeStart.toString())) {
            builder1.append("and ev.event_datetime >=:eventDateTimeStart ");
            builder2.append("and dr.created_datetime >=:eventDateTimeStart ");
            parameters.put("eventDateTimeStart", eventDateTimeStart.toString("yyyy-MM-dd"));
        }

        if (eventDateTimeEnd != null && !org.springframework.util.StringUtils.isEmpty(eventDateTimeEnd.toString())) {
            builder1.append("and ev.event_datetime <=:eventDateTimeEnd ");
            builder2.append("and dr.created_datetime <=:eventDateTimeEnd ");
            parameters.put("eventDateTimeEnd", eventDateTimeEnd.toString("yyyy-MM-dd"));
        }

        StringBuilder sqlBuilder=new StringBuilder("select (");
        sqlBuilder.append(builder1.toString());
        sqlBuilder.append(" ) + ( ");

        sqlBuilder.append(builder2.toString());
        sqlBuilder.append(" );");

        if (userId==null){
            parameters.put("userId", "");
        }else {
            parameters.put("userId", userId);
        }

        if (orgId==null){
            parameters.put("orgId", "");
        }else {
            parameters.put("orgId", orgId);
        }

        if (ysId==null){
            parameters.put("ysId", "");
        }else {
            parameters.put("ysId", ysId);
        }

        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }

        Number value = (Number) query.getSingleResult();
        return value.intValue();

    }

    @Override
    public EMREventEntity recentlyConsumptionEvent(String orgId, String userId, String ysId) {

        String sql="select ev.org_id, ev.user_id, ev.ys_id, ev.name as event_name, ev.product_base_id, ev.product_key, ev.key_batch_id,  0 as is_priced, ev.event_datetime,  pc.province, pc.city, ev.ip, p.name, '' as marketing_id  from di_event ev " +
                "left join lu_province_city pc on ev.location_id=pc.id " +
                "LEFT JOIN  product_base p on p.id=ev.product_base_id "+
                "where ev.event_id = " +
                "(select up.scan_record_id " +
                "from di_user_product up " +
                "where up.org_id =:orgId and up.user_id=:userId and up.ys_id=:ysId " +
                "order by up.time_id desc limit 1) ";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("orgId", orgId);
        parameters.put("userId", userId == null ? "" : userId);
        parameters.put("ysId", ysId == null ? "" : ysId);

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }

        List<Object[]> queryList = query.getResultList();

        if (queryList.isEmpty())
            return null;

        Object[] obj=queryList.get(0);

        EMREventEntity eventEntity=new EMREventEntity();
        eventEntity.setOrgId((String) obj[0]);
        eventEntity.setUserId((String) obj[1]);
        eventEntity.setYsId((String) obj[2]);
        eventEntity.setName((String) obj[3]);
        eventEntity.setProductBaseId((String) obj[4]);
        eventEntity.setProductKey((String) obj[5]);
        eventEntity.setKeyBatchId((String) obj[6]);
        Timestamp timestamp= (Timestamp) obj[8];
        eventEntity.setEventDateTime(LocalDateTime.fromDateFields(timestamp).toDateTime());
        eventEntity.setProvince((String) obj[9]);
        eventEntity.setCity((String) obj[10]);
        eventEntity.setIp((String) obj[11]);
        eventEntity.setProductName((String) obj[12]);

        return eventEntity;
    }

    @Override
    public int periodConsumptionCount(String orgId, String userId, String ysId, DateTime eventDateTimeStart, DateTime eventDateTimeEnd) {

        String sql="select count(1) from di_event ev " +
                "where ev.event_datetime <:eventDateTimeEnd and ev.event_datetime >=:eventDateTimeStart and ev.event_id IN " +
                "(select up.scan_record_id " +
                "from di_user_product up " +
                "where up.org_id =:orgId and up.user_id=:userId and up.ys_id=:ysId ) ";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("orgId", orgId);
        parameters.put("userId", userId == null ? "" : userId);
        parameters.put("ysId", ysId == null ? "" : ysId);
        parameters.put("eventDateTimeStart", eventDateTimeStart.toString("yyyy-MM-dd HH:mm:ss"));
        parameters.put("eventDateTimeEnd", eventDateTimeEnd.toString("yyyy-MM-dd HH:mm:ss"));

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        Number value = (Number) query.getSingleResult();

        return value.intValue();


    }

}
