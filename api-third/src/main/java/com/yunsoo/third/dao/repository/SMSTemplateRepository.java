package com.yunsoo.third.dao.repository;

import com.yunsoo.third.dao.entity.ThirdSmsTemplateEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yan on 6/13/2016.
 */
public interface SMSTemplateRepository extends CrudRepository<ThirdSmsTemplateEntity, String> {

    ThirdSmsTemplateEntity findByNameAndSupplier(String name, String supplier);
}
