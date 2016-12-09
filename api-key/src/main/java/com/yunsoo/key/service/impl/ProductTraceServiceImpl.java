package com.yunsoo.key.service.impl;

import com.yunsoo.key.dao.dao.ProductTraceDao;
import com.yunsoo.key.dao.entity.ProductTraceEntity;
import com.yunsoo.key.dao.model.ProductTraceModel;
import com.yunsoo.key.dao.repository.ProductTraceRepository;
import com.yunsoo.key.dto.ProductTrace;
import com.yunsoo.key.service.ProductTraceService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by:   yan
 * Created on:   10/11/2016
 * Descriptions:
 */
@Service
public class ProductTraceServiceImpl implements ProductTraceService {

    @Autowired
    ProductTraceDao productTraceDao;

    @Autowired
    ProductTraceRepository repository;

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
                traceModel.getCreatedSourceIdList().add(trace.getCreatedSourceId());
                traceModel.getCreatedSourceTypeList().add(trace.getCreatedSourceType());
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

                int length = traceModel.getActionList().size();
                String latestCreatedSourceId = traceModel.getCreatedSourceIdList().get(length-1);
                String latestCreatedSourceType = traceModel.getSourceTypeList().get(length-1);

                String firstCreatedSourceId = model.getCreatedSourceIdList().get(0);
                String firstCreatedSourceType = model.getCreatedSourceTypeList().get(0);

                if(latestCreatedSourceId.equals(firstCreatedSourceId) && latestCreatedSourceType.equals(firstCreatedSourceType)){
                    traceModel.getActionList().remove(length-1);
                    traceModel.getDateTimeList().remove(length - 1);
                    traceModel.getSourceIdList().remove(length - 1);
                    traceModel.getSourceTypeList().remove(length - 1);
                    traceModel.getCreatedSourceIdList().remove(length - 1);
                    traceModel.getCreatedSourceTypeList().remove(length-1);
                }


                traceModel.getActionList().addAll(model.getActionList());
                traceModel.getDateTimeList().addAll(model.getDateTimeList());
                traceModel.getSourceIdList().addAll(model.getSourceIdList());
                traceModel.getSourceTypeList().addAll(model.getSourceTypeList());
                traceModel.getCreatedSourceIdList().addAll(model.getCreatedSourceIdList());
                traceModel.getCreatedSourceTypeList().addAll(model.getCreatedSourceTypeList());
                modelList.add(traceModel);
            }

        });

        productTraceDao.batchSave(modelList);

    }

    @Override
    public void batchSaveInMySql(List<ProductTrace> traceList) {
        //save in db
        int length = traceList.size();
        List<String> idList = traceList.stream().map(trace -> trace.getId()).collect(Collectors.toList());
        List<ProductTraceEntity> entities = repository.findByIdIn(idList);
        entities.forEach(entity->{
            for(int i=0; i<length; i++){
                ProductTrace trace = traceList.get(i);
                if(trace.getId().equals(entity.getId())){
                    if(trace.getProductCount() != null) {
                         entity.setProductCount(trace.getProductCount());
                    }
                    if(trace.getStatusCode()!=null)
                        entity.setStatusCode(trace.getStatusCode());

                    break;
                }
            }

        });

        repository.save(entities);
    }



    @Override
    public ProductTrace saveInMySql(ProductTrace trace) {
        ProductTraceEntity entity = new ProductTraceEntity(trace);

        ProductTraceEntity existEntity = repository.findByProductKeyAndCreatedSourceIdAndCreatedSourceType(trace.getProductKey(), trace.getCreatedSourceId(), trace.getCreatedSourceType());
        if(existEntity != null)
            repository.delete(existEntity);

        repository.save(entity);
        return new ProductTrace(entity);
    }

    @Override
    public List<ProductTrace> search(String status) {
        return repository.findTop500ByStatusCode(status).stream()
                .map(ProductTrace::new).collect(Collectors.toList());

    }

    @Override
    public int getTotalProductCount(String sourceId, String sourceType, String action, DateTime start, DateTime end) {
        return repository.sumProduct(sourceId, sourceType, action, start, end);
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
            trace.setCreatedSourceId(traceModel.getCreatedSourceIdList().get(i));
            trace.setCreatedSourceType(traceModel.getCreatedSourceTypeList().get(i));
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
            traceModel.getCreatedSourceIdList().add(trace.getCreatedSourceId());
            traceModel.getCreatedSourceTypeList().add(trace.getCreatedSourceType());
        }

        return traceModel;
    }

}
