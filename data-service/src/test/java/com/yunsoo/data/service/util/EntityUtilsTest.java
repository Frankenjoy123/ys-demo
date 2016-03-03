package com.yunsoo.data.service.util;

import com.yunsoo.data.service.entity.AccountEntity;
import org.junit.Test;

/**
 * Created by:   Lijian
 * Created on:   2016-03-03
 * Descriptions:
 */
public class EntityUtilsTest {

    @Test
    public void test_patchUpdate(){
        AccountEntity entity = new AccountEntity();
        entity.setId("test_id");
        entity.setFirstName("firstName");
        entity.setLastName("lastName");
        AccountEntity fromEntity = new AccountEntity();
        entity.setId("test_id");
        entity.setFirstName("firstName1");
        entity.setEmail("test@test.com");

        EntityUtils.patchUpdate(entity, fromEntity);
        System.out.println(entity.getId());
        System.out.println(entity.getFirstName());
        System.out.println(entity.getLastName());
        System.out.println(entity.getEmail());
    }

}
