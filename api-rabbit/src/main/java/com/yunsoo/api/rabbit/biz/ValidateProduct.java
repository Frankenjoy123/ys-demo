package com.yunsoo.api.rabbit.biz;

import com.yunsoo.api.rabbit.dto.basic.Logistics;
import com.yunsoo.api.rabbit.dto.basic.Product;
import com.yunsoo.api.rabbit.dto.basic.ScanRecord;
import com.yunsoo.api.rabbit.dto.basic.User;
import com.yunsoo.api.rabbit.object.ValidationResult;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * 验真伪，告知当前扫描的商家产品详细信息。
 * Created by Zhe on 2015/3/11.
 */
public class ValidateProduct {

    public static Comparator<ScanRecord> comparator = (s1, s2) -> s1.getCreatedDateTime().compareTo(s2.getCreatedDateTime());

    public static ValidationResult validateProduct(Product product) {
        return ValidationResult.Real;
    }

    public static ValidationResult validateProduct(Product product, User currentUser, List<ScanRecord> scanRecords) {

        if (scanRecords.size() > 0) {
            Optional<ScanRecord> firstScanRecord = scanRecords.stream().sorted(comparator).findFirst();
            if (firstScanRecord.isPresent()) {
                if (firstScanRecord.get().getUserId().equals(currentUser.getId())) {
                    return ValidationResult.Uncertain;  //虽然第一次是自己扫的，任然需要用户判断是否扫的是同一物品。
                } else {
                    //do-check...根据扫码时间，地点是否和用户当前扫描有明显差异，提醒用户。
                    return ValidationResult.Uncertain;
                }
            }
        }
        return ValidationResult.Real;
    }

    public static ValidationResult validateProduct(Product product, User currentUser, List<ScanRecord> scanRecords, List<Logistics> logistics) {
        //to-do
        return ValidationResult.Real;
    }

}
