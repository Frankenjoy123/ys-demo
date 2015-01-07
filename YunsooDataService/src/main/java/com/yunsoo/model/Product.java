package com.yunsoo.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "product")
public class Product implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int Id;
	@Column(name = "PRODUCT_TYPE_ID")
	private int productTypeId;
	@Column(name = "PRODUCT_STATUS_ID")
	private int productStatusId;
	@Column(name = "MANUFACTURING_DATE")
	private Date manufacturingDate;
	@Column(name = "CREATED_DATETIME")
	private Date createdDateTime;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		this.Id = id;
	}

	public int getproductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(int productTypeId) {
		this.productTypeId = productTypeId;
	}

	public int getProductStatusId() {
		return productStatusId;
	}

	public void setProductStatusId(int productStatusId) {
		this.productStatusId = productStatusId;
	}

	public Date getManufacturingDate() {
		return manufacturingDate;
	}

	public void setManufacturingDate(Date manufacturingDate) {
		this.manufacturingDate = manufacturingDate;
	}

	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Date createdDate) {
		this.createdDateTime = createdDate;
	}

}