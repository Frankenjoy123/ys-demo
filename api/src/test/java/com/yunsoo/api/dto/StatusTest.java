package com.yunsoo.api.dto;

import org.junit.Test;

/**
 * Created by:   Lijian
 * Created on:   2015/2/27
 * Descriptions:
 */
public class StatusTest {

    @Test
    public void compareStatus() {
        ProductStatus ps1 = ProductStatus.NEW;
        ProductStatus ps2 = new ProductStatus(0, "NEW");
        assert ps1.equals(ps2);
        assert ps1.compareTo(ps2) == 0;

    }
}
