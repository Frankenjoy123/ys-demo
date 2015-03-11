package com.yunsoo.service.Impl;


import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.ProductDao;
import com.yunsoo.dao.ProductPackageDao;
import com.yunsoo.dbmodel.ProductPackageModel;
import com.yunsoo.service.ProductPackageService;
import com.yunsoo.service.contract.PackageBoundContract;
import com.yunsoo.service.contract.PackageContract;

import java.util.*;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by: Lijian Created on: 2015/2/1 Descriptions:
 */
@Service("ProductPackageService")
public class ProductPackageServiceImpl implements ProductPackageService {

    @Autowired
    private ProductPackageDao packageDao;

    @Autowired
    private ProductDao productDao;

    /**
     * Only one level once. We do not recursively load the packages.
     *
     * @param key
     * @return
     */
    @Override
    public PackageContract query(String key) {

        ProductPackageModel model = packageDao.getByKey(key);
        if (model == null) {
            throw new IllegalArgumentException("包装码不存在");
        }
        PackageContract p = new PackageContract(model);

        Set<String> subKeys = model.getChildProductKeySet();


        if (subKeys != null && subKeys.size() > 0) {

            List<ProductPackageModel> subModels = packageDao.batchLoad(new HashSet<>(subKeys));
            if (subModels != null && subModels.size() > 0)//they are packages.
            {
                int productCount = 0;
                int packageCount = 0;
                List<PackageContract> subPackages = new ArrayList<PackageContract>();
                for (ProductPackageModel subModel : subModels) {
                    if (subModel.isProduct()) {
                        productCount++;
                    } else {
                        packageCount++;
                        PackageContract subContract = query(subModel.getProductKey());
                        if (subContract != null) {
                            subPackages.add(subContract);
                        }
                    }
                }
                p.setPackageCount(packageCount);
                p.setProductCount(productCount);
                p.setSubPackages(subPackages);
            }

        }
        return p;

    }

    @Override
    public boolean bind(PackageBoundContract packageBoundContract) {

        DateTime now = DateTime.now();
        ProductPackageModel model = new ProductPackageModel();
        model.setProductKey(packageBoundContract.getPackageKey());
        List<String> subKeys = packageBoundContract.getKeys();
        if (packageBoundContract.getPackageKey() == null || subKeys == null || subKeys.size() == 0) {
            throw new IllegalArgumentException("非法数据");
        }
        model.setChildProductKeySet(new HashSet<>(subKeys));
        model.setParentProductKey(null);//currently not know
        model.setCreatedDateTime(now);
        model.setOperator(packageBoundContract.getOperator());
        packageDao.save(model);

        // 开始绑定子包装
        String parentKey = packageBoundContract.getPackageKey();
        long operatorId = packageBoundContract.getOperator();


        List<ProductPackageModel> batchModels = new ArrayList<>();
        // 加载子包装，也有可能是产品。
        List<ProductPackageModel> subModels = packageDao.batchLoad(new HashSet<>(packageBoundContract.getKeys()));
        List<String> existKeys = new ArrayList<>();
        if (subModels != null && subModels.size() > 0) {
            for (ProductPackageModel subModel : subModels) {
                //redefine the parent key, overwrite it.
                //TODO 如果我们要强制要求只有未被绑定过的才可以被绑定，也就是说如果parentkey为空的话，即为未被绑定
                // 如果不为空，则可以抛出异常
                subModel.setParentProductKey(parentKey);
                existKeys.add(subModel.getProductKey());
            }
            batchModels.addAll(subModels);
        }


        for (String key : packageBoundContract.getKeys()) {
            if (existKeys.contains(key)) {
                continue;
            }
            ProductPackageModel subModel = new ProductPackageModel();
            subModel.setProductKey(key);
            subModel.setParentProductKey(parentKey);
            subModel.setCreatedDateTimeValue(now.getMillis());
            subModel.setOperator(operatorId);//可设可不设
            batchModels.add(subModel);
        }
        DaoStatus status = DaoStatus.fail;
        if (batchModels.size() > 0) {
            status = packageDao.batchSave(batchModels);
        }

        return status == DaoStatus.success;
    }

    private List<ProductPackageModel> bindForBatchOperate(PackageBoundContract packageBoundContract) {

        DateTime now = DateTime.now();
        ProductPackageModel model = new ProductPackageModel();
        model.setProductKey(packageBoundContract.getPackageKey());
        List<String> subKeys = packageBoundContract.getKeys();
        model.setChildProductKeySet(new HashSet<>(subKeys));
        model.setParentProductKey(null);//currently not know
        model.setCreatedDateTime(now);
        model.setOperator(packageBoundContract.getOperator());


        // 开始绑定子包装
        String parentKey = packageBoundContract.getPackageKey();
        long operatorId = packageBoundContract.getOperator();


        List<ProductPackageModel> batchModels = new ArrayList<>();
        batchModels.add(model);
        // 加载子包装，也有可能是产品。
        List<ProductPackageModel> subModels = packageDao.batchLoad(new HashSet<>(packageBoundContract.getKeys()));
        List<String> existKeys = new ArrayList<>();
        if (subModels != null && subModels.size() > 0) {
            for (ProductPackageModel subModel : subModels) {
                //redefine the parent key, overwrite it.
                //TODO 如果我们要强制要求只有未被绑定过的才可以被绑定，也就是说如果parentkey为空的话，即为未被绑定
                // 如果不为空，则可以抛出异常
                subModel.setParentProductKey(parentKey);
                existKeys.add(subModel.getProductKey());
            }
            batchModels.addAll(subModels);
        }


        for (String key : packageBoundContract.getKeys()) {
            if (existKeys.contains(key)) {
                continue;
            }
            ProductPackageModel subModel = new ProductPackageModel();
            subModel.setProductKey(key);
            subModel.setParentProductKey(parentKey);
            subModel.setCreatedDateTimeValue(now.getMillis());
            subModel.setOperator(operatorId);//可设可不设
            batchModels.add(subModel);
        }


        return batchModels;
    }

    @Override
    public boolean batchBind(PackageBoundContract[] dataArray) {
        if (dataArray == null) {
            throw new IllegalArgumentException("数据为空");
        }
        List<ProductPackageModel> models = new ArrayList<>();
        for (PackageBoundContract contract : dataArray) {
            List<ProductPackageModel> packageModels = bindForBatchOperate(contract);
            int pos = models.size();
            models.addAll(pos, packageModels);
        }
        //remove duplicated key

        DaoStatus status = packageDao.batchSave(models);


        return status == DaoStatus.success;
    }

    @Override
    public boolean revoke(String key) {
        ProductPackageModel model = packageDao.getByKey(key);
        if (model == null) {
            throw new IllegalArgumentException("包装码不存在");
        }

        Set<String> subKeys = model.getChildProductKeySet();

        List<ProductPackageModel> subModels = packageDao.batchLoad(new HashSet<>(subKeys));
        if (subModels != null && !subModels.isEmpty()) {
            for (ProductPackageModel subModel : subModels) {
                subModel.setParentProductKey(null);
            }
        }
        model.setChildProductKeySet(null);
        model.setStatusId(1); //0 new, 1 revoke

        packageDao.save(model);
        packageDao.batchSave(subModels);
        return true;

    }

    @Override
    public List<String> loadAllKeys(String key) {
        ProductPackageModel model = packageDao.getByKey(key);
        if (model == null) {
            throw new IllegalArgumentException("包装码不存在");
        }

        Set<String> subKeys = model.getChildProductKeySet();

        List<String> keys = new ArrayList<>();
        keys.add(key);
        if (subKeys == null || subKeys.size() == 0) {
            return keys;
        }

        List<ProductPackageModel> subModels = packageDao.batchLoad(new HashSet<>(subKeys));
        if (subModels != null && !subModels.isEmpty()) {
            for (ProductPackageModel subModel : subModels) {
                keys.addAll(keys.size(), loadAllKeys(subModel.getProductKey()));
            }
        }

        return keys;
    }


}
