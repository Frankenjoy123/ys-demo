package com.yunsoo.dao;

import com.yunsoo.dbmodel.OrganizationModel;

import java.util.List;

/**
 * Created by Chen Jerry on 3/12/2015.
 */
public interface OrganizationDao {
    public OrganizationModel get(long id);

    public OrganizationModel get(String name);

    public long save(OrganizationModel organizationModelModel);

    public DaoStatus update(OrganizationModel organizationModelModel);

    public DaoStatus delete(long id, int deleteStatus);

    public List<OrganizationModel> getAllOrganizationModelModels();
}
