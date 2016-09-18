package com.yunsoo.data.service.repository.impl;

import com.amazonaws.util.StringUtils;
import com.yunsoo.data.service.entity.MktDrawPrizeReportEntity;
import com.yunsoo.data.service.repository.MktDrawPrizeReportRepository;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;

/**
 * Created by  : Haitao
 * Created on  : 2016/9/14
 * Descriptions:
 */
@Repository("mktDrawPrizeReportRepository")
public class MktDrawPrizeReportRepositoryImpl implements MktDrawPrizeReportRepository {

    @PersistenceContext(unitName = "master")
    private EntityManager entityManager;

    @Override
    public List<MktDrawPrizeReportEntity> queryMktDrawPrizeReport(String marketingId, String accountType, String prizeTypeCode, String statusCode, DateTime startTime, DateTime endTime) {

        String sql = "SELECT prize.product_key,prize.amount,prize.mobile, prize.status_code,prize.created_datetime, prize.account_type, prize.prize_account,prize.prize_account_name," +
                " product.name as product_base_name,scan.ip,scan.city, rule.comments as rule_name" +
                " FROM mkt_draw_prize prize " +
                " left join user_scan_record scan on prize.scan_record_id = scan.id " +
                " left join product_base product on scan.product_base_id = product.id " +
                " left join user usr on scan.user_id = usr.id " +
                " inner join mkt_draw_rule rule on prize.draw_rule_id = rule.id " +
                " where prize.marketing_id = :marketingId ";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("marketingId", marketingId);

        if (!StringUtils.isNullOrEmpty(accountType)) {
            sql = sql + " and prize.account_type = :accountType";
            parameters.put("accountType", accountType);
        }

        if (!StringUtils.isNullOrEmpty(prizeTypeCode)) {
            sql = sql + " and prize.prize_type_code = :prizeTypeCode";
            parameters.put("prizeTypeCode", prizeTypeCode);
        }

        if (!StringUtils.isNullOrEmpty(statusCode)) {
            sql = sql + " and prize.status_code = :statusCode";
            parameters.put("statusCode", statusCode);
        }

        if (startTime != null && !org.springframework.util.StringUtils.isEmpty(startTime.toString())) {
            sql = sql + " and prize.created_datetime >=:createdDateTimeStart";
            parameters.put("createdDateTimeStart", startTime.toString("yyyy-MM-dd"));
        }
        if (endTime != null && !org.springframework.util.StringUtils.isEmpty(endTime.toString())) {
            sql = sql + " and prize.created_datetime <=:createdDateTimeEnd";
            parameters.put("createdDateTimeEnd", endTime.toString("yyyy-MM-dd"));
        }

        sql = sql + " order by prize.created_datetime desc";

        Query query = entityManager.createNativeQuery(sql, MktDrawPrizeReportEntity.class);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List list = query.getResultList();
        return list;
    }

}
