package com.yunsoo.auth.dao.repository;

import com.yunsoo.auth.dao.entity.OAuthAccountEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yan on 10/19/2016.
 */
public interface OAuthAccountRepository extends CrudRepository<OAuthAccountEntity, String> {

    List<OAuthAccountEntity> findBySourceInAndSourceTypeCodeAndDisabled(List<String> source, String sourceTypeCode, Boolean disabled);

    int countBySourceInAndSourceTypeCodeAndDisabled(List<String> source, String sourceTypeCode, Boolean disabled);
}
