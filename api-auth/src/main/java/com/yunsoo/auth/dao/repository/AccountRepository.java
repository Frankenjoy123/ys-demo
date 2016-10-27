package com.yunsoo.auth.dao.repository;

import com.yunsoo.auth.dao.entity.AccountEntity;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/4/17
 * Descriptions:
 */
public interface AccountRepository extends Repository<AccountEntity, String> {

    AccountEntity findOne(String id);

    List<AccountEntity> findAll(Iterable<String> ids);

    List<AccountEntity> findByOrgId(String orgId);

    List<AccountEntity> findByOrgIdAndIdentifier(String orgId, String identifier);

    AccountEntity save(AccountEntity entity);

    List<AccountEntity> findByTypeCodeAndOrgId(String typeCode, String orgId);

    @Query("select acc from #{#entityName} acc " +
            "where (:orgId is null or acc.orgId = :orgId) " +
            "and (:statusCode is null or acc.statusCode = :statusCode) " +
            "and (:searchText is null " +
            "" + "or acc.identifier like ('%' || :searchText || '%') " +
            "" + "or concat(acc.lastName, acc.firstName) like ('%' || :searchText || '%') " +
            "" + "or acc.phone like ('%' || :searchText || '%') " +
            "" + "or acc.email like ('%' || :searchText || '%')) " +
            "and (:createdDateTimeGE is null or acc.createdDateTime >= :createdDateTimeGE) " +
            "and (:createdDateTimeLE is null or acc.createdDateTime <= :createdDateTimeLE) " +
            "and (:typeCode is null or acc.typeCode = :typeCode) ")
    Page<AccountEntity> search(@Param("orgId") String orgId,
                               @Param("statusCode") String statusCode,
                               @Param("searchText") String searchText,
                               @Param("createdDateTimeGE") DateTime createdDateTimeGE,
                               @Param("createdDateTimeLE") DateTime createdDateTimeLE,
                               @Param("typeCode") String typeCode,
                               Pageable pageable);

    long countByOrgId(String orgId);

    long countByOrgIdAndStatusCodeIn(String orgId, List<String> statusCodeIn);

}
