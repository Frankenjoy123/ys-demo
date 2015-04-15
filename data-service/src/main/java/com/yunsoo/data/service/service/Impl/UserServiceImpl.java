package com.yunsoo.data.service.service.Impl;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.yunsoo.data.service.config.AmazonSetting;
import com.yunsoo.data.service.config.DataServiceSetting;
import com.yunsoo.data.service.dao.DaoStatus;
import com.yunsoo.data.service.dao.S3ItemDao;
import com.yunsoo.data.service.dao.UserDao;
import com.yunsoo.data.service.dbmodel.UserModel;
import com.yunsoo.data.service.service.ServiceOperationStatus;
import com.yunsoo.data.service.service.UserService;
import com.yunsoo.data.service.service.contract.User;
import com.yunsoo.data.service.util.SpringBeanUtil;
import com.yunsoo.common.data.object.FileObject;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Zhe Zhang
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDAO;
    @Autowired
    private S3ItemDao s3ItemDao;

    @Autowired
    private DataServiceSetting dataServiceSetting;
    @Autowired
    private AmazonSetting amazonSetting;

    @Override
    public User get(String id) {
        return User.FromModel(userDAO.get(id));
    }

    @Override
    public User getByCellular(String cellular) {
        return User.FromModel(userDAO.get(cellular));
    }

    //to-be refactored
    @Override
    public S3Object getUserThumbnail(String bucketName, String key) throws IOException {

        try {
            S3Object item = s3ItemDao.getItem(bucketName, key);
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


//        ThumbnailFile thumbnail = new ThumbnailFile();

//        S3ObjectInputStream objectContent = item.getObjectContent();

//        thumbnail.setThumbnailData(IOUtils.toByteArray(objectContent));
//
//        String contentType = item.getObjectMetadata().getContentType();
//        if(contentType.equals("image/jpeg")) {
//            thumbnail.setSuffix("jpg");
//        }
//        else if(contentType.equals("image/png")) {
//            thumbnail.setSuffix("png");
//        }
//        else if(contentType.equals("image/bmp")) {
//            thumbnail.setSuffix("bmp");
//        }
//        else if(contentType.equals("image/gif")) {
//            thumbnail.setSuffix("gif");
//        }
//        return thumbnail;
    }

    @Override
    public String save(User user) throws Exception {
        if (user == null || user.getDeviceCode().isEmpty()) {
            return "-1";
        }
        DateTime userCreatedTime = DateTime.now();
        String thumbnailKey = "thumb-" + Long.toString(userCreatedTime.getMillis());
        user.setThumbnail(thumbnailKey);
        user.setStatusId(dataServiceSetting.getUser_created_status_id()); //set newly created status
        String userId = userDAO.save(User.ToModel(user));

        if (user.getFileObject() != null) {
            String thumbKey = saveUserThumbnail(userId, user.getFileObject(), thumbnailKey);
        }
        return userId;

//        //Check if need to Save thumbnail into S3 bucket
//        if (user.getThumbnailFile() != null) {
//            String thumbKey = saveUserThumbnail(userId, user.getThumbnailFile());
//            //to-do: Async the below update.
//            // patch update thumbnail url
//            UserModel currentUser = userDAO.get(userId);
//            if (currentUser == null) {
//                return -2;
//            }
//            currentUser.setThumbnail(thumbKey);
//            DaoStatus result = userDAO.patchUpdate(currentUser);
//            return result == DaoStatus.fail ? -3 : userId;
//        } else {
//            return userId;
//        }
    }

    @Override
    public ServiceOperationStatus update(User user) throws Exception {
        if (user == null || user.getId().isEmpty()) {
            return ServiceOperationStatus.InvalidArgument;
        }
        if (user.getFileObject() != null) {
            DateTime userCreatedTime = DateTime.now();
            String thumbnailKey = "thumb-" + Long.toString(userCreatedTime.getMillis());
            user.setThumbnail(thumbnailKey);
            String thumbKey = saveUserThumbnail(user.getId(), user.getFileObject(), thumbnailKey);
        }
        return userDAO.update(User.ToModel(user)) == DaoStatus.success ? ServiceOperationStatus.Success : ServiceOperationStatus.Fail;
    }

    @Override
    public ServiceOperationStatus patchUpdate(User user) throws Exception {
        if (user.getFileObject() != null) {
            DateTime userCreatedTime = DateTime.now();
            String thumbnailKey = "thumb-" + Long.toString(userCreatedTime.getMillis());
            user.setThumbnail(thumbnailKey);
            saveUserThumbnail(user.getId(), user.getFileObject(), thumbnailKey);
        }
        UserModel model = getPatchModel(user);
        return userDAO.patchUpdate(model) == DaoStatus.success ? ServiceOperationStatus.Success : ServiceOperationStatus.Fail;
    }

    @Override
    public boolean delete(String id) {
        DaoStatus daoStatus = userDAO.delete(id, dataServiceSetting.getMessage_delete_status_id());
        return daoStatus == DaoStatus.success ? true : false;
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return User.FromModelList(userDAO.getAllUsers());
    }

    @Override
    @Transactional
    public List<User> getUsersByFilter(String id, String deviceCode, String cellular, Integer status) {
        return User.FromModelList(userDAO.getUsersByFilter(id, deviceCode, cellular, status));
    }

    //Save thumbnail into S3 bucket
    private String saveUserThumbnail(String userId, FileObject fileObject, String thumbnailKey) throws IOException, Exception {
        if (fileObject == null) throw new Exception("ThumbnailFile is null!");
        InputStream inputStream = new ByteArrayInputStream(fileObject.getThumbnailData());
        //to-do:
        ObjectMetadata objectMetadata = new ObjectMetadata();
        if (fileObject.getSuffix().toLowerCase().equals("jpg") || fileObject.getSuffix().toLowerCase().equals("jpeg")) {
            objectMetadata.setContentType("image/jpeg");
        } else if (fileObject.getSuffix().toLowerCase().equals("png")) {
            objectMetadata.setContentType("image/png");
        } else if (fileObject.getSuffix().toLowerCase().equals("bmp")) {
            objectMetadata.setContentType("image/bmp");
        } else if (fileObject.getSuffix().toLowerCase().equals("gif")) {
            objectMetadata.setContentType("image/gif");
        }

        objectMetadata.setContentLength(fileObject.getLenth()); //set content-length
        String fullKey = amazonSetting.getS3_userbaseurl() + "/" + userId + "/" + thumbnailKey;
        s3ItemDao.putItem(amazonSetting.getS3_basebucket(), fullKey, inputStream, objectMetadata, CannedAccessControlList.BucketOwnerFullControl);
        //to-do: move the old thumb into history folder.

        return fullKey;
    }

    //Convert Dto to Model,just copy properties which is not null.
    private UserModel getPatchModel(User user) {
        UserModel model = new UserModel();
        BeanUtils.copyProperties(user, model, SpringBeanUtil.getNullPropertyNames(user));
        if (user.getId() != null && !user.getId().isEmpty()) {
            model.setId(user.getId());
        }
        return model;
    }
}