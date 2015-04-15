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
public class IdGeneratorTest {

    @Test
    public void test_getNew(){
        String id = IdGenerator.getNew();
        System.out.println(id);
        System.out.println(IdGenerator.getNew());
        System.out.println(IdGenerator.getNew());
        System.out.println(IdGenerator.getNew());
        System.out.println(IdGenerator.getNew());
        System.out.println(IdGenerator.getGeneratedDateFromId(id));
    }

    @Test
    public void test_getNew100m() {
        Date date = new Date();
        Set<String> set = new HashSet<>();
        for (int i = 0; i < 1000000; i++) {
            String id = IdGenerator.getNew();
            if (set.contains(id)) {
                throw new RuntimeException("Id duplicated: " + id);
            }
            set.add(id);
        }
        System.out.println("generate 100m time: " + (new Date().getTime() - date.getTime()));
        System.out.println(set.size());
    }
}
