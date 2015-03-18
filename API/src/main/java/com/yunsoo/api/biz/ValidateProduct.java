package com.yunsoo.api.biz;

import com.yunsoo.api.dto.basic.Logistics;
import com.yunsoo.api.dto.basic.Product;
import com.yunsoo.api.dto.basic.ScanRecord;
import com.yunsoo.api.dto.basic.User;
import com.yunsoo.api.object.ValidationResult;
import com.yunsoo.common.util.DateTimeUtils;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeFieldType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 验真伪，告知当前扫描的商家产品详细信息。
 * Created by Zhe on 2015/3/11.
 */
public class ValidateProduct {

    public static Comparator<ScanRecord> comparator = (s1, s2) -> DateTimeUtils.parse(s1.getCreatedDateTime()).compareTo(DateTimeUtils.parse(s2.getCreatedDateTime()));
    // public static Comparator<ScanRecord> comparator = Comparator.comparing(sr ->  LocalDateTime.parse(sr.getCreatedDateTime(), formatter));

    public static ValidationResult validateProduct(Product product) {
        return ValidationResult.Real;
    }

    public static ValidationResult validateProduct(Product product, User currentUser, List<ScanRecord> scanRecords) {

//        if (scanRecords.size() > 0) {
//            ScanRecord firstCreatedScanRecord = scanRecords.get(scanRecords.size() - 1);
//            if (firstCreatedScanRecord.getUserId() == currentUser.getId()) {
//                return ValidationResult.Uncertain; //虽然第一次是自己扫的，任然需要用户判断是否扫的是同一物品。
//            } else {
//                //do-check...扫码时间，地点是否和用户当前扫描有明显差异，提醒用户。
//                return ValidationResult.Fake;
//            }
//        }

        if (scanRecords.size() > 0) {
            Optional<ScanRecord> firstScanRecord = scanRecords.stream().sorted(comparator).findFirst();
            if (firstScanRecord.isPresent()) {
                if (firstScanRecord.get().getUserId() == currentUser.getId()) {
                    return ValidationResult.Uncertain;  //虽然第一次是自己扫的，任然需要用户判断是否扫的是同一物品。
                } else {
                    //do-check...根据扫码时间，地点是否和用户当前扫描有明显差异，提醒用户。
                    return ValidationResult.Fake;
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
