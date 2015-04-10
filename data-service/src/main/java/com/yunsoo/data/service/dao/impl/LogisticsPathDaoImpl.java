package com.yunsoo.data.service.dao.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.yunsoo.data.service.dbmodel.LogisticsPathModel;
import com.yunsoo.data.service.dao.LogisticsPathDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jerry on 3/23/2015.
 */
@Repository("logisticsPathDao")
public class LogisticsPathDaoImpl implements LogisticsPathDao {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Override
    public void save(LogisticsPathModel logisticsPathModel) {
        dynamoDBMapper.save(logisticsPathModel);
    }

    @Override
    public void batchSave(List<LogisticsPathModel> logisticsPathModelList){
        dynamoDBMapper.batchSave(logisticsPathModelList);
    }

    @Override
    public void update(LogisticsPathModel logisticsPathModel) {
        dynamoDBMapper.save(logisticsPathModel);
    }

    @Override
    public List<LogisticsPathModel> get(String productKey)
    {
        LogisticsPathModel pathModel = new LogisticsPathModel();
        pathModel.setProductKey(productKey);

        DynamoDBQueryExpression<LogisticsPathModel> queryExpression = new DynamoDBQueryExpression<LogisticsPathModel>()
                .withHashKeyValues(pathModel);

        List<LogisticsPathModel> logisticsPathModelList = dynamoDBMapper.query(LogisticsPathModel.class, queryExpression);

        return logisticsPathModelList;
    }
}
