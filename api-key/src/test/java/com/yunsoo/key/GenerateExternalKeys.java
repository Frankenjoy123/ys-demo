package com.yunsoo.key;

import com.yunsoo.common.util.RandomUtils;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2016-09-26
 * Descriptions:
 */
public class GenerateExternalKeys {

    @Test
    public void test_getNew_100w() throws IOException {
        StringBuilder stringBuilder1 = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        Map<String, String> dictionary = new HashMap<>();
        for (int i = 0; i < 1000000; i++) {
            String id = RandomUtils.generateString(10, RandomUtils.NUMERIC_CHARS);
            while (dictionary.containsKey(id)) {
                id = RandomUtils.generateString(10, RandomUtils.NUMERIC_CHARS);
            }
            dictionary.put(id, null);
            stringBuilder1.append(String.format("http://zsm.oyao.com/external/2my4cndh6d188vckm5d/%s,%s", id, id)).append("\r\n");
            stringBuilder2.append(String.format("%s", id)).append("\r\n");
        }
        FileOutputStream fileOutputStream1 = new FileOutputStream("key_100w.txt");
        BufferedOutputStream bufferedOutputStream1 = new BufferedOutputStream(fileOutputStream1);
        bufferedOutputStream1.write(stringBuilder1.toString().getBytes("utf-8"));
        bufferedOutputStream1.flush();
        bufferedOutputStream1.close();
        fileOutputStream1.close();

        FileOutputStream fileOutputStream2 = new FileOutputStream("key_100w_.txt");
        BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(fileOutputStream2);
        bufferedOutputStream2.write(stringBuilder2.toString().getBytes("utf-8"));
        bufferedOutputStream2.flush();
        bufferedOutputStream2.close();
        fileOutputStream2.close();
    }

}
