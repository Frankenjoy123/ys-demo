package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.EMREventEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EMREventRepository extends FindOneAndSaveRepository<EMREventEntity, String>, CustomEMREventRepository {

   /* @Query("select ev.province,ev.city, count(1) from #{#entityName} ev where name='draw' and ev.marketingId = :markertingId and isPriced =1  group by ev.province, ev.city")
    List<Object[]> queryRewardLocationReport(@Param("markertingId") String marketingId);*/

    @Query("select o from #{#entityName} o where " +
            "(:orgId is null or :orgId = '' or o.orgId = :orgId) " +
            "and (:userId is null or :userId = '' or o.userId = :userId) " +
            "and (:ysId is null or :ysId = '' or o.ysId = :ysId) " +
            "and (:eventDateTimeStart is null or o.eventDateTime >= :eventDateTimeStart) " +
            "and (:eventDateTimeEnd is null or o.eventDateTime <= :eventDateTimeEnd)")
    Page<EMREventEntity> findByFilter(@Param("orgId") String orgId, @Param("userId") String userId, @Param("ysId") String ysId,
                                      @Param("eventDateTimeStart") DateTime eventDateTimeStart,
                                      @Param("eventDateTimeEnd") DateTime eventDateTimeEnd, Pageable pageable);

    @Query("select o from #{#entityName} o where o.name = 'share'" +
            "and o.orgId = :orgId " +
            "and (:productBaseId is null or o.productBaseId = :productBaseId) " +
            "and (:province is null or :province = '' or o.province like ('%' || :province || '%'))" +
            "and (:city is null or :city = '' or o.city like ('%' || :city || '%')) " +
            "and (:createdDateTimeStart is null or o.eventDateTime >= :createdDateTimeStart) " +
            "and (:createdDateTimeEnd is null or o.eventDateTime <= :createdDateTimeEnd) order by o.eventDateTime DESC")
    Page<EMREventEntity> findEventFilterByShare(@Param("orgId") String orgId,
                                                @Param("productBaseId") String productBaseId,
                                                @Param("province") String province, @Param("city") String city,
                                                @Param("createdDateTimeStart") DateTime createdDateTimeStart,
                                                @Param("createdDateTimeEnd") DateTime createdDateTimeEnd,
                                                Pageable pageable);

    @Query("select o from #{#entityName} o where o.name = 'store_url'" +
            "and o.orgId = :orgId " +
            "and (:productBaseId is null or o.productBaseId = :productBaseId) " +
            "and (:province is null or :province = '' or o.province like ('%' || :province || '%'))" +
            "and (:city is null or :city = '' or o.city like ('%' || :city || '%')) " +
            "and (:createdDateTimeStart is null or o.eventDateTime >= :createdDateTimeStart) " +
            "and (:createdDateTimeEnd is null or o.eventDateTime <= :createdDateTimeEnd) order by o.eventDateTime DESC")
    Page<EMREventEntity> findEventFilterByStoreUrl(@Param("orgId") String orgId,
                                                   @Param("productBaseId") String productBaseId,
                                                   @Param("province") String province, @Param("city") String city,
                                                   @Param("createdDateTimeStart") DateTime createdDateTimeStart,
                                                   @Param("createdDateTimeEnd") DateTime createdDateTimeEnd,
                                                   Pageable pageable);

    @Query("select o from #{#entityName} o where o.name = 'comment'" +
            "and o.orgId = :orgId " +
            "and (:productBaseId is null or o.productBaseId = :productBaseId) " +
            "and (:province is null or :province = '' or o.province like ('%' || :province || '%'))" +
            "and (:city is null or :city = '' or o.city like ('%' || :city || '%')) " +
            "and (:createdDateTimeStart is null or o.eventDateTime >= :createdDateTimeStart) " +
            "and (:createdDateTimeEnd is null or o.eventDateTime <= :createdDateTimeEnd) order by o.eventDateTime DESC")
    Page<EMREventEntity> findEventFilterByComment(@Param("orgId") String orgId,
                                                  @Param("productBaseId") String productBaseId,
                                                  @Param("province") String province, @Param("city") String city,
                                                  @Param("createdDateTimeStart") DateTime createdDateTimeStart,
                                                  @Param("createdDateTimeEnd") DateTime createdDateTimeEnd,
                                                  Pageable pageable);

}
