package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.EMRUserEntity;
import com.yunsoo.data.service.entity.UserEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EMRUserRepository extends FindOneAndSaveRepository<EMRUserEntity, String>, CustomEMRUserRepository {

    @Query("select o from #{#entityName} o where " +
            "(:orgId is null or :orgId = '' or o.orgId = :orgId) " +
            "and (:userId is null or :userId = '' or o.userId = :userId) " +
            "and (:ysId is null or :ysId = '' or o.ysId = :ysId) ")
    EMRUserEntity getUser(@Param("orgId") String orgId, @Param("userId") String userId, @Param("ysId") String ysId);

    @Query("select distinct o from #{#entityName} o left join o.userTagEntities ut where (:sex is null or o.sex = :sex) and (:phone is null or :phone = '' or o.phone like ('%' || :phone || '%'))  " +
            "and (:name is null or :name = '' or o.name like ('%' || :name || '%')) " +
            "and (:orgId is null or :orgId = '' or o.orgId = :orgId) " +
            "and (:province is null or :province = '' or o.province like ('%' || :province || '%')) " +
            "and (:city is null or :city = '' or o.city like ('%' || :city || '%')) " +
            "and ((:ageStart is null or (o.age >= :ageStart)) and (:ageEnd is null or (o.age <= :ageEnd))) " +
            "and (:createdDateTimeStart is null or o.joinDateTime >= :createdDateTimeStart) " +
            "and (:createdDateTimeEnd is null or o.joinDateTime <= :createdDateTimeEnd) " +
            "and (ut.tagId in (:userTags) or :userTagsIgnored = true) " +
            "and ((:wxUser = true and o.wxOpenId is not null) or :wxUser is null or (:wxUser = false and (o.wxOpenId is null or o.wxOpenId = ''))) ")
    Page<EMRUserEntity> findByFilter(@Param("orgId") String orgId, @Param("sex") Boolean sex, @Param("phone") String phone, @Param("name") String name,
                                     @Param("province") String province, @Param("city") String city,
                                     @Param("ageStart") Integer ageStart, @Param("ageEnd") Integer ageEnd,
                                     @Param("createdDateTimeStart") DateTime createdDateTimeStart,
                                     @Param("createdDateTimeEnd") DateTime createdDateTimeEnd,
                                     @Param("userTags") List<String> userTags,
                                     @Param("userTagsIgnored") boolean userTagsIgnored,
                                     @Param("wxUser") Boolean wxUser,
                                     Pageable pageable);

    @Query("select distinct u from #{#entityName} u inner join  u.eventEntities ev where ev.name = 'scan' " +
            "and ev.orgId = :orgId " +
            "and (:productBaseId is null or ev.productBaseId = :productBaseId) " +
            "and u.wxOpenId is not null " +
            "and (:province is null or :province = '' or u.province like ('%' || :province || '%')) " +
            "and (:city is null or :city = '' or u.city like ('%' || :city || '%')) " +
            "and (:createdDateTimeStart is null or ev.scanDateTime >= :createdDateTimeStart) " +
            "and (:createdDateTimeEnd is null or ev.scanDateTime <= :createdDateTimeEnd) order by u.joinDateTime DESC")
    Page<EMRUserEntity> findEventUsersFilterByWX(@Param("orgId") String orgId,
                                                 @Param("productBaseId") String productBaseId,
                                                 @Param("province") String province, @Param("city") String city,
                                                 @Param("createdDateTimeStart") DateTime createdDateTimeStart,
                                                 @Param("createdDateTimeEnd") DateTime createdDateTimeEnd,
                                                 Pageable pageable);


    @Query("select distinct u from #{#entityName} u inner join  u.eventEntities ev where ev.name = 'draw' " +
            "and ev.orgId = :orgId " +
            "and (:productBaseId is null or ev.productBaseId = :productBaseId) " +
            "and u.wxOpenId is not null " +
            "and ev.isPriced is not null " +
            "and (:province is null or :province = '' or u.province like ('%' || :province || '%')) " +
            "and (:city is null or :city = '' or u.city like ('%' || :city || '%')) " +
            "and (:createdDateTimeStart is null or ev.scanDateTime >= :createdDateTimeStart) " +
            "and (:createdDateTimeEnd is null or ev.scanDateTime <= :createdDateTimeEnd) order by u.joinDateTime DESC")
    Page<EMRUserEntity> findEventUsersFilterByDraw(@Param("orgId") String orgId,
                                                   @Param("productBaseId") String productBaseId,
                                                   @Param("province") String province, @Param("city") String city,
                                                   @Param("createdDateTimeStart") DateTime createdDateTimeStart,
                                                   @Param("createdDateTimeEnd") DateTime createdDateTimeEnd,
                                                   Pageable pageable);

    @Query("select distinct u from #{#entityName} u inner join  u.eventEntities ev where ev.name = 'draw' " +
            "and ev.orgId = :orgId " +
            "and (:productBaseId is null or ev.productBaseId = :productBaseId) " +
            "and u.wxOpenId is not null " +
            "and ev.isPriced = 1 " +
            "and (:province is null or :province = '' or u.province like ('%' || :province || '%')) " +
            "and (:city is null or :city = '' or u.city like ('%' || :city || '%')) " +
            "and (:createdDateTimeStart is null or ev.scanDateTime >= :createdDateTimeStart) " +
            "and (:createdDateTimeEnd is null or ev.scanDateTime <= :createdDateTimeEnd) order by u.joinDateTime DESC")
    Page<EMRUserEntity> findEventUsersFilterByWin(@Param("orgId") String orgId,
                                                  @Param("productBaseId") String productBaseId,
                                                  @Param("province") String province, @Param("city") String city,
                                                  @Param("createdDateTimeStart") DateTime createdDateTimeStart,
                                                  @Param("createdDateTimeEnd") DateTime createdDateTimeEnd,
                                                  Pageable pageable);

    @Query("select distinct u from #{#entityName} u inner join  u.eventEntities ev where ev.name = 'draw' " +
            "and ev.orgId = :orgId " +
            "and (:productBaseId is null or ev.productBaseId = :productBaseId) " +
            "and u.wxOpenId is not null " +
            "and ev.isPriced = 1 " +
            "and ev.priceStatusCode in ('submit','paid')  " +
            "and (:province is null or :province = '' or u.province like ('%' || :province || '%')) " +
            "and (:city is null or :city = '' or u.city like ('%' || :city || '%')) " +
            "and (:createdDateTimeStart is null or ev.scanDateTime >= :createdDateTimeStart) " +
            "and (:createdDateTimeEnd is null or ev.scanDateTime <= :createdDateTimeEnd) order by u.joinDateTime DESC")
    Page<EMRUserEntity> findEventUsersFilterByReward(@Param("orgId") String orgId,
                                                     @Param("productBaseId") String productBaseId,
                                                     @Param("province") String province, @Param("city") String city,
                                                     @Param("createdDateTimeStart") DateTime createdDateTimeStart,
                                                     @Param("createdDateTimeEnd") DateTime createdDateTimeEnd,
                                                     Pageable pageable);

}
