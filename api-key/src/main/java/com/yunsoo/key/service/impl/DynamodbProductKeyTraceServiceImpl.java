package com.yunsoo.key.service.impl;

import com.yunsoo.key.dao.dao.ProductTraceDao;
import com.yunsoo.key.dao.model.ProductTraceModel;
import com.yunsoo.key.dto.ProductTrace;
import com.yunsoo.key.service.ProductKeyTraceService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yan on 10/11/2016.
 */
@Service
public class DynamodbProductKeyTraceServiceImpl implements ProductKeyTraceService {

    @Autowired
    ProductTraceDao productTraceDao;

    @Override
    public List<ProductTrace> getByKey(String key) {

        ProductTraceModel traceModel = productTraceDao.getByKey(key);
        if(traceModel == null)
            return null;

        return convertToTraceList(traceModel);
    }

    @Override
    public List<ProductTrace> getProductTraceListByKeys(List<String> keys) {
        List<ProductTrace> traceList = new ArrayList<>();
        List<ProductTraceModel> traceModelList = productTraceDao.batchLoad(keys);
        traceModelList.forEach(model -> {
            List<ProductTrace> convertTraceList = convertToTraceList(model);
            if(convertTraceList != null)
                traceList.addAll(convertTraceList);
        });

        Collections.sort(traceList);

        return traceList;
    }

    @Override
    public void save(ProductTrace trace) {
        productTraceDao.save(toModel(trace));

    }

    @Override
    public void batchSave(List<ProductTrace> traceList) {
        List<ProductTraceModel> modelList = new ArrayList<>();
        Map<String, ProductTraceModel> modelMap = new HashMap<>();
        traceList.forEach(trace -> {
            if(modelMap.containsKey(trace.getProductKey())){
                ProductTraceModel traceModel = modelMap.get(trace.getProductKey());
                traceModel.getActionList().add(trace.getAction());
                traceModel.getDateTimeList().add(trace.getCreatedDateTime().getMillis());
                traceModel.getSourceIdList().add(trace.getSourceId());
                traceModel.getSourceTypeList().add(trace.getSourceType());
            }
            else{
                ProductTraceModel traceModel = new ProductTraceModel(trace);
                modelMap.put(trace.getProductKey(), traceModel);
            }
        });

        modelMap.keySet().forEach(key->{
            ProductTraceModel model = modelMap.get(key);
            ProductTraceModel traceModel = productTraceDao.getByKey(key);
            if(traceModel == null){
                modelList.add(model);
            }
            else{
                traceModel.getActionList().addAll(model.getActionList());
                traceModel.getDateTimeList().addAll(model.getDateTimeList());
                traceModel.getSourceIdList().addAll(model.getSourceIdList());
                traceModel.getSourceTypeList().addAll(model.getSourceTypeList());
                modelList.add(traceModel);
            }

        });

        productTraceDao.batchSave(modelList);

    }

    private List<ProductTrace> convertToTraceList(ProductTraceModel traceModel){
        List<ProductTrace> traceList = new ArrayList<>();
        int size = traceModel.getActionList() == null? 0 : traceModel.getActionList().size();

        if(traceModel.getDateTimeList() == null || size != traceModel.getDateTimeList().size()
                || traceModel.getSourceIdList() == null || size != traceModel.getSourceIdList().size()
                || traceModel.getSourceTypeList() == null || size != traceModel.getSourceTypeList().size())
            return null;

        for(int i=0; i<size; i++){
            ProductTrace trace = new ProductTrace();
            trace.setProductKey(traceModel.getProductKey());
            trace.setAction(traceModel.getActionList().get(i));
            trace.setSourceId(traceModel.getSourceIdList().get(i));
            trace.setSourceType(traceModel.getSourceTypeList().get(i));
            trace.setCreatedDateTime(new DateTime(traceModel.getDateTimeList().get(i)));
            traceList.add(trace);
        }

        return traceList;
    }

    private ProductTraceModel toModel(ProductTrace trace){
        ProductTraceModel traceModel = productTraceDao.getByKey(trace.getProductKey());
        if(traceModel == null){
            new ProductTraceModel(trace);
        }
        else{
            traceModel.getActionList().add(trace.getAction());
            traceModel.getDateTimeList().add(trace.getCreatedDateTime().getMillis());
            traceModel.getSourceIdList().add(trace.getSourceId());
            traceModel.getSourceTypeList().add(trace.getSourceType());
        }

        return traceModel;
    }

}
