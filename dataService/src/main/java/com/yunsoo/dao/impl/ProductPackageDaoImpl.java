package com.yunsoo.dao.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.ProductPackageDao;
import com.yunsoo.dbmodel.ProductPackageModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by: Lijian Created on: 2015/2/1 Descriptions:
 */
@Repository("productPackageDao")
public class ProductPackageDaoImpl implements ProductPackageDao {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Override
    public ProductPackageModel getByKey(String key) {
        return dynamoDBMapper.load(ProductPackageModel.class, key);
    }

    @Override
    public DaoStatus save(ProductPackageModel productKeyModel) {
        dynamoDBMapper.save(productKeyModel);
        return DaoStatus.success;
    }

    @Override
    public DaoStatus update(ProductPackageModel productKeyModel) {
        dynamoDBMapper.save(productKeyModel);
        return DaoStatus.success;
    }

    @Override
    public List<ProductPackageModel> batchLoad(Set<String> keys) {
        List<Object> itemsToGet = new ArrayList<>();
        for (String key : keys) {
            ProductPackageModel model = new ProductPackageModel();
            model.setProductKey(key);
            itemsToGet.add(model);
        }

        List<ProductPackageModel> loadedItems = new ArrayList<>();
        Map<String, List<Object>> items = dynamoDBMapper.batchLoad(itemsToGet);
        for (Map.Entry<String, List<Object>> entry : items.entrySet()) {
            String string = entry.getKey();
            System.out.println("retrieve data from table " + string);
            List<Object> list = entry.getValue();
            for (Object obj : list) {
                loadedItems.add((ProductPackageModel) obj);
            }
            //only load one table.
            break;
        }
        return loadedItems;
    }

    @Override
    public DaoStatus batchSave(Set<ProductPackageModel> packages) {

        //normally it works well, but sometimes if the item size is excceeded 400K, then it will fail.
        List<DynamoDBMapper.FailedBatch> outcome = dynamoDBMapper.batchSave(packages);
        return outcome.isEmpty() ? DaoStatus.success : DaoStatus.fail;
    }
}
