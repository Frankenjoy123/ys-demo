package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MobileVerificationCodeEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

/**
 * Created by  : haitao
 * Created on  : 2016/7/6
 * Descriptions:
 */
public interface MobileVerificationCodeRepository extends FindOneAndSaveRepository<MobileVerificationCodeEntity, String> {
    MobileVerificationCodeEntity findFirstByMobileAndSentDateTimeNotNullOrderBySentDateTimeDesc(String mobile);
}
