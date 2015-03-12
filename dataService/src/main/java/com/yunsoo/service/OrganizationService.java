package com.yunsoo.service;

import com.yunsoo.service.contract.Organization;

import java.util.List;

/**
 * Created by Chen Jerry on 3/12/2015.
 */
public interface OrganizationService {
    public Organization get(int id);

    public Organization get(String name);

    public long save(Organization org);

    public ServiceOperationStatus update(Organization org);

    public boolean delete(int id, int deleteStatus);

    public List<Organization> getAllOrganizations();
}
