package com.yunsoo.data.api.dto;

import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.data.service.service.contract.User;
import com.yunsoo.data.service.util.SpringBeanUtil;
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
    private Long thumbnailContentLength;
    private Integer ysCreadit;
    private Integer level;
    private String status;
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

    public Long getThumbnailContentLength() {
        return thumbnailContentLength;
    }

    public void setThumbnailContentLength(Long thumbnailContentLength) {
        this.thumbnailContentLength = thumbnailContentLength;
    }

    public Integer getYsCreadit() {
        return ysCreadit;
    }

    public void setYsCreadit(Integer ysCreadit) {
        this.ysCreadit = ysCreadit;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
            FileObject fileObject = new FileObject();
            fileObject.setThumbnailData(userDto.getThumbnailData());
            if (userDto.getThumbnailName() != null && !userDto.getThumbnailName().isEmpty()) {
                fileObject.setName(userDto.getThumbnailName());
            }
            if (userDto.getThumbnailSuffix() != null) {
                fileObject.setSuffix(userDto.getThumbnailSuffix());
            }
            if (userDto.getThumbnailContentType() != null) {
                fileObject.setContentType(userDto.getThumbnailContentType());
            }

            if (userDto.getThumbnailContentLength() != null) {
                fileObject.setLength(userDto.getThumbnailContentLength());
            } else {
                fileObject.setLength((long) (fileObject.getThumbnailData().length));  //calculate length
            }
            user.setFileObject(fileObject); //set user's ThumbnailFile
        }
        return user;
    }

    //convert User into UserDto
    public static UserDto FromUser(User user) {
        if (user == null) return null;
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto, SpringBeanUtil.getNullPropertyNames(user));
        return userDto;
    }
}
