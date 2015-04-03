package com.yunsoo.dbmodel;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * KB on 3/8/2015
 */
@Entity
@Table(name = "device")
@XmlRootElement
@DynamicUpdate
public class DeviceModel {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "status")
    private Integer status;

    @Column(name = "device_name", nullable = true)
    private String deviceName;

    @Column(name = "device_os", nullable = true)
    private String deviceOS;

    @Column(name = "device_guid")
    private String deviceGUID;

    @Column(name = "device_certificate", nullable = true)
    private String deviceCertificate;

    @Column(name = "created_ts", updatable = false, nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdTs;

    @Column(name = "updated_ts", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime updatedTs;

    @Column(name = "point_id", nullable = true)
    private Integer pointId;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }

    public String getDeviceOS() { return deviceOS; }
    public void setDeviceOS(String deviceOS) { this.deviceOS = deviceOS; }

    public String getDeviceGUID() { return deviceGUID; }
    public void setDeviceGUID(String deviceGUID) { this.deviceGUID = deviceGUID; }

    public String getDeviceCertificate() { return deviceCertificate; }
    public void setDeviceCertificate(String deviceCertificate) { this.deviceCertificate = deviceCertificate;}

    public DateTime getCreatedTs() { return createdTs; }
    public void setCreatedTs(DateTime createdTs) { this.createdTs = createdTs; }

    public DateTime getUpdatedTs() { return updatedTs; }
    public void setUpdatedTs(DateTime updatedTs) { this.updatedTs = updatedTs; }

    public Integer getPointId() { return pointId; }
    public void setPointId(Integer pointId) { this.pointId = pointId; }

}