package com.yunsoo.di.dao.repository.Impl;

import com.yunsoo.di.dao.entity.EMRUserEntity;
import com.yunsoo.di.dao.repository.EMRUserRepository;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yunsu on 2016/11/8.
 */
@Repository
public class EMRUserRepositoryImpl implements EMRUserRepository {

    @PersistenceContext(unitName = "di")
    private EntityManager entityManager;

    @Override
    public int countUsersByFilter(String orgId, Boolean sex, String phone, String name, String province, String city, Integer ageStart, Integer ageEnd, DateTime createdDateTimeStart, DateTime createdDateTimeEnd, List<String> userTags, boolean userTagsIgnored, Boolean wxUser) {
        String sql = "select count(DISTINCT u.user_id, u.ys_id, u.org_id) " +
                "from di.di_user u left join di.lu_province_city pc on u.location_id = pc.id left join di.user_tag tag on tag.org_id=u.org_id and (tag.user_id = u.user_id and tag.ys_id = u.ys_id) " +
                "LEFT JOIN  di.di_event ev on u.latest_scan_id=ev.event_id and ev.name='scan' LEFT JOIN di.lu_user_agent ua ON ua.id=ev.user_agent_id "+
                "where u.org_id =:orgId ";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("orgId", orgId);
        if (sex!=null){
            sql=sql+" and u.sex=:sex";
            parameters.put("sex", sex);
        }

        if (!StringUtils.isEmpty(phone)){
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
        if (ageStart!=null){
            sql = sql + " and u.age>= :ageStart";
            parameters.put("ageStart", ageStart);
        }
        if (ageEnd!=null){
            sql = sql + " and u.age>= :ageEnd";
            parameters.put("ageEnd",  ageEnd);
        }

        if (createdDateTimeStart != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeStart.toString())) {
            sql = sql + " and u.join_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStart.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEnd != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeEnd.toString())) {
            sql = sql + " and ev.event_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", createdDateTimeEnd.toString("yyyy-MM-dd"));
        }
        if ( !userTagsIgnored){
            sql = sql + " and tag.tag_id in :tags";
            parameters.put("tags", userTags);
        }
        if (wxUser!=null){
            if (wxUser){
                sql = sql + " and u.wx_openid is not null";
            }else {
                sql = sql + " and (u.wx_openid is null or u.wx_openid=''";
            }
        }

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }

        BigInteger value = (BigInteger) query.getSingleResult();
        return value.intValue();
    }

    @Override
    public List<EMRUserEntity> findUsersByFilter(String orgId, Boolean sex, String phone, String name,
                                              String province, String city, Integer ageStart, Integer ageEnd,
                                              DateTime createdDateTimeStart, DateTime createdDateTimeEnd, List<String> userTags,
                                              boolean userTagsIgnored, Boolean wxUser, Pageable pageable) {

        String sql = "select DISTINCT u.*, pc.province, pc.city, ev.ip, ua.os " +
                "from di.di_user u left join di.lu_province_city pc on u.location_id = pc.id left join di.user_tag tag on tag.org_id=u.org_id and (tag.user_id = u.user_id and tag.ys_id = u.ys_id) " +
                "LEFT JOIN  di.di_event ev on u.latest_scan_id=ev.event_id and ev.name='scan' LEFT JOIN di.lu_user_agent ua ON ua.id=ev.user_agent_id "+
                "where u.org_id =:orgId ";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("orgId", orgId);
        if (sex!=null){
            sql=sql+" and u.sex=:sex";
            parameters.put("sex", sex);
        }

        if (!StringUtils.isEmpty(phone)){
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
        if (ageStart!=null){
            sql = sql + " and u.age>= :ageStart";
            parameters.put("ageStart", ageStart);
        }
        if (ageEnd!=null){
            sql = sql + " and u.age>= :ageEnd";
            parameters.put("ageEnd",  ageEnd);
        }

        if (createdDateTimeStart != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeStart.toString())) {
            sql = sql + " and u.join_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", createdDateTimeStart.toString("yyyy-MM-dd"));
        }
        if (createdDateTimeEnd != null && !org.springframework.util.StringUtils.isEmpty(createdDateTimeEnd.toString())) {
            sql = sql + " and ev.event_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", createdDateTimeEnd.toString("yyyy-MM-dd"));
        }
        if ( !userTagsIgnored){
            sql = sql + " and tag.tag_id in :tags";
            parameters.put("tags", userTags);
        }
        if (wxUser!=null){
            if (wxUser){
                sql = sql + " and u.wx_openid is not null";
            }else {
                sql = sql + " and (u.wx_openid is null or u.wx_openid=''";
            }
        }

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        if (pageable != null) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }

        List<Object[]> queryList = query.getResultList();
        List<EMRUserEntity> list = new ArrayList<>();

        for(Object[] obj : queryList)
        {
            EMRUserEntity userEntity=new EMRUserEntity();
            userEntity.setId(((BigInteger)obj[0]).intValue());
            userEntity.setUserId((String) obj[1]);
            userEntity.setYsId((String) obj[2]);
            userEntity.setOrgId((String) obj[3]);
            userEntity.setName((String) obj[4]);
            userEntity.setPhone((String) obj[5]);
            userEntity.setEmail((String) obj[6]);
            if (obj[7]!=null){
                userEntity.setAge(((BigInteger)obj[7]).intValue());
            }
            userEntity.setSex((Boolean) obj[8]);
            userEntity.setGravatarUrl((String) obj[9]);
            userEntity.setWxOpenId((String) obj[10]);
            Timestamp timestamp= (Timestamp) obj[12];
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
                "from di.di_user u left join di.lu_province_city pc on u.location_id = pc.id " +
                "LEFT JOIN  di.di_event ev on u.latest_scan_id=ev.event_id and ev.name='scan' LEFT JOIN di.lu_user_agent ua ON ua.id=ev.user_agent_id "+
                "where u.org_id =:orgId ";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("orgId", orgId);

        if (!StringUtils.isEmpty(userId)){
            sql = sql + " and u.user_id = :userId";
            parameters.put("userId", userId);
        }

        if (!StringUtils.isEmpty(ysId)){
            sql = sql + " and u.ys_id = :ysId";
            parameters.put("ysId", ysId);
        }

        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }

        Object[] obj= (Object[]) query.getSingleResult();

        EMRUserEntity userEntity=new EMRUserEntity();
        userEntity.setId(((BigInteger)obj[0]).intValue());
        userEntity.setUserId((String) obj[1]);
        userEntity.setYsId((String) obj[2]);
        userEntity.setOrgId((String) obj[3]);
        userEntity.setName((String) obj[4]);
        userEntity.setPhone((String) obj[5]);
        userEntity.setEmail((String) obj[6]);
        if (obj[7]!=null){
            userEntity.setAge(((BigInteger)obj[7]).intValue());
        }
        userEntity.setSex((Boolean) obj[8]);
        userEntity.setGravatarUrl((String) obj[9]);
        userEntity.setWxOpenId((String) obj[10]);
        Timestamp timestamp= (Timestamp) obj[12];
        userEntity.setJoinDateTime(LocalDateTime.fromDateFields(timestamp).toDateTime());
        userEntity.setProvince((String) obj[14]);
        userEntity.setCity((String) obj[15]);
        userEntity.setIp((String) obj[16]);
        userEntity.setDevice((String) obj[17]);

        return userEntity;
    }

}
