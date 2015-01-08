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
	@Column(name = "BASE_PRODUCT_ID")
	private int baseProductId;
	@Column(name = "PRODUCT_STATUS_ID")
	private int productStatusId;
	@Column(name = "MANUFACTURING_DATE")
	private Date manufacturingDate;
	@Column(name = "CREATED_DATETIME")
	private Date createdDateTime;
	private BaseProduct baseProduct;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		this.Id = id;
	}

	public int getBaseProducctId() {
		return baseProductId;
	}

	public void setBaseProductId(int baseProductId) {
		this.baseProductId = baseProductId;
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

	public BaseProduct getBaseProduct() {
		return baseProduct;
	}

	public void setBaseProduct(BaseProduct baseProduct) {
		this.baseProduct = baseProduct;
	}

}