package com.yunsoo.service.contract;

import com.yunsoo.dbmodel.UserModel;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhe on 2015/1/26.
 */
public class User {

    private String id;
    private String address;
    private String name;
    private String cellular;
    private String deviceCode;
    private String thumbnail;
    private int statusId;
    private String createdDateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCellular() {
        return cellular;
    }

    public void setCellular(String cellular) {
        this.cellular = cellular;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public static User FromModel(UserModel model) {
        if (model == null) return null;
        User user = new User();
        user.setName(model.getName());
        user.setAddress(model.getAddress());
        user.setCellular(model.getCellular());
        user.setDeviceCode(model.getDeviceCode());
        user.setId(Long.toString(model.getId()));
        user.setThumbnail(model.getThumbnail());
        user.setStatusId(model.getStatusId());
        if (model.getCreatedDateTime() != null) {
            user.setCreatedDateTime(model.getCreatedDateTime().toString());
        }
        return user;
    }

    public static UserModel ToModel(User user) {
        if (user == null) return null;
        UserModel model = new UserModel();
        if (user.getId() != null && !user.getId().isEmpty()) {
            model.setId(Long.parseLong(user.getId()));
        }
        model.setName(user.getName());
        model.setDeviceCode(user.getDeviceCode());
        model.setCellular(user.getCellular());
        model.setAddress(user.getAddress());
        model.setThumbnail(user.getThumbnail());
        model.setStatusId(user.getStatusId());
        if (user.getCreatedDateTime() != null) {
            model.setCreatedDateTime(DateTime.parse(user.getCreatedDateTime()));
        }
        return model;
    }

    //Convert List of UserModel To List of User
    public static List<User> FromModelList(List<UserModel> modelList) {
        if (modelList == null) return null;
        List<User> userList = new ArrayList<User>();
        for (UserModel model : modelList) {
            userList.add(com.yunsoo.service.contract.User.FromModel(model));
        }
        return userList;
    }

    //Convert List of User to List of UserModel
    public static List<UserModel> ToModelList(List<User> userList) {
        if (userList == null) return null;
        List<UserModel> modelList = new ArrayList<UserModel>();
        for (User user : userList) {
            modelList.add(com.yunsoo.service.contract.User.ToModel(user));
        }
        return modelList;
    }
}
