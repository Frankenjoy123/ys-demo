package com.yunsoo.dbmodel;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product_base")
public class ProductBaseModel {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long Id;
	@Column(name = "CATEGORY_ID")
	private int categoryId;
	@Column(name = "manufacturer_id")
	private int manufacturerId;

	@Column(name = "barcode")
	private String barcode;
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;
	@Column(name = "details")
	private String details;

	@Column(name = "shelf_life")
	private int shelfLife;
	@Column(name = "shelf_life_interval")
	private String shelfLifeInterval;

	@Column(name = "CREATED_DATETIME")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime createdDateTime;
	@Column(name = "is_active")
	private Boolean active;

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		this.Id = id;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int baseProductId) {
		this.categoryId = baseProductId;
	}

	public int getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(int manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	public int getShelfLife() {
		return shelfLife;
	}

	public void setShelfLife(int shelfLife) {
		this.shelfLife = shelfLife;
	}

	public String getShelfLifeInterval() {
		return shelfLifeInterval;
	}

	public void setShelfLifeInterval(String shelfLifeInterval) {
		this.shelfLifeInterval = shelfLifeInterval;
	}

	public DateTime getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(DateTime createdDate) {
		this.createdDateTime = createdDate;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}
