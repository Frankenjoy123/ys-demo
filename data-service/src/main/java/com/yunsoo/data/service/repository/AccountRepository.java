package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.AccountEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by:   Lijian
 * Created on:   2015/4/17
 * Descriptions:
 */
public interface AccountRepository extends CrudRepository<AccountEntity, String> {
}
