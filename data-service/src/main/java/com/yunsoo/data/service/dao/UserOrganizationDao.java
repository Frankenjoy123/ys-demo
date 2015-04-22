package com.yunsoo.data.service.dao;

import com.yunsoo.data.service.dbmodel.UserOrganizationModel;

import java.util.List;

/**
 * Created by Zhe on 2015/3/24.
 */
public interface UserOrganizationDao {

    public UserOrganizationModel get(String userId, String orgId);

    public List<UserOrganizationModel> getUserOrgModelByFilter(String userId, String organizationId, Boolean isFollowing);

    public Long save(UserOrganizationModel model);

    public DaoStatus patchUpdate(UserOrganizationModel model);

}
