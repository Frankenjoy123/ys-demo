package com.yunsoo.dbmodel;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author Zhe Zhang
 *         Update properties with DeviceCode, Cellular etc.  2015/1/26
 */
@Entity
@Table(name = "user")
@XmlRootElement
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserModel)) {
            return false;
        }
        UserModel other = (UserModel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.id.dbmodel.EmployeeModel[ id=" + id + " ]";
    }
}