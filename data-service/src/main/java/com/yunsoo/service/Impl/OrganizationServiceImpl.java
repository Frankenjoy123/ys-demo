package com.yunsoo.service.Impl;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.OrganizationDao;
import com.yunsoo.dao.S3ItemDao;
import com.yunsoo.dbmodel.OrganizationModel;
import com.yunsoo.service.OrganizationService;
import com.yunsoo.service.ServiceOperationStatus;
import com.yunsoo.service.contract.Organization;
import com.yunsoo.util.SpringBeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * Created by Chen Jerry on 3/12/2015.
 */
@Service("organizationService")
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private S3ItemDao s3ItemDao;

    @Override
    public Organization get(int id) {
        return Organization.FromModel(organizationDao.get(id));
    }

    @Override
    public S3Object getOrgThumbnail(String bucketName, String imgKey) throws IOException {
        try {
            S3Object item = s3ItemDao.getItem(bucketName, imgKey);
            return item;
        } catch (AmazonS3Exception s3ex) {
            if (s3ex.getErrorCode() == "NoSuchKey") {
                //log
            }
            return null;
        } catch (Exception ex) {
            //to-do: log
            return null;
        }
    }

    @Override
    public Organization get(String name) {
        return Organization.FromModel(organizationDao.get(name));
    }

    @Override
    public long save(Organization org) {
        if (org == null ) {
            return -1;
        }
        return organizationDao.save(Organization.ToModel(org));
    }

    @Override
    public ServiceOperationStatus update(Organization org) {
        if (org == null || org.getId() < 0) {
            return ServiceOperationStatus.InvalidArgument;
        }

        OrganizationModel model = new OrganizationModel();
        BeanUtils.copyProperties(org, model,SpringBeanUtil.getNullPropertyNames(org));

        DaoStatus daoStatus = organizationDao.update(model);
        if (daoStatus == DaoStatus.success) return ServiceOperationStatus.Success;
        else return ServiceOperationStatus.Fail;
    }

    @Override
    public boolean delete(int id, int deleteStatus) {
        DaoStatus daoStatus = organizationDao.delete(id, deleteStatus);
        if (daoStatus == DaoStatus.success) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public List<Organization> getAllOrganizations() {
        return Organization.FromModelList(organizationDao.getAllOrganizationModelModels());
    }
}
