package com.yunsoo.third.dao.repository;

import com.yunsoo.third.dao.entity.ThirdMobileVerificationCodeEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by  : haitao
 * Created on  : 2016/7/6
 * Descriptions:
 */
public interface MobileVerificationCodeRepository extends CrudRepository<ThirdMobileVerificationCodeEntity, String> {
    ThirdMobileVerificationCodeEntity findFirstByMobileAndSentDateTimeNotNullOrderBySentDateTimeDesc(String mobile);
}
