package com.yunsoo.dbmodel;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

/**
 * KB on 3/8/2015
 */
@Entity
@Table(name = "account")
@XmlRootElement
@DynamicUpdate
public class AccountModel {
    private Long id;
    private Integer status;
    private String SSID;
    private String identifier;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String salt;
    private DateTime createdTs;
    private DateTime updatedTs;
    private Long updatedBy;

    @Id
    @GeneratedValue
    @Column(name = "id")
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @Column(name = "status")
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    @Column(name = "SSID", nullable = true)
    public String getSSID() { return SSID; }
    public void setSSID(String SSID) { this.SSID = SSID; }

    @Column(name = "identifier", nullable = true)
    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }

    @Column(name = "first_name", nullable = true)
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    @Column(name = "last_name", nullable = true)
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    @Column(name = "primary_email", nullable = true)
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Column(name = "primary_phone", nullable = true)
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @Column(name = "password")
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Column(name = "salt")
    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }

    @Column(name = "created_ts", updatable = false, nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getCreatedTs() { return createdTs; }
    public void setCreatedTs(DateTime createdTs) { this.createdTs = createdTs; }

    @Column(name = "updated_ts", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getUpdatedTs() { return updatedTs; }
    public void setUpdatedTs(DateTime updatedTs) { this.updatedTs = updatedTs; }

    @Column(name = "updated_by", nullable = true)
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) {this.updatedBy = updatedBy; }

    private Set<AccountTokenModel> tokens;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
    public Set<AccountTokenModel> getTokens() {
        return tokens;
    }
    public void setTokens(Set<AccountTokenModel> tokens) {
        this.tokens = tokens;
    }

}