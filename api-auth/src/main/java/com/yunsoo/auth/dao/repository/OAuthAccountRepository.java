package com.yunsoo.auth.dao.repository;

import com.yunsoo.auth.dao.entity.OAuthAccountEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by yan on 10/19/2016.
 */
public interface OAuthAccountRepository extends CrudRepository<OAuthAccountEntity, String> {

    @Query("from OAuthAccountEntity where (:hasSource = true and source in (:source)) and (:sourceType is null or :sourceType = sourceTypeCode) " +
            "and (:accountId is null or :accountId=accountId) and (:disabled is null or :disabled=disabled)")
    List<OAuthAccountEntity> query(@Param("hasSource")Boolean hasSource, @Param("source")List<String> source, @Param("sourceType")String sourceTypeCode,
                                   @Param("accountId")String accountId, @Param("disabled")Boolean disabled);

    int countBySourceInAndSourceTypeCodeAndDisabled(List<String> source, String sourceTypeCode, Boolean disabled);

    List<OAuthAccountEntity> findByOAuthTypeCodeAndOAuthOpenIdAndSourceAndSourceTypeCodeAndDisabled(String type, String id,String sourceId, String sourceType, Boolean disabled);

    OAuthAccountEntity getByAccountIdAndDisabled(String id, Boolean disabled);
}
