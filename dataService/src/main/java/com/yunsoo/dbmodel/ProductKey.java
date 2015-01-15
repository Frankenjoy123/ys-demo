package com.yunsoo.dbmodel;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "product_key")
@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "ProductKey.findAll", query = "SELECT k FROM product_key k"),
//    @NamedQuery(name = "ProductKey.findById", query = "SELECT k FROM product_key k WHERE k.id = :id"),
//    @NamedQuery(name = "ProductKey.findByKeyStatusId", query = "SELECT k FROM product_key k WHERE k.KEY_STATUS_ID = :KEY_STATUS_ID"),
//    @NamedQuery(name = "ProductKey.findByProductId", query = "SELECT k FROM product_key k WHERE k.PRODUCT_ID = :PRODUCT_ID")})

public class ProductKey implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	@Basic(optional = false)
	private int Id;

	@Column(name = "PTKEY")
	private String key;

	@Column(name = "KEY_TYPE_ID")
	private int keyTypeId;

	@Column(name = "KEY_STATUS_ID")
	@Basic(optional = false)
	private int keyStatusId;

	@Column(name = "PRODUCT_ID")
	@Basic(optional = false)
	private long productId;

	@Column(name = "CREATED_CLIENT_ID")
	private int createdClientId;

	@Column(name = "CREATED_ACCOUNT_ID")
	private int createdAccountId;

	@Column(name = "CREATED_DATETIME")
	private Date createdDateTime;

	public ProductKey() {
		this.key = "DefaultKEY";
		this.productId = -1L;
		this.keyStatusId = 1; //default
		this.keyTypeId = 0; //default by key type
		this.createdAccountId = 1; //default by account 1
		this.createdClientId = 1; //default by System.
		this.createdDateTime = new Date();
	}
	public ProductKey(String key, String productId) {
		new ProductKey(key,Long.parseLong(productId, 10) );
	}
	
	public ProductKey(String key, long productId) {
		super();
		this.key = key;
		this.productId = productId;
	}
	
	public int getId() {
		return Id;
	}

	public void setId(int id) {
		this.Id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getkeyTypeId() {
		return keyTypeId;
	}

	public void setkeyTypeId(int keyTypeId) {
		this.keyTypeId = keyTypeId;
	}

	public int getkeyStatusId() {
		return keyStatusId;
	}

	public void setkeyStatusId(int keyStatusId) {
		this.keyStatusId = keyStatusId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public int getCreatedClientId() {
		return createdClientId;
	}

	public void setCreatedClientId(int createdClientId) {
		this.createdClientId = createdClientId;
	}

	public int getCreatedAccountId() {
		return createdAccountId;
	}

	public void setCreatedAccountId(int createdAccountId) {
		this.createdAccountId = createdAccountId;
	}

	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Date createdDate) {
		this.createdDateTime = createdDate;
	}

	//Validate the ProductKey object.
	public boolean validate() {
		if (this.key == null || this.key == "") {
			return false;
		}
		if (this.keyStatusId < 0) {
			return false;
		}
		if (this.keyTypeId < 0) {
			return false;
		}
		if (this.productId < 0) {
			return false;
		}
		if (this.createdAccountId < 0) {
			return false;
		}
		if (this.createdClientId < 0) {
			return false;
		}
		return true;
	}

}