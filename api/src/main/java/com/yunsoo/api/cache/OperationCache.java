package com.yunsoo.api.cache;

import com.yunsoo.api.cache.ObjectKeyGenerator;
import com.yunsoo.common.data.object.OperationLogObject;
import com.yunsoo.common.util.ObjectIdGenerator;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yan on 7/8/2016.
 */
public final class OperationCache {

    static ConcurrentHashMap<String, OperationLogObject> map = new ConcurrentHashMap<>();

    public static void put(OperationLogObject object){
        String key = ObjectIdGenerator.getNew();
        while (map.containsKey(key)){
            key = ObjectIdGenerator.getNew();
        }
        map.put(key, object);
    }

    public static List<OperationLogObject> getAll(){
        List<OperationLogObject> resultList = new ArrayList<>();
        Enumeration<String> keys = map.keys();
        while (keys.hasMoreElements()) {
            String currKey = keys.nextElement();
            resultList.add(map.get(currKey));
            map.remove(currKey);
        }

        return resultList;
    }

}
