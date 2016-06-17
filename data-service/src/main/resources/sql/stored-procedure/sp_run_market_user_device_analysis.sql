DROP PROCEDURE IF EXISTS `sp_run_market_user_device_analysis`;
CREATE PROCEDURE `sp_run_market_user_device_analysis`(chooseDate DATE)
  BEGIN

    SET SQL_SAFE_UPDATES = 0;

    DROP TABLE IF EXISTS tmp_market_user_device;

    CREATE TEMPORARY TABLE tmp_market_user_device
    (
      draw_date       DATE,
      org_id          CHAR(19),
      user_id         CHAR(19),
      marketing_id    CHAR(19),
      product_base_id CHAR(19),
      device          VARCHAR(50),
      sex             INT(11)
    );


    -- 取前一天
    IF (chooseDate IS NULL)
    THEN
      SET chooseDate = date(DATE_SUB(convert_tz(now(), '+00:00', '+08:00'), INTERVAL 1 DAY));
    END IF;

    -- 插入设备型号
    INSERT INTO tmp_market_user_device
      SELECT
        date(convert_tz(mr.created_datetime, "+00:00", "+08:00")),
        m.org_id,
        u.id,
        mr.marketing_id,
        mr.product_base_id,
        'iPhone',
        CASE u.sex
        WHEN 1
          THEN 1
        WHEN 0
          THEN 0
        ELSE 2
        END
      FROM mkt_draw_record mr
        LEFT JOIN user u ON mr.user_id = u.id COLLATE utf8_general_ci
        -- user表join的目的在与明确与user表关联。
        INNER JOIN marketing m ON mr.marketing_id = m.id COLLATE utf8_general_ci
        INNER JOIN user_scan_record usr ON usr.id = mr.scan_record_id COLLATE utf8_general_ci
      WHERE date(convert_tz(mr.created_datetime, '+00:00', '+08:00')) = chooseDate
            AND (usr.user_agent LIKE '%iphone%' OR usr.user_agent LIKE '%iPad%');

    -- 插入设备型号
    INSERT INTO tmp_market_user_device
      SELECT
        date(convert_tz(mr.created_datetime, "+00:00", "+08:00")),
        m.org_id,
        u.id,
        mr.marketing_id,
        mr.product_base_id,
        'Android',
        CASE u.sex
        WHEN 1
          THEN 1
        WHEN 0
          THEN 0
        ELSE 2
        END
      FROM mkt_draw_record mr
        LEFT JOIN user u ON mr.user_id = u.id COLLATE utf8_general_ci
        -- user表join的目的在与明确与user表关联。
        INNER JOIN marketing m ON mr.marketing_id = m.id COLLATE utf8_general_ci
        INNER JOIN user_scan_record usr ON usr.id = mr.scan_record_id COLLATE utf8_general_ci
      WHERE date(convert_tz(mr.created_datetime, '+00:00', '+08:00')) = chooseDate
            AND (usr.user_agent LIKE '%android%');

    -- 插入设备型号
    INSERT INTO tmp_market_user_device
      SELECT
        date(convert_tz(mr.created_datetime, "+00:00", "+08:00")),
        m.org_id,
        u.id,
        mr.marketing_id,
        mr.product_base_id,
        'Windows',
        CASE u.sex
        WHEN 1
          THEN 1
        WHEN 0
          THEN 0
        ELSE 2
        END
      FROM mkt_draw_record mr
        LEFT JOIN user u ON mr.user_id = u.id COLLATE utf8_general_ci
        -- user表join的目的在与明确与user表关联。
        INNER JOIN marketing m ON mr.marketing_id = m.id COLLATE utf8_general_ci
        INNER JOIN user_scan_record usr ON usr.id = mr.scan_record_id COLLATE utf8_general_ci
      WHERE date(convert_tz(mr.created_datetime, '+00:00', '+08:00')) = chooseDate
            AND (usr.user_agent LIKE '%windows%');

    -- 插入设备型号
    INSERT INTO tmp_market_user_device
      SELECT
        date(convert_tz(mr.created_datetime, "+00:00", "+08:00")),
        m.org_id,
        u.id,
        mr.marketing_id,
        mr.product_base_id,
        'Other',
        CASE u.sex
        WHEN 1
          THEN 1 -- 女
        WHEN 0
          THEN 0 -- 男
        ELSE 2 -- 未知
        END
      FROM mkt_draw_record mr
        LEFT JOIN user u ON mr.user_id = u.id COLLATE utf8_general_ci
        -- user表join的目的在与明确与user表关联。
        INNER JOIN marketing m ON mr.marketing_id = m.id COLLATE utf8_general_ci
        INNER JOIN user_scan_record usr ON usr.id = mr.scan_record_id COLLATE utf8_general_ci
      WHERE date(convert_tz(mr.created_datetime, '+00:00', '+08:00')) = chooseDate
            AND (usr.user_agent NOT LIKE '%windows%' AND usr.user_agent NOT LIKE '%iphone%' AND
                 usr.user_agent NOT LIKE '%ipad%'
                 AND usr.user_agent NOT LIKE '%android%'
            );


    START TRANSACTION;
    -- 删除该天数据
    DELETE FROM emr_market_user_device_analysis
    WHERE date(draw_date) = chooseDate;
    DELETE FROM emr_market_user_gender_analysis
    WHERE date(draw_date) = chooseDate;

    INSERT INTO emr_market_user_device_analysis
      SELECT
        NULL,
        draw_date,
        device,
        count(1),
        org_id,
        marketing_id
      FROM tmp_market_user_device
      GROUP BY draw_date, org_id, marketing_id, device;

    -- 插入性别统计
    INSERT INTO emr_market_user_gender_analysis
      SELECT
        NULL,
        draw_date,
        sex,
        count(1),
        org_id,
        marketing_id
      FROM tmp_market_user_device
      GROUP BY draw_date, org_id, marketing_id, sex;

    INSERT INTO emr_task (task_id, task_name, last_value, created_datetime)
    VALUES (5, '营销用户设备及性别分析', chooseDate, now());
    COMMIT;

    DROP TABLE tmp_market_user_device;


  END;
