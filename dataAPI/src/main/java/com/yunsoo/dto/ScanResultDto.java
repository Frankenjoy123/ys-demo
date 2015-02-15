package com.yunsoo.dto;

import com.yunsoo.dbmodel.OrganizationModel;

import java.util.List;

/**
 * Created by Zhe on 2015/2/12.
 */
public class ScanResultDto {

    private String KeyType;
    private ProductDto Product;
    private OrganizationDto Manufacturer;
    private List<ScanRecordDto> scanRecordDtoList;
    private LogisticsDto logisticsDto;

    public String getKeyType() {
        return KeyType;
    }

    public void setKeyType(String keyType) {
        KeyType = keyType;
    }

    public ProductDto getProduct() {
        return Product;
    }

    public void setProduct(ProductDto product) {
        Product = product;
    }

    public OrganizationDto getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(OrganizationDto manufacturer) {
        Manufacturer = manufacturer;
    }

    public List<ScanRecordDto> getScanRecord() {
        return scanRecordDtoList;
    }

    public void setScanRecord(List<ScanRecordDto> scanRecordDtoList) {
        scanRecordDtoList = scanRecordDtoList;
    }

    public LogisticsDto getLogisticsDto() {
        return logisticsDto;
    }

    public void setLogisticsDto(LogisticsDto logisticsDto) {
        this.logisticsDto = logisticsDto;
    }

}
