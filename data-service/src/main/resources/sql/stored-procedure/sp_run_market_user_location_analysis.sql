DROP PROCEDURE IF EXISTS `sp_run_market_user_location_analysis`;
CREATE PROCEDURE `sp_run_market_user_location_analysis`(chooseDate DATE)
  BEGIN

    SET SQL_SAFE_UPDATES = 0;

    DROP TABLE IF EXISTS tmp_market_user_location;

    CREATE TEMPORARY TABLE tmp_market_user_location
    (
      draw_date       DATE,
      org_id          CHAR(19),
      user_id         CHAR(19),
      province        VARCHAR(45),
      city            VARCHAR(45),
      marketing_id    CHAR(19),
      product_base_id CHAR(19)
    );


    -- 取前一天
    IF (chooseDate IS NULL)
    THEN
      SET chooseDate = date(DATE_SUB(convert_tz(now(), '+00:00', '+08:00'), INTERVAL 1 DAY));
    END IF;

    -- 插入正常的city
    INSERT INTO tmp_market_user_location
      SELECT
        date(convert_tz(mr.created_datetime, "+00:00", "+08:00")),
        m.org_id,
        u.id,
        l.province,
        l.city,
        mr.marketing_id,
        mr.product_base_id
      FROM mkt_draw_record mr LEFT JOIN user u ON mr.user_id = u.id COLLATE utf8_general_ci
        INNER JOIN marketing m ON mr.marketing_id = m.id COLLATE utf8_general_ci
        INNER JOIN user_scan_record usr ON usr.id = mr.scan_record_id COLLATE utf8_general_ci
        INNER JOIN lu_province_city l ON l.city = usr.city
      WHERE date(convert_tz(mr.created_datetime, '+00:00', '+08:00')) = chooseDate;

    -- 插入正常的city
    INSERT INTO tmp_market_user_location
      SELECT
        date(convert_tz(mr.created_datetime, "+00:00", "+08:00")),
        m.org_id,
        u.id,
        l.province,
        l.city,
        mr.marketing_id,
        mr.product_base_id
      FROM mkt_draw_record mr LEFT JOIN user u ON mr.user_id = u.id COLLATE utf8_general_ci
        INNER JOIN marketing m ON mr.marketing_id = m.id COLLATE utf8_general_ci
        INNER JOIN user_scan_record usr ON usr.id = mr.scan_record_id COLLATE utf8_general_ci
        INNER JOIN lu_province_city l ON l.city = concat(usr.city, '市')
      WHERE date(convert_tz(mr.created_datetime, '+00:00', '+08:00')) = chooseDate;

    -- 插入异常地区
    INSERT INTO tmp_market_user_location
      SELECT
        date(convert_tz(mr.created_datetime, "+00:00", "+08:00")),
        m.org_id,
        u.id,
        '未知省份',
        '未知城市',
        mr.marketing_id,
        mr.product_base_id
      FROM mkt_draw_record mr LEFT JOIN user u ON mr.user_id = u.id COLLATE utf8_general_ci
        INNER JOIN marketing m ON mr.marketing_id = m.id COLLATE utf8_general_ci
        INNER JOIN user_scan_record usr ON usr.id = mr.scan_record_id COLLATE utf8_general_ci
      WHERE date(convert_tz(mr.created_datetime, '+00:00', '+08:00')) = chooseDate
            AND (usr.city IS NULL OR length(usr.city) = 0 OR usr.city = '未知');


    START TRANSACTION;
    -- 删除该天数据
    DELETE FROM emr_market_user_location_analysis
    WHERE date(draw_date) = chooseDate;

    INSERT INTO emr_market_user_location_analysis
      SELECT
        NULL,
        draw_date,
        province,
        city,
        count(1),
        org_id,
        marketing_id
      FROM tmp_market_user_location
      GROUP BY draw_date, org_id, marketing_id, province, city;

    INSERT INTO emr_task (task_id, task_name, last_value, created_datetime) VALUES (6, '营销用户地点分析', chooseDate, now());
    COMMIT;

    DROP TABLE tmp_market_user_location;


  END;
