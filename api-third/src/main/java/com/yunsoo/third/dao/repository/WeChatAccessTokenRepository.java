package com.yunsoo.third.dao.repository;

import com.yunsoo.third.dao.entity.ThirdWeChatAccessTokenEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Admin on 6/28/2016.
 */
public interface WeChatAccessTokenRepository extends CrudRepository<ThirdWeChatAccessTokenEntity, String> {

    ThirdWeChatAccessTokenEntity findTop1ByAppIdOrderByUpdatedDateTimeDesc(String appId);
}
