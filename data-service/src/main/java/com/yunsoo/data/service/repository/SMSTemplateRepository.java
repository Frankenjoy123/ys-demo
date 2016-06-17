package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.SmsTemplateEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yan on 6/13/2016.
 */
public interface SMSTemplateRepository extends CrudRepository<SmsTemplateEntity, String> {

    SmsTemplateEntity findByName(String name);
}
