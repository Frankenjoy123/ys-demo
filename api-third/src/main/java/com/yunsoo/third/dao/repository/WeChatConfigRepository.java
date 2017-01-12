package com.yunsoo.third.dao.repository;

import com.yunsoo.third.dao.entity.ThirdWeChatConfigEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yan on 12/27/2016.
 */
public interface WeChatConfigRepository extends CrudRepository<ThirdWeChatConfigEntity, String> {

     ThirdWeChatConfigEntity getByOrgId(String orgId);
}
