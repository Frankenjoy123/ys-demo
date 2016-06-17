DROP PROCEDURE IF EXISTS `sp_run_market_user_area_analysis`;
CREATE PROCEDURE `sp_run_market_user_area_analysis`(chooseDate DATE)
  BEGIN

    SET SQL_SAFE_UPDATES = 0;

    DROP TABLE IF EXISTS tmp_market_user_area;

    CREATE TEMPORARY TABLE tmp_market_user_area
    (
      draw_date       DATE,
      org_id          CHAR(19),
      user_id         CHAR(19),
      province        VARCHAR(45),
      city            VARCHAR(45),
      marketing_id    CHAR(19),
      product_base_id CHAR(19),
      sex             BIT(1),
      age             INT(11),
      tag_id          INT,
      tag_name        VARCHAR(50)
    );


    -- 取前一天
    IF (chooseDate IS NULL)
    THEN
      SET chooseDate = date(DATE_SUB(convert_tz(now(), '+00:00', '+08:00'), INTERVAL 1 DAY));
    END IF;

    -- 插入正常的city
    INSERT INTO tmp_market_user_area
      SELECT
        date(convert_tz(mr.created_datetime, "+00:00", "+08:00")),
        m.org_id,
        u.id,
        usr.province,
        usr.city,
        mr.marketing_id,
        mr.product_base_id,
        u.sex,
        u.age,
        tag.id,
        tag.name
      FROM mkt_draw_record mr LEFT JOIN user u ON mr.user_id = u.id COLLATE utf8_general_ci
        INNER JOIN marketing m ON mr.marketing_id = m.id COLLATE utf8_general_ci
        INNER JOIN user_scan_record usr ON usr.id = mr.scan_record_id COLLATE utf8_general_ci
        INNER JOIN lu_province_city l ON l.city = usr.city
        INNER JOIN lu_tag tag ON l.tag_id = tag.id
      WHERE date(convert_tz(mr.created_datetime, '+00:00', '+08:00')) = chooseDate;

    -- 插入正常的city
    INSERT INTO tmp_market_user_area
      SELECT
        date(convert_tz(mr.created_datetime, "+00:00", "+08:00")),
        m.org_id,
        u.id,
        usr.province,
        usr.city,
        mr.marketing_id,
        mr.product_base_id,
        u.sex,
        u.age,
        tag.id,
        tag.name
      FROM mkt_draw_record mr LEFT JOIN user u ON mr.user_id = u.id COLLATE utf8_general_ci
        INNER JOIN marketing m ON mr.marketing_id = m.id COLLATE utf8_general_ci
        INNER JOIN user_scan_record usr ON usr.id = mr.scan_record_id COLLATE utf8_general_ci
        INNER JOIN lu_province_city l ON l.city = concat(usr.city, '市')
        INNER JOIN lu_tag tag ON l.tag_id = tag.id
      WHERE date(convert_tz(mr.created_datetime, '+00:00', '+08:00')) = chooseDate;

    -- 插入非正常的city
    INSERT INTO tmp_market_user_area
      SELECT
        date(convert_tz(mr.created_datetime, "+00:00", "+08:00")),
        m.org_id,
        u.id,
        usr.province,
        usr.city,
        mr.marketing_id,
        mr.product_base_id,
        u.sex,
        u.age,
        tag.id,
        tag.name
      FROM mkt_draw_record mr LEFT JOIN user u ON mr.user_id = u.id COLLATE utf8_general_ci
        INNER JOIN marketing m ON mr.marketing_id = m.id COLLATE utf8_general_ci
        INNER JOIN user_scan_record usr ON usr.id = mr.scan_record_id COLLATE utf8_general_ci
        INNER JOIN lu_tag tag ON tag.id = 5
      WHERE date(convert_tz(mr.created_datetime, '+00:00', '+08:00')) = chooseDate
            AND (usr.city IS NULL OR length(usr.city) = 0 OR usr.city = '未知' OR (usr.city NOT IN (SELECT city
                                                                                                  FROM lu_province_city)
                                                                                 AND concat(usr.city, '市') NOT IN
                                                                                     (SELECT city
                                                                                      FROM lu_province_city)));


    START TRANSACTION;
    -- 删除该天数据
    DELETE FROM emr_market_user_area_analysis
    WHERE date(draw_date) = chooseDate;

    INSERT INTO emr_market_user_area_analysis
      SELECT
        NULL,
        draw_date,
        tag_id,
        count(1),
        org_id,
        marketing_id,
        tag_name
      FROM tmp_market_user_area
      GROUP BY draw_date, org_id, marketing_id, tag_id;

    INSERT INTO emr_task (task_id, task_name, last_value, created_datetime) VALUES (3, '营销用户人群分析', chooseDate, now());
    COMMIT;

    DROP TABLE tmp_market_user_area;


  END;
