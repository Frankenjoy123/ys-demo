package com.yunsoo.dbmodel;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "product")
public class ProductModel implements Serializable {

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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BASE_PRODUCT_ID", insertable = false, updatable = false, nullable = false)
    private BaseProductModel baseProductModel;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public int getBaseProductId() {
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

    public BaseProductModel getBaseProductModel() {
        return baseProductModel;
    }

    public void setBaseProductModel(BaseProductModel baseProductModel) {
        this.baseProductModel = baseProductModel;
    }

}