package com.yunsoo.data.service.service;

import com.amazonaws.services.s3.model.S3Object;
import com.yunsoo.data.service.service.contract.Organization;

import java.io.IOException;
import java.util.List;

/**
 * Created by Chen Jerry on 3/12/2015.
 */
public interface OrganizationService {
    public Organization get(int id);

    public S3Object getOrgThumbnail(String bucketName, String imgKey) throws IOException;

    public Organization get(String name);

    public long save(Organization org);

    public ServiceOperationStatus update(Organization org);

    public boolean delete(int id, int deleteStatus);

    public List<Organization> getAllOrganizations();
}
