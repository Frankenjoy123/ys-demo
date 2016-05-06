package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ProductBaseEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by  : haitao
 * Created on  : 2015/7/20
 * Descriptions:
 */
public interface ProductBaseRepository extends FindOneAndSaveRepository<ProductBaseEntity, String> {

    Page<ProductBaseEntity> findByDeletedFalseAndOrgId(String orgId, Pageable pageable);

    Page<ProductBaseEntity> findByDeletedFalse(Pageable pageable);

    Long countByOrgIdAndDeletedFalse(String orgId);

    @Query("select p.id from ProductBaseEntity p where orgId = ?1")
    List<String> findIdByOrgId(String orgId);

    @Query("select o from #{#entityName} o where (:orgId is null or o.orgId = :orgId) and (:proName is null or :proName = '' or o.name like ('%' || :proName || '%'))  " +
            "and (:createAccount is null or :createAccount = '' or o.createdAccountId = :createAccount) and (:createdDateTimeStart is null or o.createdDateTime >= :createdDateTimeStart) " +
            "and (:createdDateTimeEnd is null or o.createdDateTime <= :createdDateTimeEnd)")
    Page<ProductBaseEntity> findByFilter(@Param("orgId") String orgId, @Param("proName") String proName, @Param("createAccount") String createAccount,
                                         @Param("createdDateTimeStart") DateTime createdDateTimeStart,
                                         @Param("createdDateTimeEnd") DateTime createdDateTimeEnd, Pageable pageable);

}
