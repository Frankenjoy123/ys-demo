package com.yunsoo.dbmodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "product_sub_category")
@XmlRootElement
public class ProductSubCategoryModel {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int Id;
	@Column(name = "category_id")
	private int categoryId;
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		this.Id = id;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
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
}
