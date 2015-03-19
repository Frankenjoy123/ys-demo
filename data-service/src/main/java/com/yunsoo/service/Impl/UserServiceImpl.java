package com.yunsoo.service.Impl;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.S3ItemDao;
import com.yunsoo.dao.UserDao;
import com.yunsoo.dbmodel.UserModel;
import com.yunsoo.model.ThumbnailFile;
import com.yunsoo.service.ServiceOperationStatus;
import com.yunsoo.service.UserService;
import com.yunsoo.service.contract.User;
import com.yunsoo.util.SpringBeanUtil;
import com.yunsoo.util.YunsooConfig;
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

    private String baseBucketName = YunsooConfig.getBaseBucket();
    private String userBaseURL = YunsooConfig.getUserBaseURL();

    @Override
    public User get(Long id) {
        return User.FromModel(userDAO.get(id));
    }

    @Override
    public User get(String cellular) {
        return User.FromModel(userDAO.get(cellular));
    }

    @Override
    public long save(User user) throws Exception {
        if (user == null || user.getDeviceCode().isEmpty()) {
            return -1L;
        }
        user.setStatusId(YunsooConfig.getUserCreatedStatus()); //set newly created status
        long userId = userDAO.save(User.ToModel(user));

        //Check if need to Save thumbnail into S3 bucket
        if (user.getThumbnailFile() != null) {
            String thumbKey = saveUserThumbnail(userId, user.getThumbnailFile());
            //to-do: Async the below update.
            // patch update thumbnail url
            UserModel currentUser = userDAO.get(userId);
            if (currentUser == null) {
                return -2;
            }
            currentUser.setThumbnail(thumbKey);
            DaoStatus result = userDAO.patchUpdate(currentUser);
            return result == DaoStatus.fail ? -3 : userId;
        } else {
            return userId;
        }
//        try (InputStream inputStream = new ByteArrayInputStream(user.getThumbnailFile().getThumbnailData());) {
//            ObjectMetadata objectMetadata = new ObjectMetadata();
//            if (user.getThumbnailFile().getSuffix().toLowerCase() == "jpg" || user.getThumbnailFile().getSuffix().toLowerCase() == "jpeg") {
//                objectMetadata.setContentType("image/jpeg");
//            } else if (user.getThumbnailFile().getSuffix().toLowerCase() == "png") {
//                objectMetadata.setContentType("image/png");
//            }
//            String currentTime = DateTime.now().toLocalDateTime().toString();
//            String key = userBaseURL + "/" + userId + "/thumb-" + currentTime;
//            s3ItemDao.putItem(baseBucketName, key, inputStream, objectMetadata, CannedAccessControlList.Private);
//            //update thumbnail url
//            UserModel currentUser = userDAO.get(userId);
//            if(currentUser == null) {  return -1L; }
//            currentUser.setThumbnail(baseBucketName + "/" + key);
//            DaoStatus result = userDAO.patchUpdate(currentUser);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public ServiceOperationStatus update(User user) throws Exception {
        if (user == null || user.getId().isEmpty()) {
            return ServiceOperationStatus.InvalidArgument;
        }
        if (user.getThumbnailFile() != null) {
            String thumbKey = saveUserThumbnail(Long.parseLong(user.getId()), user.getThumbnailFile());
            user.setThumbnail(thumbKey);
        }
        return userDAO.update(User.ToModel(user)) == DaoStatus.success ? ServiceOperationStatus.Success : ServiceOperationStatus.Fail;
    }

    @Override
    public ServiceOperationStatus patchUpdate(User user) throws Exception {
        if (user.getThumbnailFile() != null) {
            saveUserThumbnail(Long.parseLong(user.getId()), user.getThumbnailFile());
            String thumbKey = saveUserThumbnail(Long.parseLong(user.getId()), user.getThumbnailFile());
            user.setThumbnail(thumbKey);
        }
        UserModel model = getPatchModel(user);
        return userDAO.patchUpdate(model) == DaoStatus.success ? ServiceOperationStatus.Success : ServiceOperationStatus.Fail;
    }

    @Override
    public boolean delete(Long id) {
        DaoStatus daoStatus = userDAO.delete(id, YunsooConfig.getUserDeletedStatus());
        return daoStatus == DaoStatus.success ? true : false;
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return User.FromModelList(userDAO.getAllUsers());
    }

    @Override
    @Transactional
    public List<User> getUsersByFilter(Long id, String deviceCode, String cellular, Integer status) {
        return User.FromModelList(userDAO.getUsersByFilter(id, deviceCode, cellular, status));
    }

    //Save thumbnail into S3 bucket
    private String saveUserThumbnail(long userId, ThumbnailFile thumbnailFile) throws IOException, Exception {
        if (thumbnailFile == null) throw new Exception("ThumbnailFile is null!");
        InputStream inputStream = new ByteArrayInputStream(thumbnailFile.getThumbnailData());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        if (thumbnailFile.getSuffix().toLowerCase() == "jpg" || thumbnailFile.getSuffix().toLowerCase() == "jpeg") {
            objectMetadata.setContentType("image/jpeg");
        } else if (thumbnailFile.getSuffix().toLowerCase() == "png") {
            objectMetadata.setContentType("image/png");
        } else if (thumbnailFile.getSuffix().toLowerCase() == "bmp") {
            objectMetadata.setContentType("image/bmp");
        } else if (thumbnailFile.getSuffix().toLowerCase() == "gif") {
            objectMetadata.setContentType("image/gif");
        }

        String currentTime = DateTime.now().toLocalDateTime().toString();
        String key = userBaseURL + "/" + userId + "/thumb-" + currentTime;
        s3ItemDao.putItem(baseBucketName, key, inputStream, objectMetadata, CannedAccessControlList.PublicRead);
        //to-do: move the old thumb into history folder.

        return key;
    }

    //Convert Dto to Model,just copy properties which is not null.
    private UserModel getPatchModel(User user) {
        UserModel model = new UserModel();
        BeanUtils.copyProperties(user, model, SpringBeanUtil.getNullPropertyNames(user));
        if (user.getId() != null && !user.getId().isEmpty()) {
            model.setId(Long.parseLong(user.getId()));
        }
        return model;
    }
}