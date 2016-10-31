package com.yunsoo.di.dao.repository.basic;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/20
 * Descriptions:
 */
@NoRepositoryBean
public interface FindOneAndSaveRepository<T, ID extends Serializable> extends Repository<T, ID> {

    T findOne(ID id);

    <S extends T> S save(S entity);

}
