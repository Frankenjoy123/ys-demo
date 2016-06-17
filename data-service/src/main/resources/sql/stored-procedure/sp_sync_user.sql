DROP PROCEDURE IF EXISTS `sp_sync_user`;
CREATE PROCEDURE `sp_sync_user`()
  BEGIN

    DECLARE min_value VARCHAR(50);
    DECLARE min_value_date DATETIME;
    DECLARE max_value_date DATETIME;

    SET SQL_SAFE_UPDATES = 0;

    DROP TABLE IF EXISTS sp_sync_user_tmp_user;

    CREATE TEMPORARY TABLE sp_sync_user_tmp_user
    (
      `user_id`         CHAR(19)     DEFAULT NULL
      COMMENT '用户id',
      `ys_id`           VARCHAR(32)  DEFAULT NULL
      COMMENT 'ysid，当匿名访问时，ysid作为唯一区分用户的id',
      `org_id`          CHAR(19)     DEFAULT NULL
      COMMENT '品牌商id',
      `org_name`        VARCHAR(255) DEFAULT NULL
      COMMENT '品牌商名字',
      `name`            VARCHAR(45)  DEFAULT NULL,
      `phone`           VARCHAR(20)  DEFAULT NULL
      COMMENT '手机',
      `email`           VARCHAR(45)  DEFAULT NULL,
      `age`             INT(11)      DEFAULT NULL
      COMMENT '年龄',
      `sex`             BIT(1)       DEFAULT NULL
      COMMENT '性别',
      `gravatar_url`    VARCHAR(255) DEFAULT NULL
      COMMENT '头像',
      `wx_openid`       VARCHAR(50)  DEFAULT NULL
      COMMENT '微信id',
      `province`        VARCHAR(45)  DEFAULT NULL
      COMMENT '省',
      `city`            VARCHAR(45)  DEFAULT NULL
      COMMENT '城市',
      `address`         VARCHAR(100) DEFAULT NULL
      COMMENT '地址',
      `join_datetime`   DATETIME     DEFAULT NULL
      COMMENT '加入时间',
      `last_event_ip`   VARCHAR(55)  DEFAULT NULL
      COMMENT 'ip',
      `last_user_agent` VARCHAR(255) DEFAULT NULL
      COMMENT '设备'
    );


    SELECT last_value
    INTO min_value
    FROM emr_task
    WHERE task_id = 7
    ORDER BY created_datetime DESC
    LIMIT 1;
    SET min_value_date = ifnull(min_value, '2015-1-1 00:00:00');


    -- 取当前时间的5分前。。
    SET max_value_date = date_sub(current_timestamp(), INTERVAL 5 MINUTE);

    SELECT min_value_date;

    -- 先读取user表中新增用户的信息，因为有可能不需要scan也能有user的情况。
    INSERT INTO sp_sync_user_tmp_user
      SELECT
        u.id,
        '',
        org.id   AS 'org_id',
        org.name AS 'org_name',
        u.name,
        u.phone,
        u.email,
        u.age,
        u.sex,
        u.gravatar_url,
        CASE WHEN u.oauth_type_code = 'webchat'
          THEN u.oauth_openid
        ELSE NULL END,
        ifnull(u.province, usr2.province),
        ifnull(u.city, usr2.city),
        ifnull(u.address, usr2.address),
        usr2.created_datetime,
        usr2.ip,
        usr2.user_agent
      FROM user u
        INNER JOIN (
                     SELECT
                       usr.user_id,
                       pb.org_id,
                       min(usr.created_datetime) AS 'created_datetime',
                       usr.id,
                       province,
                       city,
                       address,
                       usr.ip,
                       usr.user_agent
                     FROM yunsoo2015DB.user_scan_record usr INNER JOIN product_base pb
                         ON usr.product_base_id = pb.id
                     GROUP BY usr.user_id, pb.org_id
                     ORDER BY usr.id DESC) AS usr2 ON u.id = usr2.user_id
        INNER JOIN organization org ON usr2.org_id = org.id
      WHERE u.created_datetime >= min_value_date AND u.created_datetime < max_value_date;
    -- where (u.modified_datetime is null and u.created_datetime >= @max_value_date) or u.modified_datetime >= @max_value_date;

    -- 插入ysId不为空，而user——id为空或者固定的。用来读取匿名用户
    INSERT INTO sp_sync_user_tmp_user
      SELECT
        '',
        usr.ysid,
        org.id   AS 'org_id',
        org.name AS 'org_name',
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        usr.province,
        usr.city,
        usr.address,
        usr.created_datetime,
        usr.ip,
        usr.user_agent
      FROM user_scan_record usr INNER JOIN product_base pb
          ON usr.product_base_id = pb.id
        INNER JOIN organization org ON pb.org_id = org.id
      WHERE usr.created_datetime >= min_value_date AND usr.created_datetime < max_value_date
            AND (usr.user_id IS NULL OR usr.user_id = '0020000000000000000')
      GROUP BY usr.ysid, org.id
      ORDER BY usr.id DESC;

    -- select * from sp_sync_user_tmp_user;

    -- select * from sp_sync_user_tmp_user;

    START TRANSACTION;

    -- 先删除部分数据

    DELETE u FROM emr_user u
      INNER JOIN sp_sync_user_tmp_user tmp
        ON u.org_id = tmp.org_id AND (u.user_id = tmp.user_id AND u.ys_id = tmp.ys_id);


    -- 插入user——id
    INSERT INTO emr_user (user_id, ys_id, org_id, org_name, name, phone, email, age, sex, gravatar_url, wx_openid, province, city, address, join_datetime, latest_event_ip, latest_event_device)
      SELECT
        usr.user_id,
        usr.ys_id,
        usr.org_id,
        usr.org_name,
        usr.name,
        usr.phone,
        usr.email,
        usr.age,
        usr.sex,
        usr.gravatar_url,
        usr.wx_openid,
        ifnull(l.province, usr.province),
        ifnull(l.city, usr.city),
        usr.address,
        usr.join_datetime,
        usr.last_event_ip,
        CASE WHEN usr.last_user_agent LIKE '%iphone%'
          THEN 'iPhone'
        WHEN usr.last_user_agent LIKE '%android%'
          THEN 'Android'
        WHEN usr.last_user_agent LIKE '%windows%'
          THEN 'Windows'
        ELSE '其他' END
      FROM sp_sync_user_tmp_user usr
        LEFT JOIN lu_province_city l ON (l.city = usr.city OR l.city = concat(usr.city, '市'))
      WHERE NOT exists(SELECT 1
                       FROM emr_user
                       WHERE user_id = usr.user_id AND org_id = usr.org_id AND ys_id = usr.ys_id);


    INSERT INTO emr_task (task_id, task_name, last_value, created_datetime) VALUES (7, '同步用户信息', max_value_date, now());
    COMMIT;

    DROP TABLE sp_sync_user_tmp_user;

  END;
