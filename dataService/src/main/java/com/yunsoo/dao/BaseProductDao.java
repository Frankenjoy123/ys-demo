package com.yunsoo.dao;

import java.util.List;

import com.yunsoo.dbmodel.BaseProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseProductDao {

    public BaseProductModel getById(int id);

    public void save(BaseProductModel baseProductModel);

    public void update(BaseProductModel baseProductModel);

    public void delete(BaseProductModel baseProductModel);

    public List<BaseProductModel> getAllBaseProducts();

}

