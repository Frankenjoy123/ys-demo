package com.yunsoo.di.dao.repository;

import com.yunsoo.di.dao.entity.EMRUserEntity;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Created by yunsu on 2016/11/8.
 */
public interface EMRUserRepository {

    int countUsersByFilter (String orgId,Boolean sex,  String phone, String name, String province, String city, Integer ageStart,  Integer ageEnd,
                            DateTime createdDateTimeStart, DateTime createdDateTimeEnd, List<String> userTags,
                            boolean userTagsIgnored, Boolean wxUser);

    List<EMRUserEntity> findUsersByFilter( String orgId,Boolean sex,  String phone, String name, String province, String city, Integer ageStart,  Integer ageEnd,
                                        DateTime createdDateTimeStart, DateTime createdDateTimeEnd, List<String> userTags,
                                        boolean userTagsIgnored, Boolean wxUser, Pageable pageable);

    EMRUserEntity getUser( String orgId,  String userId,  String ysId);
}
