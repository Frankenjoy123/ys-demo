DROP PROCEDURE IF EXISTS `sp_run_market_user_usage_analysis`;
CREATE PROCEDURE `sp_run_market_user_usage_analysis`(chooseDate DATE)
  BEGIN

    SET SQL_SAFE_UPDATES = 0;

    DROP TABLE IF EXISTS tmp_market_user_usage;

    CREATE TEMPORARY TABLE tmp_market_user_usage
    (
      draw_date       DATE,
      org_id          CHAR(19),
      user_id         CHAR(19),
      marketing_id    CHAR(19),
      product_base_id CHAR(19),
      draw_datetime   DATETIME
    );


    -- 取前一天
    IF (chooseDate IS NULL)
    THEN
      SET chooseDate = date(DATE_SUB(convert_tz(now(), '+00:00', '+08:00'), INTERVAL 1 DAY));
    END IF;

    -- 插入对小时的分析
    INSERT INTO tmp_market_user_usage
      SELECT
        date(convert_tz(mr.created_datetime, "+00:00", "+08:00")),
        m.org_id,
        u.id,
        mr.marketing_id,
        mr.product_base_id,
        convert_tz(mr.created_datetime, "+00:00", "+08:00") AS draw_datetime
      FROM mkt_draw_record mr LEFT JOIN user u ON mr.user_id = u.id COLLATE utf8_general_ci
        INNER JOIN marketing m ON mr.marketing_id = m.id COLLATE utf8_general_ci
        INNER JOIN user_scan_record usr ON usr.id = mr.scan_record_id COLLATE utf8_general_ci
      WHERE date(convert_tz(mr.created_datetime, '+00:00', '+08:00')) = chooseDate;

    START TRANSACTION;
    -- 删除该天数据
    DELETE FROM emr_market_user_usage_analysis
    WHERE draw_date = chooseDate;

    -- 睡觉时间
    INSERT INTO emr_market_user_usage_analysis
      SELECT
        NULL,
        draw_date,
        '00:00-06:00',
        count(1),
        org_id,
        marketing_id
      FROM tmp_market_user_usage
      WHERE hour(draw_datetime) < 6
      GROUP BY draw_date, org_id, marketing_id;

    -- 早上时间
    INSERT INTO emr_market_user_usage_analysis
      SELECT
        NULL,
        draw_date,
        '06:00-08:00',
        count(1),
        org_id,
        marketing_id
      FROM tmp_market_user_usage
      WHERE hour(draw_datetime) >= 6 AND hour(draw_datetime) < 8
      GROUP BY draw_date, org_id, marketing_id;

    -- 上午上班时间
    INSERT INTO emr_market_user_usage_analysis
      SELECT
        NULL,
        draw_date,
        '08:00-12:00',
        count(1),
        org_id,
        marketing_id
      FROM tmp_market_user_usage
      WHERE hour(draw_datetime) >= 8 AND hour(draw_datetime) < 12
      GROUP BY draw_date, org_id, marketing_id;

    -- 中午午休时间
    INSERT INTO emr_market_user_usage_analysis
      SELECT
        NULL,
        draw_date,
        '12:00-14:00',
        count(1),
        org_id,
        marketing_id
      FROM tmp_market_user_usage
      WHERE hour(draw_datetime) >= 12 AND hour(draw_datetime) < 14
      GROUP BY draw_date, org_id, marketing_id;

    -- 下午上班时间
    INSERT INTO emr_market_user_usage_analysis
      SELECT
        NULL,
        draw_date,
        '14:00-16:00',
        count(1),
        org_id,
        marketing_id
      FROM tmp_market_user_usage
      WHERE hour(draw_datetime) >= 14 AND hour(draw_datetime) < 16
      GROUP BY draw_date, org_id, marketing_id;

    -- 下午时间
    INSERT INTO emr_market_user_usage_analysis
      SELECT
        NULL,
        draw_date,
        '16:00-18:00',
        count(1),
        org_id,
        marketing_id
      FROM tmp_market_user_usage
      WHERE hour(draw_datetime) >= 16 AND hour(draw_datetime) < 18
      GROUP BY draw_date, org_id, marketing_id;

    -- 晚上活动时间
    INSERT INTO emr_market_user_usage_analysis
      SELECT
        NULL,
        draw_date,
        '18:00-22:00',
        count(1),
        org_id,
        marketing_id
      FROM tmp_market_user_usage
      WHERE hour(draw_datetime) >= 18 AND hour(draw_datetime) < 22
      GROUP BY draw_date, org_id, marketing_id;

    -- 晚上睡觉时间
    INSERT INTO emr_market_user_usage_analysis
      SELECT
        NULL,
        draw_date,
        '22:00-24:00',
        count(1),
        org_id,
        marketing_id
      FROM tmp_market_user_usage
      WHERE hour(draw_datetime) >= 22 AND hour(draw_datetime) <= 23
      GROUP BY draw_date, org_id, marketing_id;


    INSERT INTO emr_task (task_id, task_name, last_value, created_datetime)
    VALUES (4, '营销用户人群使用时间分析', chooseDate, now());
    COMMIT;

    DROP TABLE tmp_market_user_usage;


  END;
