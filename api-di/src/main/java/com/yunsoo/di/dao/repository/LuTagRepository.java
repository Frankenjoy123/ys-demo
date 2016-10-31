package com.yunsoo.di.dao.repository;

import com.yunsoo.di.dao.entity.LuTagEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


/**
 * Created by:   yan
 * Created on:   9/7/2015
 * Descriptions:
 */
public interface LuTagRepository extends CrudRepository<LuTagEntity, Integer> {
    List<LuTagEntity> findAll();
}
