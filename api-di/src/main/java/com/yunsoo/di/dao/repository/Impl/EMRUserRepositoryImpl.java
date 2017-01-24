package com.yunsoo.di.dao.repository.Impl;

import com.yunsoo.di.dao.entity.EMRUserEntity;
import com.yunsoo.di.dao.entity.EMRUserProductEventStatistics;
import com.yunsoo.di.dao.repository.EMRUserRepository;
import org.hibernate.annotations.common.util.StringHelper;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yunsu on 2016/11/8.
 */
@Repository
public class EMRUserRepositoryImpl implements EMRUserRepository {

    @PersistenceContext(unitName = "di")
    private EntityManager entityManager;

    /**
     * 消费者行为分析，扫描用户列表
     *
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStart
     * @param createdDateTimeEnd
     * @param wxUser               判断是否为微信用户
     * @param pageable
     * @return
     */
    @Override
    public List<EMRUserEntity> findEventUsersFilterByScan(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd, Boolean wxUser, Pageable pageable) {
        String sql = "select DISTINCT u.*, pc.province, pc.city, ev2.ip, ua.os  from  di_event ev " +
                "inner join  di_user u on ev.org_id = u.org_id and (ev.user_id = u.user_id and ev.ys_id = u.ys_id) " +
                "inner join di_event ev2 on u.latest_scan_id = ev2.event_id and ev2.name='scan' " +
                "left join  lu_province_city pc on u.location_id = pc.id " +
                " LEFT JOIN  lu_user_agent ua ON ua.id=ev2.user_agent_id " +
                "where ev.name= :eventName " +
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

        if (wxUser != null && wxUser) {
            sql = sql + " and u.wx_openid is not null";
        }
        sql = sql + " order by u.join_datetime desc";


        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        if (pageable != null) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }

        List<Object[]> queryList = query.getResultList();
        return getUserListByQueryList(queryList);
    }

    /**
     * 消费者行为分析，抽奖用户列表
     *
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStart
     * @param createdDateTimeEnd
     * @param wxUser               判断是否为微信用户
     * @param pageable
     * @return
     */
    @Override
    public List<EMRUserEntity> findEventUsersFilterByDraw(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart,
                                                          DateTime createdDateTimeEnd, Boolean wxUser, Pageable pageable) {

        return getDrawOrWinUserList(orgId, productBaseId, province, city, createdDateTimeStart,
                createdDateTimeEnd, wxUser, pageable, 2);
    }

    /**
     * 消费者行为分析，中奖用户列表
     *
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStart
     * @param createdDateTimeEnd
     * @param wxUser               判断是否为微信用户
     * @param pageable
     * @return
     */
    @Override
    public List<EMRUserEntity> findEventUsersFilterByWin(String orgId, String productBaseId, String province, String city,
                                                         DateTime createdDateTimeStart, DateTime createdDateTimeEnd, Boolean wxUser, Pageable pageable) {
        return getDrawOrWinUserList(orgId, productBaseId, province, city, createdDateTimeStart,
                createdDateTimeEnd, wxUser, pageable, 3);
    }


    private List<EMRUserEntity> getDrawOrWinUserList(String orgId, String productBaseId, String province, String city,
                                                     DateTime createdDateTimeStart, DateTime createdDateTimeEnd, Boolean wxUser, Pageable pageable, int level) {
        String sql = "select DISTINCT  u.*, pc.province, pc.city, ev2.ip, ua.os from  mkt_draw_record dr inner join  di_event ev on dr.scan_record_id=ev.event_id and ev.name='scan' " +
                "inner join  di_user u on ev.org_id = u.org_id and (ev.user_id = u.user_id and ev.ys_id = u.ys_id) " +
                "inner join di_event ev2 on u.latest_scan_id = ev2.event_id and ev2.name='scan' " +
                "left join  lu_province_city pc on u.location_id = pc.id " +
                " LEFT JOIN  lu_user_agent ua ON ua.id=ev2.user_agent_id " +
                "where ev.org_id =:orgId  ";

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

        if (wxUser != null && wxUser) {
            sql = sql + " and u.wx_openid is not null";
        }

        if (level > 1) {
            sql = sql + " and dr.isPrized is not null";
        }
        if (level > 2) {
            sql = sql + " and dr.isPrized = 1";
        }
        sql = sql + " order by u.join_datetime desc";

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        if (pageable != null) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }

        List<Object[]> queryList = query.getResultList();
        return getUserListByQueryList(queryList);
    }

    /**
     * 兑奖用户列表
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStart
     * @param createdDateTimeEnd
     * @param wxUser 判断是否为微信用户
     * @param pageable
     * @return
     */
    @Override
    public List<EMRUserEntity> findEventUsersFilterByReward(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStart, DateTime createdDateTimeEnd, Boolean wxUser, Pageable pageable) {
        String sql = "select DISTINCT u.*, pc.province, pc.city, ev2.ip, ua.os from  mkt_draw_record dr left join  di_event ev on dr.scan_record_id=ev.event_id and ev.name='scan' INNER JOIN  mkt_draw_prize dp ON dr.id=dp.draw_record_id " +
                "inner join  di_user u on ev.org_id = u.org_id and (ev.user_id = u.user_id and ev.ys_id = u.ys_id) " +
                "inner join di_event ev2 on u.latest_scan_id = ev2.event_id and ev2.name='scan' " +
                "left join  lu_province_city pc on u.location_id = pc.id " +
                " LEFT JOIN  lu_user_agent ua ON ua.id=ev2.user_agent_id " +
                "where ev.org_id =:orgId  and dr.isPrized = 1 and dp.status_code in ('submit','paid') ";

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

        if (wxUser != null && wxUser) {
            sql = sql + " and u.wx_openid is not null";
        }
        sql = sql + " order by u.join_datetime desc";

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        if (pageable != null) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }

        List<Object[]> queryList = query.getResultList();
        return getUserListByQueryList(queryList);
    }

    @Override
    public int countUsersByFilter(String orgId, Boolean sex, String phone, String name, String province, String city, Integer ageStart, Integer ageEnd, DateTime createdDateTimeStart, DateTime createdDateTimeEnd, List<String> userTags, boolean userTagsIgnored, Boolean wxUser) {
        String sql = "select count(DISTINCT u.user_id, u.ys_id, u.org_id) " +
                "from  di_user u left join  lu_province_city pc on u.location_id = pc.id left join  user_tag tag on tag.org_id=u.org_id and (tag.user_id = u.user_id and tag.ys_id = u.ys_id) " +
                "LEFT JOIN   di_event ev on u.latest_scan_id=ev.event_id and ev.name='scan' LEFT JOIN  lu_user_agent ua ON ua.id=ev.user_agent_id " +
                "where u.org_id =:orgId ";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("orgId", orgId);
        if (sex != null) {
            sql = sql + " and u.sex=:sex";
            parameters.put("sex", sex);
        }

        if (!StringUtils.isEmpty(phone)) {
            sql = sql + " and u.phone like :phone";
            parameters.put("phone", "%" + phone + "%");
        }

        if (!StringUtils.isEmpty(name)) {
            sql = sql + " and u.name like :name";
            parameters.put("name", "%" + name + "%");
        }

        if (!StringUtils.isEmpty(province)) {
            sql = sql + " and pc.province like :province";
            parameters.put("province", "%" + province + "%");
        }
        if (!StringUtils.isEmpty(city)) {
            sql = sql + " and pc.city like :city";
            parameters.put("city", "%" + city + "%");
        }
        if (ageStart != null) {
            sql = sql + " and u.age>= :ageStart";
            parameters.put("ageStart", ageStart);
        }
        if (ageEnd != null) {
            sql = sql + " and u.age>= :ageEnd";
            parameters.put("ageEnd", ageEnd);
        }

        if (createdDateTimeStart != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeStart.toString())) {
            sql = sql + " and u.join_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStart.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEnd != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeEnd.toString())) {
            sql = sql + " and ev.event_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", createdDateTimeEnd.toString("yyyy-MM-dd"));
        }
        if (!userTagsIgnored) {
            sql = sql + " and tag.tag_id in :tags";
            parameters.put("tags", userTags);
        }
        if (wxUser != null) {
            if (wxUser) {
                sql = sql + " and u.wx_openid is not null";
            } else {
                sql = sql + " and (u.wx_openid is null or u.wx_openid=''";
            }
        }

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }

        Number value = (Number) query.getSingleResult();
        return value.intValue();
    }

    @Override
    public List<EMRUserEntity> findUsersByFilter(String orgId, Boolean sex, String phone, String name,
                                                 String province, String city, Integer ageStart, Integer ageEnd,
                                                 DateTime createdDateTimeStart, DateTime createdDateTimeEnd, List<String> userTags,
                                                 boolean userTagsIgnored, Boolean wxUser, Pageable pageable) {

        String sql = "select DISTINCT u.*, pc.province, pc.city, ev.ip, ua.os " +
                "from  di_user u left join  lu_province_city pc on u.location_id = pc.id left join  user_tag tag on tag.org_id=u.org_id and (tag.user_id = u.user_id and tag.ys_id = u.ys_id) " +
                "LEFT JOIN   di_event ev on u.latest_scan_id=ev.event_id and ev.name='scan' LEFT JOIN  lu_user_agent ua ON ua.id=ev.user_agent_id " +
                "where u.org_id =:orgId ";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("orgId", orgId);
        if (sex != null) {
            sql = sql + " and u.sex=:sex";
            parameters.put("sex", sex);
        }

        if (!StringUtils.isEmpty(phone)) {
            sql = sql + " and u.phone like :phone";
            parameters.put("phone", "%" + phone + "%");
        }

        if (!StringUtils.isEmpty(name)) {
            sql = sql + " and u.name like :name";
            parameters.put("name", "%" + name + "%");
        }

        if (!StringUtils.isEmpty(province)) {
            sql = sql + " and pc.province like :province";
            parameters.put("province", "%" + province + "%");
        }
        if (!StringUtils.isEmpty(city)) {
            sql = sql + " and pc.city like :city";
            parameters.put("city", "%" + city + "%");
        }
        if (ageStart != null) {
            sql = sql + " and u.age>= :ageStart";
            parameters.put("ageStart", ageStart);
        }
        if (ageEnd != null) {
            sql = sql + " and u.age>= :ageEnd";
            parameters.put("ageEnd", ageEnd);
        }

        if (createdDateTimeStart != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeStart.toString())) {
            sql = sql + " and u.join_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStart.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEnd != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeEnd.toString())) {
            sql = sql + " and u.join_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", createdDateTimeEnd.toString("yyyy-MM-dd"));
        }
        if (!userTagsIgnored) {
            sql = sql + " and tag.tag_id in :tags";
            parameters.put("tags", userTags);
        }
        if (wxUser != null) {
            if (wxUser) {
                sql = sql + " and u.wx_openid is not null";
            } else {
                sql = sql + " and (u.wx_openid is null or u.wx_openid=''";
            }
        }
        // 按照加入时间倒序排列
        sql = sql + " order by u.join_datetime desc";

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        if (pageable != null) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }

        List<Object[]> queryList = query.getResultList();
        return getUserListByQueryList(queryList);
    }

    private List<EMRUserEntity> getUserListByQueryList(List<Object[]> queryList) {
        List<EMRUserEntity> list = new ArrayList<>();

        for (Object[] obj : queryList) {
            EMRUserEntity userEntity = new EMRUserEntity();
            userEntity.setId(((Number) obj[0]).intValue());
            userEntity.setUserId((String) obj[1]);
            userEntity.setYsId((String) obj[2]);
            userEntity.setOrgId((String) obj[3]);
            userEntity.setName((String) obj[4]);
            userEntity.setPhone((String) obj[5]);
            userEntity.setEmail((String) obj[6]);
            if (obj[7] != null) {
                userEntity.setAge(((Number) obj[7]).intValue());
            }
            userEntity.setSex((Boolean) obj[8]);
            userEntity.setGravatarUrl((String) obj[9]);
            userEntity.setWxOpenId((String) obj[10]);
            Timestamp timestamp = (Timestamp) obj[12];
            userEntity.setJoinDateTime(LocalDateTime.fromDateFields(timestamp).toDateTime());
            userEntity.setProvince((String) obj[14]);
            userEntity.setCity((String) obj[15]);
            userEntity.setIp((String) obj[16]);
            userEntity.setDevice((String) obj[17]);
            list.add(userEntity);
        }
        return list;
    }

    @Override
    public EMRUserEntity getUser(String orgId, String userId, String ysId) {
        String sql = "select DISTINCT u.*, pc.province, pc.city, ev.ip, ua.os " +
                "from  di_user u left join  lu_province_city pc on u.location_id = pc.id " +
                "LEFT JOIN   di_event ev on u.latest_scan_id=ev.event_id and ev.name='scan' LEFT JOIN  lu_user_agent ua ON ua.id=ev.user_agent_id " +
                "where u.org_id =:orgId ";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("orgId", orgId);

        if (!StringUtils.isEmpty(userId)) {
            sql = sql + " and u.user_id = :userId";
            parameters.put("userId", userId);
        }

        if (!StringUtils.isEmpty(ysId)) {
            sql = sql + " and u.ys_id = :ysId";
            parameters.put("ysId", ysId);
        }

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }

        Object[] obj = (Object[]) query.getSingleResult();

        EMRUserEntity userEntity = new EMRUserEntity();
        userEntity.setId(((Number) obj[0]).intValue());
        userEntity.setUserId((String) obj[1]);
        userEntity.setYsId((String) obj[2]);
        userEntity.setOrgId((String) obj[3]);
        userEntity.setName((String) obj[4]);
        userEntity.setPhone((String) obj[5]);
        userEntity.setEmail((String) obj[6]);
        if (obj[7] != null) {
            userEntity.setAge(((Number) obj[7]).intValue());
        }
        userEntity.setSex((Boolean) obj[8]);
        userEntity.setGravatarUrl((String) obj[9]);
        userEntity.setWxOpenId((String) obj[10]);
        Timestamp timestamp = (Timestamp) obj[12];
        userEntity.setJoinDateTime(LocalDateTime.fromDateFields(timestamp).toDateTime());
        userEntity.setProvince((String) obj[14]);
        userEntity.setCity((String) obj[15]);
        userEntity.setIp((String) obj[16]);
        userEntity.setDevice((String) obj[17]);

        return userEntity;
    }


    /**
     * 用于<strong>消费者行为分析</strong>里面，当用户选择微信来源的时候，用于显示微信用户列表
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStartTo
     * @param createdDateTimeEndTo
     * @param pageable
     * @return
     */
    @Override
    public List<EMRUserEntity> findEventUsersFilterByWX(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStartTo, DateTime createdDateTimeEndTo, Pageable pageable) {
        String sql = "select DISTINCT u.*, pc.province, pc.city, ev2.ip, ua.os from di_event ev " +
                "inner join  di_user u on ev.org_id = u.org_id and ev.user_id = u.user_id and ev.ys_id = u.ys_id " +
                " left join  lu_province_city pc on u.location_id = pc.id " +
                "LEFT JOIN   di_event ev2 on u.latest_scan_id=ev2.event_id and ev2.name='scan' LEFT JOIN  lu_user_agent ua ON ua.id=ev2.user_agent_id " +
                "where ev.org_id =:orgId ";

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
        if (!StringUtils.isEmpty(city)) {
            sql = sql + " and pc.city like :city";
            parameters.put("city", "%" + city + "%");
        }

        if (createdDateTimeStartTo != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeStartTo.toString())) {
            sql = sql + " and ev.event_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStartTo.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEndTo != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeEndTo.toString())) {
            sql = sql + " and ev.event_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", createdDateTimeEndTo.toString("yyyy-MM-dd"));
        }
        sql = sql + " and u.wx_openid is not null";

        // 按照加入时间倒序排列
        sql = sql + " order by u.join_datetime desc";

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }

        if (pageable != null) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }

        List<Object[]> queryList = query.getResultList();
        return getUserListByQueryList(queryList);

    }

    /**
     * 用于<strong>消费者行为分析</strong>里面，当用户选择微信来源的时候，用于显示微信用户列表的个数
     * @param orgId
     * @param productBaseId
     * @param province
     * @param city
     * @param createdDateTimeStartTo
     * @param createdDateTimeEndTo
     * @return
     */
    @Override
    public int countUsersByFilterByWX(String orgId, String productBaseId, String province, String city, DateTime createdDateTimeStartTo, DateTime createdDateTimeEndTo) {
        String sql = "select count(distinct u.id) from di_event ev " +
                "inner join di_user u on ev.org_id = u.org_id and ev.user_id = u.user_id and ev.ys_id = u.ys_id " +
                "left join  lu_province_city pc on u.location_id = pc.id " +
                "LEFT JOIN   di_event ev2 on u.latest_scan_id=ev2.event_id and ev2.name='scan' " +
                "where ev.org_id =:orgId ";
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
        if (!StringUtils.isEmpty(city)) {
            sql = sql + " and pc.city like :city";
            parameters.put("city", "%" + city + "%");
        }

        if (createdDateTimeStartTo != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeStartTo.toString())) {
            sql = sql + " and ev.event_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStartTo.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEndTo != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeEndTo.toString())) {
            sql = sql + " and ev.event_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", createdDateTimeEndTo.toString("yyyy-MM-dd"));
        }
        sql = sql + " and u.wx_openid is not null";

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        Number number = (Number) query.getSingleResult();
        return number.intValue();
    }


    // 统计各类事件的数据
    @Override
    public List<EMRUserProductEventStatistics> queryUserEventStatistics(String orgId, String userId, String ysId, DateTime createdDateTimeStartTo, DateTime createdDateTimeEndTo) {
        List<Object[]> queryList = getScanEventStat(orgId, userId, ysId, createdDateTimeStartTo, createdDateTimeEndTo);
        HashMap<String, EMRUserProductEventStatistics> items = new HashMap<>();

        for (Object[] data : queryList) {
            String productBaseId = (String) data[0];
            String productName = (String) data[1];
            Number count = (Number) data[2];
            EMRUserProductEventStatistics eventStat = null;
            if (items.containsKey(productBaseId)) {
                eventStat = items.get(productBaseId);
            } else {
                eventStat = new EMRUserProductEventStatistics();
                eventStat.setProductName(productName);
                eventStat.setProductBaseId(productBaseId);
                items.put(productBaseId, eventStat);
            }
            eventStat.setScanCount(count.intValue());

        }

        queryList = getDrawEventStat(orgId, userId, ysId, createdDateTimeStartTo, createdDateTimeEndTo);
        for (Object[] data : queryList) {
            String productBaseId = (String) data[0];
            String productName = (String) data[1];
            Number drawCount = (Number) data[2];
            Number winCount = (Number) data[3];
            Number rewardCount = (Number) data[4];
            EMRUserProductEventStatistics eventStat = null;
            if (items.containsKey(productBaseId)) {
                eventStat = items.get(productBaseId);
            } else {
                eventStat = new EMRUserProductEventStatistics();
                eventStat.setProductName(productName);
                eventStat.setProductBaseId(productBaseId);
                items.put(productBaseId, eventStat);
            }
            eventStat.setDrawCount(drawCount.intValue());
            eventStat.setWinCount(winCount.intValue());
            eventStat.setRewardCount(rewardCount.intValue());
        }
        queryList = getUserEventStat(orgId, userId, ysId, createdDateTimeStartTo, createdDateTimeEndTo);
        for (Object[] data : queryList) {
            String productBaseId = (String) data[0];
            String productName = (String) data[1];
            Number commentCount = (Number) data[2];
            Number storeCount = (Number) data[3];
            Number shareCount = (Number) data[4];
            EMRUserProductEventStatistics eventStat = null;
            if (items.containsKey(productBaseId)) {
                eventStat = items.get(productBaseId);
            } else {
                eventStat = new EMRUserProductEventStatistics();
                eventStat.setProductName(productName);
                eventStat.setProductBaseId(productBaseId);
                items.put(productBaseId, eventStat);
            }
            eventStat.setCommentCount(commentCount.intValue());
            eventStat.setStoreCount(storeCount.intValue());
            eventStat.setShareCount(shareCount.intValue());
        }

        List<EMRUserProductEventStatistics> list = items.values().stream().collect(Collectors.toList());
        if(list.size() > 1){ list.sort((o1, o2) -> o1.getProductName().compareTo(o2.getProductName()));}

        return list;
    }

    private List<Object[]> getScanEventStat(String orgId, String userId, String ysId, DateTime createdDateTimeStartTo, DateTime createdDateTimeEndTo) {
        HashMap<String, Object> parameters = new HashMap<>();
        String sql = "select ev.product_base_id, pb.name, count(1) from di_event ev inner join product_base pb on ev.product_base_id = pb.id where ev.org_id = :orgId";


        parameters.put("orgId", orgId);

        if (!StringHelper.isEmpty(userId)) {
            sql = sql +
                    " and ev.user_id = :userId";
            parameters.put("userId", userId);
        } else if (!StringHelper.isEmpty(ysId)) {
            sql = sql + " and ev.ys_id = :ysId";
            parameters.put("ysId", ysId);
        }

        if (createdDateTimeStartTo != null && !StringUtils.isEmpty(createdDateTimeStartTo.toString())) {
            sql = sql + " and ev.event_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStartTo.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEndTo != null && !StringUtils.isEmpty(createdDateTimeEndTo.toString())) {
            sql = sql + " and ev.event_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", createdDateTimeEndTo.toString("yyyy-MM-dd"));
        }
        sql = sql + " group by ev.product_base_id";


        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        return query.getResultList();
    }

    private List<Object[]> getDrawEventStat(String orgId, String userId, String ysId, DateTime createdDateTimeStartTo, DateTime createdDateTimeEndTo) {
        HashMap<String, Object> parameters = new HashMap<>();

        String sql = "select ev.product_base_id, pb.name, count(1) as 'draw_count',\n" +
                "sum(case when mdr.isPrized = 1 then 1 else 0 end) as 'win_count',\n" +
                "sum(case when mdp.status_code in ('paid','submit') then 1 else 0 end) as 'reward_count'\n" +
                "  from mkt_draw_record mdr inner join di_event ev on mdr.scan_record_id = ev.event_id and ev.name='scan'\n" +
                "inner join product_base pb on ev.product_base_id = pb.id\n" +
                "left join mkt_draw_prize mdp on mdr.scan_record_id = mdp.scan_record_id \n" +
                "where ev.org_id = :orgId";

        parameters.put("orgId", orgId);

        if (!StringHelper.isEmpty(userId)) {
            sql = sql +
                    " and ev.user_id = :userId";
            parameters.put("userId", userId);
        } else if (!StringHelper.isEmpty(ysId)) {
            sql = sql + " and ev.ys_id = :ysId";
            parameters.put("ysId", ysId);
        }

        if (createdDateTimeStartTo != null && !StringUtils.isEmpty(createdDateTimeStartTo.toString())) {
            sql = sql + " and ev.event_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStartTo.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEndTo != null && !StringUtils.isEmpty(createdDateTimeEndTo.toString())) {
            sql = sql + " and ev.event_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", createdDateTimeEndTo.toString("yyyy-MM-dd"));
        }
        sql = sql + " group by ev.product_base_id";


        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        return query.getResultList();
    }

    private List<Object[]> getUserEventStat(String orgId, String userId, String ysId, DateTime createdDateTimeStartTo, DateTime createdDateTimeEndTo) {
        HashMap<String, Object> parameters = new HashMap<>();
        String sql = "SELECT ev.product_base_id, pb.name, sum(case when ue.type_code = 'comment' then 1 else 0 end) as 'comment_count',\n" +
                "sum(case when ue.type_code = 'store_url' then 1 else 0 end) as 'store_count',\n" +
                "sum(case when ue.type_code = 'share' then 1 else 0 end) as 'share_count'\n" +
                " FROM di.user_event ue\n" +
                "inner join di_event ev on ue.scan_record_id = ev.event_id and ev.name='scan'\n" +
                "inner join product_base pb on ev.product_base_id = pb.id where ev.org_id = :orgId";


        parameters.put("orgId", orgId);

        if (!StringHelper.isEmpty(userId)) {
            sql = sql +
                    " and ev.user_id = :userId";
            parameters.put("userId", userId);
        } else if (!StringHelper.isEmpty(ysId)) {
            sql = sql + " and ev.ys_id = :ysId";
            parameters.put("ysId", ysId);
        }

        if (createdDateTimeStartTo != null && !StringUtils.isEmpty(createdDateTimeStartTo.toString())) {
            sql = sql + " and ev.event_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStartTo.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEndTo != null && !StringUtils.isEmpty(createdDateTimeEndTo.toString())) {
            sql = sql + " and ev.event_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", createdDateTimeEndTo.toString("yyyy-MM-dd"));
        }
        sql = sql + " group by ev.product_base_id";


        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        return query.getResultList();
    }


}
