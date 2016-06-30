package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.UserAccessTokenEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

/**
 * Created by Admin on 6/28/2016.
 */
public interface UserAccessTokenRepository extends FindOneAndSaveRepository<UserAccessTokenEntity, String> {

    UserAccessTokenEntity findTop1ByOrgIdOrderByCreatedDateTimeDesc(String orgId);
}
