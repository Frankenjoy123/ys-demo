CREATE DEFINER=`root`@`%` PROCEDURE `sp_sync_event`()
BEGIN

declare min_value varchar(50);
declare min_value_date datetime;
declare max_value_date datetime;

SET SQL_SAFE_UPDATES = 0;

drop table if exists sp_sync_event_tmp_event;

create temporary table sp_sync_event_tmp_event
(	
   `name` varchar(45) NOT NULL COMMENT '事件名称',
  `user_id` char(19) DEFAULT NULL COMMENT 'WHO',
  `ys_id` varchar(45) DEFAULT NULL,
  `wx_openid` varchar(50) DEFAULT NULL COMMENT '微信id',
  `event_datetime` datetime DEFAULT NULL,
  `ip` varchar(55) DEFAULT NULL,
  `user_agent` varchar(500) DEFAULT NULL,
  `province` varchar(20) DEFAULT NULL,
  `city` varchar(20) DEFAULT NULL,
  `org_id` char(19) DEFAULT NULL,
  `org_name` varchar(255) DEFAULT NULL,
  `product_base_id` char(19) DEFAULT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `key_batch_id` char(19) DEFAULT NULL,
  `product_key` varchar(22) DEFAULT NULL,
  `scan_record_id` char(19) DEFAULT NULL,
   `scan_datetime` datetime DEFAULT NULL,
  `draw_id` char(19) DEFAULT NULL COMMENT '抽奖id',
  `draw_datetime` datetime DEFAULT NULL,
  `is_priced` tinyint(1) DEFAULT NULL COMMENT '0表示没有抽中，1表示抽中',
  `price_status_code` varchar(20) DEFAULT NULL COMMENT 'paid和submit表示兑奖成功',
  `price_id` char(19) DEFAULT NULL,
  `marketing_id` char(19) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL
);



select last_value into min_value from emr_task where task_id = 8 order by created_datetime desc limit 1;
set min_value_date = ifnull(min_value, '2015-1-1 00:00:00');


-- 取当前时间的5分前。。
set max_value_date = date_sub(current_timestamp(),INTERVAL 1 MINUTE);

select min_value_date;

-- 插入扫描事件
insert into sp_sync_event_tmp_event 
SELECT 'scan', case when usr.user_id = '0020000000000000000' then '' else usr.user_id END, 
case when usr.user_id = '0020000000000000000' then usr.ysid else '' END,
case when u.oauth_type_code = 'webchat' then u.oauth_openid else null END, usr.created_datetime, usr.ip, usr.user_agent,usr.province, usr.city
, pb.org_id, '', pb.id, pb.name, usr.product_key_batch_id,usr.product_key, usr.id, usr.created_datetime, null, null, null, null,null,null,null
 from user_scan_record usr
inner join product_base pb on usr.product_base_id = pb.id  
left join user u on usr.user_id = u.id
where usr.created_datetime >= min_value_date AND usr.created_datetime < max_value_date;

-- where (u.modified_datetime is null and u.created_datetime >= @max_value_date) or u.modified_datetime >= @max_value_date;



-- 插入营销事件
insert into sp_sync_event_tmp_event 
SELECT 'draw', case when usr.user_id = '0020000000000000000' then '' else usr.user_id END, 
case when usr.user_id = '0020000000000000000' then usr.ysid else '' END,
case when u.oauth_type_code = 'webchat' then u.oauth_openid else null END, mdr.created_datetime,usr.ip, usr.user_agent,usr.province, usr.city
, pb.org_id, '', pb.id, pb.name, usr.product_key_batch_id,usr.product_key, usr.id, usr.created_datetime, mdr.id, mdr.created_datetime, mdr.isPrized, mdp.status_code,
mdp.draw_record_id, mdp.marketing_id, null
 from user_scan_record usr 
inner join product_base pb
on usr.product_base_id = pb.id  
inner join mkt_draw_record mdr on mdr.scan_record_id = usr.id
left join mkt_draw_prize mdp on mdr.scan_record_id = mdp.scan_record_id
left join user u on usr.user_id = u.id
where mdr.created_datetime >= min_value_date AND mdr.created_datetime < max_value_date;

-- 插入分享/导流事件
insert into sp_sync_event_tmp_event 
SELECT ue.type_code, case when usr.user_id = '0020000000000000000' then '' else usr.user_id END, 
case when usr.user_id = '0020000000000000000' then usr.ysid else '' END,
case when u.oauth_type_code = 'webchat' then u.oauth_openid else null END, ue.created_datetime, usr.ip, usr.user_agent,usr.province, usr.city
, pb.org_id, '', pb.id, pb.name, usr.product_key_batch_id,usr.product_key, usr.id, usr.created_datetime, null, null, null, null,null,null,ue.value
 from user_scan_record usr
inner join  user_event ue on usr.id = ue.scan_record_id
inner join product_base pb on usr.product_base_id = pb.id  
left join user u on usr.user_id = u.id
where ue.created_datetime >= min_value_date AND ue.created_datetime < max_value_date;

-- 插入评论事件
insert into sp_sync_event_tmp_event 
SELECT 'comment', case when usr.user_id = '0020000000000000000' then '' else usr.user_id END, 
case when usr.user_id = '0020000000000000000' then usr.ysid else '' END,
case when u.oauth_type_code = 'webchat' then u.oauth_openid else null END, pc.created_datetime, usr.ip, usr.user_agent,usr.province, usr.city
, pb.org_id, '', pb.id, pb.name, usr.product_key_batch_id,usr.product_key, usr.id, usr.created_datetime, null, null, null, null,null,null,pc.comments
 from user_scan_record usr
inner join  product_comments pc on usr.id = pc.scan_record_id
inner join product_base pb on usr.product_base_id = pb.id  
left join user u on usr.user_id = u.id
where pc.created_datetime >= min_value_date AND pc.created_datetime < max_value_date;

update sp_sync_event_tmp_event
set province = trim(trailing'省'FROM province) where province like '%省';


start transaction;
-- 插入user——id
insert into emr_event (name, user_id, ys_id, wx_openid, event_datetime, ip, user_agent, province, city, 
org_id, org_name, product_base_id, product_name, key_batch_id, product_key,
scan_record_id, scan_datetime, draw_id,draw_datetime, is_priced, price_status_code, price_id, marketing_id,value) 
select ev.name, ev.user_id, ev.ys_id, ev.wx_openid, ev.event_datetime, ev.ip, ev.user_agent,
ifnull(l.province,ev.province), l.city, 
ev.org_id, ev.org_name, ev.product_base_id, ev.product_name, ev.key_batch_id, ev.product_key,
ev.scan_record_id, ev.scan_datetime, ev.draw_id,ev.draw_datetime, ev.is_priced, ev.price_status_code, ev.price_id, ev.marketing_id, ev.value
from sp_sync_event_tmp_event ev
left join lu_province_city l on (l.city = ev.city  or l.city = concat(ev.city,'市') or (length(ev.city) >0 and l.city like concat(ev.city,'%'))  )
order by ev.event_datetime asc;

insert into emr_task (task_id, task_name, last_value, created_datetime) values(8,'同步事件信息', max_value_date, now());
commit;

drop table sp_sync_event_tmp_event;



END