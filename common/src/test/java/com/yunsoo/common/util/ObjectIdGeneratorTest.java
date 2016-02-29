package com.yunsoo.common.util;

import org.junit.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by:   Lijian
 * Created on:   2015/4/15
 * Descriptions:
 */
public class ObjectIdGeneratorTest {

    @Test
    public void test_getNew(){
        String id = ObjectIdGenerator.getNew();
        System.out.println(id);
        System.out.println(ObjectIdGenerator.getNew());
        System.out.println(ObjectIdGenerator.getNew());
        System.out.println(ObjectIdGenerator.getNew());
        System.out.println(ObjectIdGenerator.getNew());
        System.out.println(ObjectIdGenerator.getGeneratedDateFromId(id));
    }

    @Test
    public void test_getNewIds() {
        for (int i = 0; i < 34; i++) {
            System.out.println(ObjectIdGenerator.getNew());
        }
    }

    @Test
    public void test_getNew100m() {
        Date date = new Date();
        Set<String> set = new HashSet<>();
        for (int i = 0; i < 1000000; i++) {
            String id = ObjectIdGenerator.getNew();
            if (set.contains(id)) {
                throw new RuntimeException("Id duplicated: " + id);
            }
            set.add(id);
        }
        System.out.println("generate 100m time: " + (new Date().getTime() - date.getTime()));
        System.out.println(set.size());
    }
}
