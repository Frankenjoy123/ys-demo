package com.yunsoo.data.service.dbmodel;

import com.yunsoo.common.data.object.FileObject;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Zhe Zhang
 *         Update properties with DeviceCode, Cellular etc.  2015/1/26
 */
@Entity
@Table(name = "user")
@XmlRootElement
@DynamicUpdate
//@NamedQueries({
//    @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e"),
//    @NamedQuery(name = "Employee.findById", query = "SELECT e FROM Employee e WHERE e.id = :id"),
//    @NamedQuery(name = "Employee.findByAddress", query = "SELECT e FROM Employee e WHERE e.address = :address"),
//    @NamedQuery(name = "Employee.findByName", query = "SELECT e FROM Employee e WHERE e.name = :name")})
public class UserModel {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @Column(name = "address")
    private String address;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "cellular")
    private String cellular;
    @Column(name = "device_code")
    private String deviceCode;
    @Column(name = "thumbnail")
    private String thumbnail;
    @Transient
    private FileObject fileObject;
    @Column(name = "ys_creadit")
    private int ysCreadit;
    @Column(name = "level")
    private int level;
    @Column(name = "status_id")
    private int statusId;
    @Column(name = "created_datetime", nullable = true, updatable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

//    @OneToMany
//    @JoinTable(name="user_organization")
//    @JoinColumn(name = "user_Id")
//    private Set<UserOrganizationModel> userOrganizationModelSet;

    public UserModel() {
    }

    public UserModel(String id) {
        this.id = id;
    }

    public UserModel(String id, String address, String name) {
        this.id = id;
        this.address = address;
        this.name = name;
    }

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

    public void setStatusId(int status) {
        this.statusId = status;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public FileObject getFileObject() {
        return fileObject;
    }

    public void setFileObject(FileObject fileObject) {
        this.fileObject = fileObject;
    }

    public int getYsCreadit() {
        return ysCreadit;
    }

    public void setYsCreadit(int ysCreadit) {
        this.ysCreadit = ysCreadit;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    //    public Set<UserOrganizationModel> getUserOrganizationModelSet() {
//        return userOrganizationModelSet;
//    }
//
//    public void setUserOrganizationModelSet(Set<UserOrganizationModel> userOrganizationModelSet) {
//        this.userOrganizationModelSet = userOrganizationModelSet;
//    }
}