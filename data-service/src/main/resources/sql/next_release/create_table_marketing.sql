#marketing
DROP TABLE IF EXISTS marketing;
CREATE TABLE `marketing` (
  `id` varchar(19) NOT NULL,
  `org_id` varchar(20) NOT NULL,
  `product_base_id` varchar(20) NOT NULL,
  `type_code` varchar(20) NOT NULL,
  `balance` double NOT NULL,
  `created_account_id` char(19) NOT NULL,
  `created_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_account_id` char(19) DEFAULT NULL,
  `modified_datetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

#mkt_draw_rule
DROP TABLE IF EXISTS mkt_draw_rule;
CREATE TABLE `mkt_draw_rule` (
  `id` char(19) NOT NULL,
  `marketing_id` char(19) NOT NULL,
  `prize_type_code` varchar(20) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `probability` decimal(10,8) DEFAULT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `created_account_id` char(19) NOT NULL,
  `created_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_account_id` char(19) DEFAULT NULL,
  `modified_datetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

#mkt_draw_record
DROP TABLE IF EXISTS mkt_draw_record;
CREATE TABLE `mkt_draw_record` (
  `id` char(19) NOT NULL,
  `scan_record_id` char(19) NOT NULL,
  `product_base_id` varchar(20) NOT NULL,
  `product_key` char(19) NOT NULL,
  `created_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` char(19) DEFAULT NULL,
  `isPrized` tinyint(1) DEFAULT ''0'',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

#mkt_draw_prize
DROP TABLE IF EXISTS mkt_draw_prize;
CREATE TABLE `mkt_draw_prize` (
  `draw_record_id` char(19) NOT NULL,
  `product_key` char(19) NOT NULL,
  `scan_record_id` char(19) NOT NULL,
  `amount` int(11) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `prize_type_code` varchar(20) DEFAULT NULL,
  `status_code` varchar(20) DEFAULT NULL,
  `created_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `paid_datetime` datetime DEFAULT NULL,
  `account_type` varchar(20) DEFAULT NULL,
  `prize_account` varchar(50) DEFAULT NULL,
  `prize_account_name` varchar(50) DEFAULT NULL,
  `comments` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`draw_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

