package com.yunsoo.di.dao.repository;

import com.yunsoo.di.dao.entity.RankUserEntity;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by yunsu on 2016/11/17.
 */
public interface RankRepository {

    List<RankUserEntity> getRankUsers(String orgId, Integer limit , Integer threshold, String productBaseId , Pageable pageable);

    int getRankUsersCount (String orgId, Integer limit , Integer threshold, String productBaseId);

}
