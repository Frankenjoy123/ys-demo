package com.yunsoo.dbmodel;

import com.sun.org.apache.bcel.internal.generic.INEG;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * KB on 3/8/2015
 */
@Entity
@Table(name = "account")
@XmlRootElement
@DynamicUpdate
public class AccountModel {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "status")
    private Integer status;

    @Column(name = "SSID", nullable = true)
    private String SSID;

    @Column(name = "identifier", nullable = true)
    private String identifier;

    @Column(name = "first_name", nullable = true)
    private String firstName;

    @Column(name = "last_name", nullable = true)
    private String lastName;

    @Column(name = "primary_email", nullable = true)
    private String email;

    @Column(name = "primary_phone", nullable = true)
    private String phone;

    @Column(name = "password")
    private String password;

    @Column(name = "salt")
    private String salt;

    @Column(name = "created_ts", updatable = false, nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdTs;

    @Column(name = "updated_ts", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime updatedTs;

    @ManyToOne
    @Column(name = "updated_by", nullable = true)
    private AccountModel updatedBy;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public String getSSID() { return SSID; }
    public void setSSID(String SSID) { this.SSID = SSID; }

    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }

    public DateTime getCreatedTs() { return createdTs; }
    public void setCreatedTs(DateTime createdTs) { this.createdTs = createdTs; }

    public DateTime getUpdatedTs() { return updatedTs; }
    public void setUpdatedTs(DateTime updatedTs) { this.updatedTs = updatedTs; }

    public AccountModel getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(AccountModel updatedBy) {this.updatedBy = updatedBy; }



}