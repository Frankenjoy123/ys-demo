package com.yunsoo.dataapi.dto;

import com.yunsoo.model.ThumbnailFile;
import com.yunsoo.service.contract.User;
import com.yunsoo.util.SpringBeanUtil;
import org.springframework.beans.BeanUtils;

/**
 * Created by Zhe on 2015/3/3.
 */
public class UserDto {
    private String id;
    private String address;
    private String name;
    private String cellular;
    private String deviceCode;
    private String thumbnail;
    //private ThumbnailFile thumbnailFile;
    private byte[] thumbnailData;
    private String thumbnailName;
    private String thumbnailSuffix;
    private String thumbnailContentType;
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

    public byte[] getThumbnailData() {
        return thumbnailData;
    }

    public void setThumbnailData(byte[] thumbnailData) {
        this.thumbnailData = thumbnailData;
    }

    public String getThumbnailName() {
        return thumbnailName;
    }

    public void setThumbnailName(String thumbnailName) {
        this.thumbnailName = thumbnailName;
    }

    public String getThumbnailSuffix() {
        return thumbnailSuffix;
    }

    public void setThumbnailSuffix(String thumbnailSuffix) {
        this.thumbnailSuffix = thumbnailSuffix;
    }

    public String getThumbnailContentType() {
        return thumbnailContentType;
    }

    public void setThumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
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

    //convert UserDto into User
    public static User ToUser(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user, SpringBeanUtil.getNullPropertyNames(userDto));
        if (userDto.getThumbnailData() != null && userDto.getThumbnailData().length > 0) {
            ThumbnailFile thumbnailFile = new ThumbnailFile();
            thumbnailFile.setThumbnailData(userDto.getThumbnailData());
            if (userDto.getThumbnailName() != null && !userDto.getThumbnailName().isEmpty()) {
                thumbnailFile.setName(userDto.getThumbnailName());
            }
            if (userDto.getThumbnailSuffix() != null) {
                thumbnailFile.setSuffix(userDto.getThumbnailSuffix());
            }
            if (userDto.getThumbnailContentType() != null) {
                thumbnailFile.setContentType(userDto.getThumbnailContentType());
            }

            user.setThumbnailFile(thumbnailFile); //set user's ThumbnailFile
        }
        return user;
    }

    //convert User into UserDto
    public static UserDto FromUser(User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto, SpringBeanUtil.getNullPropertyNames(user));
        return userDto;
    }
}
