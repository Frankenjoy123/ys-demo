package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.EMREventEntity;
import com.yunsoo.data.service.entity.EMRUserEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EMREventRepository extends FindOneAndSaveRepository<EMREventEntity, String> {

    @Query("select count(1) from #{#entityName} ev where name= 'scan' " +
            "and ev.orgId =:orgId " +
            "and (:productBaseId is null or ev.productBaseId = :productBaseId) " +
            "and (:province is null or :province = '' or ev.province like ('%' || :province || '%')) " +
            "and (:city is null or :city = '' or ev.city like ('%' || :city || '%')) " +
            "and (:createdDateTimeStart is null or ev.scanDateTime >= :createdDateTimeStart) " +
            "and (:createdDateTimeEnd is null or ev.scanDateTime <= :createdDateTimeEnd)")
    int scanCount(@Param("orgId") String orgId, @Param("productBaseId") String productBaseId,
                  @Param("province") String province, @Param("city") String city,
                  @Param("createdDateTimeStart") DateTime createdDateTimeStart,
                  @Param("createdDateTimeEnd") DateTime createdDateTimeEnd);


    @Query("select count(1) from #{#entityName} ev where ev.name= 'draw' " +
            "and ev.orgId =:orgId " +
            "and (:productBaseId is null or ev.productBaseId = :productBaseId) " +
            "and (:province is null or :province = '' or ev.province like ('%' || :province || '%')) " +
            "and (:city is null or :city = '' or ev.city like ('%' || :city || '%')) " +
            "and ev.wxOpenId is not null " +
            "and (:createdDateTimeStart is null or ev.scanDateTime >= :createdDateTimeStart) " +
            "and (:createdDateTimeEnd is null or ev.scanDateTime <= :createdDateTimeEnd)")
    int drawCount(@Param("orgId") String orgId, @Param("productBaseId") String productBaseId,
                  @Param("province") String province, @Param("city") String city,
                  @Param("createdDateTimeStart") DateTime createdDateTimeStart,
                  @Param("createdDateTimeEnd") DateTime createdDateTimeEnd);

    @Query("select count(1) from #{#entityName} ev where ev.name= 'draw' " +
            "and ev.orgId =:orgId " +
            "and (:productBaseId is null or ev.productBaseId = :productBaseId) " +
            "and (:province is null or :province = '' or ev.province like ('%' || :province || '%')) " +
            "and (:city is null or :city = '' or ev.city like ('%' || :city || '%')) " +
            "and ev.wxOpenId is not null " +
            "and ev.isPriced = 1" +
            "and (:createdDateTimeStart is null or ev.scanDateTime >= :createdDateTimeStart) " +
            "and (:createdDateTimeEnd is null or ev.scanDateTime <= :createdDateTimeEnd)")
    int winCount(@Param("orgId") String orgId, @Param("productBaseId") String productBaseId,
                 @Param("province") String province, @Param("city") String city,
                 @Param("createdDateTimeStart") DateTime createdDateTimeStart,
                 @Param("createdDateTimeEnd") DateTime createdDateTimeEnd);

    @Query("select count(1) from #{#entityName} ev where name= 'draw' " +
            "and ev.orgId =:orgId " +
            "and (:productBaseId is null or ev.productBaseId = :productBaseId) " +
            "and (:province is null or :province = '' or ev.province like ('%' || :province || '%')) " +
            "and (:city is null or :city = '' or ev.city like ('%' || :city || '%')) " +
            "and ev.wxOpenId is not null " +
            "and ev.isPriced = 1 " +
            "and ev.priceStatusCode in ('submit','paid')  " +
            "and (:createdDateTimeStart is null or ev.scanDateTime >= :createdDateTimeStart) " +
            "and (:createdDateTimeEnd is null or ev.scanDateTime <= :createdDateTimeEnd)")
    int rewardCount(@Param("orgId") String orgId, @Param("productBaseId") String productBaseId,
                    @Param("province") String province, @Param("city") String city,
                    @Param("createdDateTimeStart") DateTime createdDateTimeStart,
                    @Param("createdDateTimeEnd") DateTime createdDateTimeEnd);

    @Query("select count(1) from #{#entityName} ev where name= 'scan' " +
            "and ev.orgId =:orgId " +
            "and (:productBaseId is null or ev.productBaseId = :productBaseId) " +
            "and (:province is null or :province = '' or ev.province like ('%' || :province || '%')) " +
            "and (:city is null or :city = '' or ev.city like ('%' || :city || '%')) " +
            "and ev.wxOpenId is not null " +
            "and (:createdDateTimeStart is null or ev.scanDateTime >= :createdDateTimeStart) " +
            "and (:createdDateTimeEnd is null or ev.scanDateTime <= :createdDateTimeEnd)")
    int wxCount(@Param("orgId") String orgId,@Param("productBaseId") String productBaseId,
                @Param("province") String province, @Param("city") String city,
                @Param("createdDateTimeStart") DateTime createdDateTimeStart,
                @Param("createdDateTimeEnd") DateTime createdDateTimeEnd);


}
