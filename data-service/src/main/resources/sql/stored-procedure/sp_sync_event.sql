DROP PROCEDURE IF EXISTS `sp_sync_event`;
CREATE PROCEDURE `sp_sync_event`()
  BEGIN

    DECLARE min_value VARCHAR(50);
    DECLARE min_value_date DATETIME;
    DECLARE max_value_date DATETIME;

    SET SQL_SAFE_UPDATES = 0;

    DROP TABLE IF EXISTS sp_sync_event_tmp_event;

    CREATE TEMPORARY TABLE sp_sync_event_tmp_event
    (
      `name`              VARCHAR(45) NOT NULL
      COMMENT '事件名称',
      `user_id`           CHAR(19)     DEFAULT NULL
      COMMENT 'WHO',
      `ys_id`             VARCHAR(45)  DEFAULT NULL,
      `wx_openid`         VARCHAR(50)  DEFAULT NULL
      COMMENT '微信id',
      `event_datetime`    DATETIME     DEFAULT NULL,
      `ip`                VARCHAR(55)  DEFAULT NULL,
      `user_agent`        VARCHAR(500) DEFAULT NULL,
      `province`          VARCHAR(20)  DEFAULT NULL,
      `city`              VARCHAR(20)  DEFAULT NULL,
      `org_id`            CHAR(19)     DEFAULT NULL,
      `org_name`          VARCHAR(255) DEFAULT NULL,
      `product_base_id`   CHAR(19)     DEFAULT NULL,
      `product_name`      VARCHAR(255) DEFAULT NULL,
      `key_batch_id`      CHAR(19)     DEFAULT NULL,
      `product_key`       VARCHAR(22)  DEFAULT NULL,
      `scan_record_id`    CHAR(19)     DEFAULT NULL,
      `scan_datetime`     DATETIME     DEFAULT NULL,
      `draw_id`           CHAR(19)     DEFAULT NULL
      COMMENT '抽奖id',
      `draw_datetime`     DATETIME     DEFAULT NULL,
      `is_priced`         TINYINT(1)   DEFAULT NULL
      COMMENT '0表示没有抽中，1表示抽中',
      `price_status_code` VARCHAR(20)  DEFAULT NULL
      COMMENT 'paid和submit表示兑奖成功',
      `price_id`          CHAR(19)     DEFAULT NULL,
      `marketing_id`      CHAR(19)     DEFAULT NULL
    );


    SELECT last_value
    INTO min_value
    FROM emr_task
    WHERE task_id = 8
    ORDER BY created_datetime DESC
    LIMIT 1;
    SET min_value_date = ifnull(min_value, '2015-1-1 00:00:00');


    -- 取当前时间的5分前。。
    SET max_value_date = date_sub(current_timestamp(), INTERVAL 5 MINUTE);

    SELECT min_value_date;

    -- 插入扫描事件
    INSERT INTO sp_sync_event_tmp_event
      SELECT
        'scan',
        CASE WHEN usr.user_id = '0020000000000000000'
          THEN ''
        ELSE usr.user_id END,
        CASE WHEN usr.user_id = '0020000000000000000'
          THEN usr.ysid
        ELSE '' END,
        CASE WHEN u.oauth_type_code = 'webchat'
          THEN u.oauth_openid
        ELSE NULL END,
        usr.created_datetime,
        usr.ip,
        usr.user_agent,
        usr.province,
        usr.city,
        org.id,
        org.name,
        pb.id,
        pb.name,
        usr.product_key_batch_id,
        usr.product_key,
        usr.id,
        usr.created_datetime,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL
      FROM user_scan_record usr
        INNER JOIN product_base pb ON usr.product_base_id = pb.id
        INNER JOIN organization org ON org.id = pb.org_id
        LEFT JOIN user u ON usr.user_id = u.id
      WHERE usr.created_datetime >= min_value_date AND usr.created_datetime < max_value_date;

    -- where (u.modified_datetime is null and u.created_datetime >= @max_value_date) or u.modified_datetime >= @max_value_date;
    
    
    
    -- 插入营销事件
    INSERT INTO sp_sync_event_tmp_event
      SELECT
        'draw',
        CASE WHEN usr.user_id = '0020000000000000000'
          THEN ''
        ELSE usr.user_id END,
        CASE WHEN usr.user_id = '0020000000000000000'
          THEN usr.ysid
        ELSE '' END,
        CASE WHEN u.oauth_type_code = 'webchat'
          THEN u.oauth_openid
        ELSE NULL END,
        mdr.created_datetime,
        usr.ip,
        usr.user_agent,
        usr.province,
        usr.city,
        org.id,
        org.name,
        pb.id,
        pb.name,
        usr.product_key_batch_id,
        usr.product_key,
        usr.id,
        usr.created_datetime,
        mdr.id,
        mdr.created_datetime,
        mdr.isPrized,
        mdp.status_code,
        mdp.draw_record_id,
        mdp.marketing_id
      FROM user_scan_record usr
        INNER JOIN product_base pb
          ON usr.product_base_id = pb.id
        INNER JOIN organization org ON org.id = pb.org_id
        INNER JOIN mkt_draw_record mdr ON mdr.scan_record_id = usr.id
        LEFT JOIN mkt_draw_prize mdp ON mdr.scan_record_id = mdp.scan_record_id
        LEFT JOIN user u ON usr.user_id = u.id
      WHERE mdr.created_datetime >= min_value_date AND mdr.created_datetime < max_value_date;

    -- select * from sp_sync_user_tmp_user;

    START TRANSACTION;
    -- 插入user——id
    INSERT INTO emr_event (name, user_id, ys_id, wx_openid, event_datetime, ip, user_agent, province, city,
                           org_id, org_name, product_base_id, product_name, key_batch_id, product_key,
                           scan_record_id, scan_datetime, draw_id, draw_datetime, is_priced, price_status_code, price_id, marketing_id)
      SELECT
        ev.name,
        ev.user_id,
        ev.ys_id,
        ev.wx_openid,
        ev.event_datetime,
        ev.ip,
        ev.user_agent,
        ifnull(l.province, ev.province),
        ifnull(l.city, ev.city),
        ev.org_id,
        ev.org_name,
        ev.product_base_id,
        ev.product_name,
        ev.key_batch_id,
        ev.product_key,
        ev.scan_record_id,
        ev.scan_datetime,
        ev.draw_id,
        ev.draw_datetime,
        ev.is_priced,
        ev.price_status_code,
        ev.price_id,
        ev.marketing_id
      FROM sp_sync_event_tmp_event ev
        LEFT JOIN lu_province_city l ON (l.city = ev.city OR l.city = concat(ev.city, '市'))
      ORDER BY ev.event_datetime ASC;

    INSERT INTO emr_task (task_id, task_name, last_value, created_datetime) VALUES (8, '同步事件信息', max_value_date, now());
    COMMIT;

    DROP TABLE sp_sync_event_tmp_event;


  END;
