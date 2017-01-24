package com.yunsoo.api.di.service;

import com.yunsoo.api.client.DiApiClient;
import com.yunsoo.common.data.object.EMRUserObject;
import com.yunsoo.common.data.object.EMRUserProductEventStasticsObject;
import com.yunsoo.common.data.object.EMRUserReportObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xiaowu on 2016/11/21.
 */
@Service
public class EMRUserService {
    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    private DiApiClient diApiClient;

    public EMRUserObject getEMRUser(String orgId, String userId, String ysId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("user_id", userId)
                .append("ys_id", ysId)
                .build();

        return diApiClient.get("user/id" + query, EMRUserObject.class);
    }

    public Page<EMRUserObject> getEMRUserList(String orgId, Boolean sex, String phone, String name, String province, String city, Integer ageStart, Integer ageEnd, org.joda.time.LocalDate createdDateTimeStart, org.joda.time.LocalDate createdDateTimeEnd, String userTags, Boolean wxUser, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("sex", sex)
                .append("wx_user", wxUser)
                .append("phone", phone)
                .append("name", name)
                .append("province", province)
                .append("city", city)
                .append("user_tags", userTags)
                .append("age_start", ageStart)
                .append("age_end", ageEnd)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .append(pageable)
                .build();

        return diApiClient.getPaged("user" + query, new ParameterizedTypeReference<List<EMRUserObject>>() {
        });
    }

    public EMRUserReportObject getEMRUserFunnelCount(String orgId, String productBaseId, String province, String city, org.joda.time.LocalDate createdDateTimeStart, org.joda.time.LocalDate createdDateTimeEnd, Boolean wxUser) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("province", province)
                .append("city", city)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .append("wx_user", wxUser)
                .build();

        return diApiClient.get("user/funnel" + query, new ParameterizedTypeReference<EMRUserReportObject>() {
        });
    }


    public Page<EMRUserObject> getEMRScanUserList(String orgId, String productBaseId, String province, String city, LocalDate createdDateTimeStart, LocalDate createdDateTimeEnd, Pageable pageable) {
        // 需要加controller 扫码用户的列表
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("province", province)
                .append("city", city)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .append(pageable)
                .build();

        return diApiClient.getPaged("user/scan" + query, new ParameterizedTypeReference<List<EMRUserObject>>() {
        });
    }

    public Page<EMRUserObject> getEMRDrawUserList(String orgId, String productBaseId, String province, String city, LocalDate createdDateTimeStart, LocalDate createdDateTimeEnd, boolean isWXUser,Pageable pageable) {

        // 需要加controller 扫码抽奖的列表
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("province", province)
                .append("city", city)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .append("wx_user",isWXUser)
                .append(pageable)
                .build();

        return diApiClient.getPaged("user/draw" + query, new ParameterizedTypeReference<List<EMRUserObject>>() {
        });
    }

    public Page<EMRUserObject> getEMRWXUserList(String orgId, String productBaseId, String province, String city, LocalDate createdDateTimeStart, LocalDate createdDateTimeEnd, Pageable pageable) {
        //  需要加controller 微信用户的列表
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("province", province)
                .append("city", city)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .append(pageable)
                .build();

        return diApiClient.getPaged("user/wx" + query, new ParameterizedTypeReference<List<EMRUserObject>>() {
        });
    }

    public Page<EMRUserObject> getEMRWinUserList(String orgId, String productBaseId, String province, String city, LocalDate createdDateTimeStart, LocalDate createdDateTimeEnd,boolean isWXUser, Pageable pageable) {
        // 需要加controller 中奖用户的列表
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("province", province)
                .append("city", city)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .append("wx_user",isWXUser)
                .append(pageable)
                .build();

        return diApiClient.getPaged("user/win" + query, new ParameterizedTypeReference<List<EMRUserObject>>() {
        });
    }

    public Page<EMRUserObject> getEMRRewardUserList(String orgId, String productBaseId, String province, String city, LocalDate createdDateTimeStart, LocalDate createdDateTimeEnd, boolean isWXUser,Pageable pageable) {
        // 需要加controller 兑奖用户的列表
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("province", province)
                .append("city", city)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .append("wx_user",isWXUser)
                .append(pageable)
                .build();

        return diApiClient.getPaged("user/reward" + query, new ParameterizedTypeReference<List<EMRUserObject>>() {
        });
    }

/*
    public Page<EMRUserObject> getEMRShareUserList(String orgId, String productBaseId, String province, String city, LocalDate createdDateTimeStart, LocalDate createdDateTimeEnd, Pageable pageable) {
        //  需要加controller 分享用户的列表
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("province", province)
                .append("city", city)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .append(pageable)
                .build();

        return diApiClient.getPaged("user/share" + query, new ParameterizedTypeReference<List<EMRUserObject>>() {
        });
    }

    public Page<EMRUserObject> getEMRStoreUrlUserList(String orgId, String productBaseId, String province, String city, LocalDate createdDateTimeStart, LocalDate createdDateTimeEnd, Pageable pageable) {
        //  需要加controller 点击外链的用户列表
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("province", province)
                .append("city", city)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .append(pageable)
                .build();

        return diApiClient.getPaged("user/store_url" + query, new ParameterizedTypeReference<List<EMRUserObject>>() {
        });
    }

    public Page<EMRUserObject> getEMRCommentUserList(String orgId, String productBaseId, String province, String city, LocalDate createdDateTimeStart, LocalDate createdDateTimeEnd, Pageable pageable) {
        // 需要加controller 评论用户的列表
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("province", province)
                .append("city", city)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .append(pageable)
                .build();

        return diApiClient.getPaged("user/comment" + query, new ParameterizedTypeReference<List<EMRUserObject>>() {
        });
    }

    public EMRActionReportObject getEMRActionReport(String orgId, String productBaseId, String province, String city, LocalDate createdDateTimeStart, LocalDate createdDateTimeEnd) {
        // 需要加controller 用户看板的数据
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("province", province)
                .append("city", city)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .build();

        return diApiClient.get("user/action" + query, new ParameterizedTypeReference<EMRActionReportObject>() {
        });
    }
*/

    /**
     * 获得用户产品的使用情况
     *
     * @param orgId
     * @param userId
     * @param ysId
     * @param createdDateTimeStart
     * @param createdDateTimeEnd
     * @return
     */
    public List<EMRUserProductEventStasticsObject> getEMRUserProductEventStasticsObjects(String orgId, String userId, String ysId, org.joda.time.LocalDate createdDateTimeStart, org.joda.time.LocalDate createdDateTimeEnd) {
        // 需要加controller 用户产品统计，包括各类action的统计
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("user_id", userId)
                .append("ys_id", ysId)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .build();

        return diApiClient.get("user/event_stats" + query, new ParameterizedTypeReference<List<EMRUserProductEventStasticsObject>>() {
        });
    }
}
