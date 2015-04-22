package com.yunsoo.data.service.dao;

import com.yunsoo.data.service.dbmodel.UserFollowingModel;

import java.util.List;

/**
 * Created by Zhe on 2015/3/24.
 */
public interface UserOrganizationDao {

    public UserFollowingModel get(String userId, String orgId);

    public List<UserFollowingModel> getUserOrgModelByFilter(String userId, String organizationId, Boolean isFollowing);

    public Long save(UserFollowingModel model);

    public DaoStatus patchUpdate(UserFollowingModel model);

}
