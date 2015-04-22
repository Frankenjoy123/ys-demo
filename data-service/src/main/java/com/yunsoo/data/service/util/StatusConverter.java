package com.yunsoo.data.service.util;

import com.yunsoo.data.service.dao.DaoStatus;
import com.yunsoo.data.service.service.ServiceOperationStatus;

/**
 * Created by Zhe on 2015/4/15.
 */
public class StatusConverter {

    public static ServiceOperationStatus convertFrom(DaoStatus status) {
        if (DaoStatus.NotFound == status) {
            return ServiceOperationStatus.ObjectNotFound;
        } else if (DaoStatus.fail == status) {
            return ServiceOperationStatus.Fail;
        }
        return ServiceOperationStatus.Success;
    }
}
