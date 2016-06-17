DROP PROCEDURE IF EXISTS `sp_run_scan_analysis`;
CREATE PROCEDURE `sp_run_scan_analysis`(chooseDate DATE)
  BEGIN

    SET SQL_SAFE_UPDATES = 0;

    DROP TABLE IF EXISTS tmp_user_scan_record;

    CREATE TEMPORARY TABLE tmp_user_scan_record
    (
      id              VARCHAR(32),
      user_id         CHAR(19),
      product_key     VARCHAR(22) BINARY,
      product_base_id CHAR(19),
      app_id          CHAR(19),
      ysid            VARCHAR(32),
      device_id       VARCHAR(40),
      ip              VARCHAR(15),
      lat             DOUBLE,
      lng             DOUBLE,
      province        VARCHAR(20),
      city            VARCHAR(20),
      address         VARCHAR(100),
      details         VARCHAR(100),
      user_agent      VARCHAR(255),
      scan_date       DATE,
      product_name    VARCHAR(255),
      category_id     CHAR(19),
      category_name   VARCHAR(50),
      app_category    VARCHAR(50),
      org_id          CHAR(19),
      org_name        VARCHAR(255)
    );


    -- 取前一天
    IF (chooseDate IS NULL)
    THEN
      SET chooseDate = date(DATE_SUB(convert_tz(now(), '+00:00', '+08:00'), INTERVAL 1 DAY));
    END IF;

    INSERT INTO tmp_user_scan_record
      SELECT
        ifnull(r.ysid, r.user_id),
        r.user_id,
        r.product_key,
        r.product_base_id,
        r.app_id,
        r.ysid,
        r.device_id,
        r.ip,
        r.latitude,
        r.longitude,
        r.province,
        r.city,
        r.address,
        r.details,
        r.user_agent,
        date(convert_tz(r.created_datetime, "+00:00", "+08:00")) AS scan_date,
        pb.name                                                  AS product_name,
        pc.id                                                    AS category_id,
        pc.name                                                  AS category_name,
        ap.name                                                  AS app_category,
        org.id                                                   AS org_id,
        org.name                                                 AS org_name
      FROM user_scan_record r
        INNER JOIN product_base pb ON r.product_base_id = pb.id COLLATE utf8_general_ci
        INNER JOIN organization org ON org.id = pb.org_id COLLATE utf8_general_ci
        LEFT JOIN product_category pc ON pc.id = pb.category_id COLLATE utf8_general_ci
        LEFT JOIN application ap ON r.app_id = ap.id COLLATE utf8_general_ci
      WHERE date(convert_tz(r.created_datetime, '+00:00', '+08:00')) = chooseDate;

    START TRANSACTION;
    -- 删除该天数据
    DELETE FROM emr_scan_record_analysis
    WHERE date(scan_date) = chooseDate;

    INSERT INTO emr_scan_record_analysis SELECT
                                           NULL,
                                           scan_date,
                                           org_id,
                                           product_base_id,
                                           product_name,
                                           '所有批次'                          AS batch_id,
                                           count(1)                        AS pv,
                                           count(DISTINCT id, product_key) AS uv
                                         FROM tmp_user_scan_record
                                         GROUP BY scan_date, org_id, product_base_id, product_name;

    INSERT INTO emr_task (task_id, task_name, last_value, created_datetime) VALUES (1, '统计扫描率', chooseDate, now());
    COMMIT;

    DROP TABLE tmp_user_scan_record;


  END;
