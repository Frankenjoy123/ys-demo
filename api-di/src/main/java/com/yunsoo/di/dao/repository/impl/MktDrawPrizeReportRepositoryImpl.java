package com.yunsoo.di.dao.repository.impl;

import com.amazonaws.util.StringUtils;
import com.yunsoo.data.service.entity.MktDrawPrizeReportEntity;
import com.yunsoo.data.service.repository.MktDrawPrizeReportRepository;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
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
                " product.name as product_base_name,scan.ip,scan.city, rule.comments as rule_name,usr.gravatar_url,usr.oauth_openid" +
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

//        Query query = entityManager.createNativeQuery(sql, MktDrawPrizeReportEntity.class);
//        for (String key : parameters.keySet()) {
//            query.setParameter(key, parameters.get(key));
//        }
//        List list = query.getResultList();
//        return list;


        Query query = entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List<Object[]> queryList = query.getResultList();
        List<MktDrawPrizeReportEntity> resultList = new ArrayList<>();
        for (Object[] data : queryList) {
            MktDrawPrizeReportEntity item = new MktDrawPrizeReportEntity();
            item.setProductKey((String) data[0]);
            item.setAmount(Double.valueOf(data[1].toString()));
            item.setMobile((String) data[2]);
            item.setStatusCode((String) data[3]);
            DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S");
            DateTime dateTimeTemp = DateTime.parse((data[4].toString()), format);
            item.setCreatedDateTime(dateTimeTemp);
            item.setAccountType((String) data[5]);
            item.setPrizeAccount(((String) data[6]));
            item.setPrizeAccountName((String) data[7]);
            item.setProductBaseName((String) data[8]);
            item.setIp((String) data[9]);
            item.setCity((String) data[10]);
            item.setRuleName((String) data[11]);
            item.setGravatarUrl((String) data[12]);
            item.setOauthOpenid((String) data[13]);
            resultList.add(item);
        }
        return resultList;
    }

}
