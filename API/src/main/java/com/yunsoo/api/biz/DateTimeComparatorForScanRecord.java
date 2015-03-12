package com.yunsoo.api.biz;

import com.yunsoo.api.dto.basic.ScanRecord;
import com.yunsoo.common.util.DateTimeUtils;

import java.util.Comparator;

/**
 * Created by Zhe on 2015/3/12.
 */

public class DateTimeComparatorForScanRecord implements Comparator<ScanRecord> {

    public int compare(ScanRecord st1, ScanRecord st2) {
        if (DateTimeUtils.parse(st1.getCreatedDateTime()) == DateTimeUtils.parse(st2.getCreatedDateTime())) {
            return 0;
        }
        return DateTimeUtils.parse(st1.getCreatedDateTime()).isBefore(DateTimeUtils.parse(st2.getCreatedDateTime())) ? -1 : 1;
    }
};
