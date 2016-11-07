package com.yunsoo.di.dao.repository;

import com.yunsoo.di.dao.entity.UserEntity;
import com.yunsoo.di.dao.repository.basic.FindOneAndSaveRepository;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by:   xiaowu
 * Created on:   2016/11/7
 * Descriptions:
 */

public interface UserRepository extends FindOneAndSaveRepository<UserEntity, String> {

    @Query("select distinct o from #{#entityName} o left join  o.luProvinceCityEntity pc left join o.userTagEntities ut  where (:sex is null or o.sex = :sex) and (:phone is null or :phone = '' or o.phone like ('%' || :phone || '%'))  " +
            "and (:name is null or :name = '' or o.name like ('%' || :name || '%')) " +
            "and (:orgId is null or :orgId = '' or o.orgId = :orgId) " +
            "and (:province is null or :province = '' or pc.province like ('%' || :province || '%')) " +
            "and (:city is null or :city = '' or pc.city like ('%' || :city || '%')) " +
            "and ((:ageStart is null or (o.age >= :ageStart)) and (:ageEnd is null or (o.age <= :ageEnd))) " +
            "and (:createdDateTimeStart is null or o.joinDateTime >= :createdDateTimeStart) " +
            "and (:createdDateTimeEnd is null or o.joinDateTime <= :createdDateTimeEnd) " +
            "and (ut.tagId in (:userTags) or :userTagsIgnored = true) " +
            "and ((:wxUser = true and o.wxOpenId is not null) or :wxUser is null or (:wxUser = false and (o.wxOpenId is null or o.wxOpenId = ''))) ")
    Page<UserEntity> findByFilter(@Param("orgId") String orgId, @Param("sex") Boolean sex, @Param("phone") String phone, @Param("name") String name,
                                     @Param("province") String province, @Param("city") String city,
                                     @Param("ageStart") Integer ageStart, @Param("ageEnd") Integer ageEnd,
                                     @Param("createdDateTimeStart") DateTime createdDateTimeStart,
                                     @Param("createdDateTimeEnd") DateTime createdDateTimeEnd,
                                     @Param("userTags") List<String> userTags,
                                     @Param("userTagsIgnored") boolean userTagsIgnored,
                                     @Param("wxUser") Boolean wxUser,
                                     Pageable pageable);

}
