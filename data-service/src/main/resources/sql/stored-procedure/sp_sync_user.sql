DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `sp_sync_user`()
BEGIN

declare min_value varchar(50);
declare min_value_date datetime;
declare max_value_date datetime;

SET SQL_SAFE_UPDATES = 0;

drop table if exists sp_sync_user_tmp_user;

create temporary table sp_sync_user_tmp_user
(	
  `user_id` char(19) DEFAULT NULL COMMENT '用户id',
  `ys_id` varchar(32) DEFAULT NULL COMMENT 'ysid，当匿名访问时，ysid作为唯一区分用户的id',
  `org_id` char(19) DEFAULT NULL COMMENT '品牌商id',
  `org_name` varchar(255) DEFAULT NULL COMMENT '品牌商名字',
  `name` varchar(45) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL COMMENT '手机',
  `email` varchar(45) DEFAULT NULL,
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `sex` bit(1) DEFAULT NULL COMMENT '性别',
  `gravatar_url` varchar(255) DEFAULT NULL COMMENT '头像',
  `wx_openid` varchar(50) DEFAULT NULL COMMENT '微信id',
  `province` varchar(45) DEFAULT NULL COMMENT '省',
  `city` varchar(45) DEFAULT NULL COMMENT '城市',
  `address` varchar(100) DEFAULT NULL COMMENT '地址',
  `join_datetime` datetime DEFAULT NULL COMMENT '加入时间',
  `last_event_ip` varchar(55) DEFAULT NULL COMMENT 'ip',
  `last_user_agent` varchar(255) DEFAULT NULL COMMENT '设备'
);



select last_value into min_value from emr_task where task_id = 7 order by created_datetime desc limit 1;
set min_value_date = ifnull(min_value, '2015-1-1 00:00:00');


-- 取当前时间的5分前。。
set max_value_date = date_sub(current_timestamp(),INTERVAL 5 MINUTE);

select min_value_date;

-- 先读取user表中新增用户的信息，因为有可能不需要scan也能有user的情况。
insert into sp_sync_user_tmp_user 
SELECT u.id, '', org.id as 'org_id', org.name as 'org_name', u.name, u.phone, u.email, u.age, u.sex, u.gravatar_url, case when u.oauth_type_code = 'webchat' then u.oauth_openid else null END, 
ifnull(u.province, usr2.province), ifnull(u.city, usr2.city), ifnull(u.address, usr2.address), usr2.created_datetime,
usr2.ip,usr2.user_agent
from  user u 
inner join (
SELECT usr.user_id, pb.org_id, min(usr.created_datetime) as 'created_datetime',usr.id, province, city, address, usr.ip, usr.user_agent 
FROM yunsoo2015DB.user_scan_record usr inner join product_base pb
on usr.product_base_id = pb.id group by usr.user_id, pb.org_id order by usr.id desc) as usr2 on u.id = usr2.user_id 
inner join organization org on usr2.org_id = org.id
where u.created_datetime >= min_value_date AND u.created_datetime < max_value_date;
-- where (u.modified_datetime is null and u.created_datetime >= @max_value_date) or u.modified_datetime >= @max_value_date;

-- 插入ysId不为空，而user——id为空或者固定的。用来读取匿名用户
insert into sp_sync_user_tmp_user 
SELECT '', usr.ysid, org.id as 'org_id', org.name as 'org_name', null, null, null, null, null, null, null, 
usr.province, usr.city, usr.address, usr.created_datetime,usr.ip, usr.user_agent
from user_scan_record usr inner join product_base pb
on usr.product_base_id = pb.id 
inner join organization org on pb.org_id = org.id
where usr.created_datetime >= min_value_date and usr.created_datetime < max_value_date 
and (usr.user_id is null or usr.user_id = '0020000000000000000')
group by usr.ysid, org.id order by usr.id desc; 

-- select * from sp_sync_user_tmp_user;

-- select * from sp_sync_user_tmp_user;

start transaction;

-- 先删除部分数据

delete u from emr_user u 
inner join sp_sync_user_tmp_user tmp 
on u.org_id = tmp.org_id and (u.user_id = tmp.user_id and u.ys_id = tmp.ys_id);


-- 插入user——id
insert into emr_user (user_id, ys_id, org_id, org_name, name, phone, email,age, sex, gravatar_url, wx_openid, province, city, address,join_datetime, latest_event_ip,latest_event_device) 
select usr.user_id, usr.ys_id, usr.org_id, usr.org_name, usr.name, usr.phone, usr.email,usr.age, 
usr.sex, usr.gravatar_url, usr.wx_openid, ifnull(l.province,usr.province), ifnull(l.city,usr.city),
usr.address,usr.join_datetime, usr.last_event_ip,  
case when usr.last_user_agent like '%iphone%' then 'iPhone' 
 when usr.last_user_agent like '%android%' then 'Android'
 when usr.last_user_agent like '%windows%' then 'Windows'
 else '其他' end
from sp_sync_user_tmp_user usr 
left join lu_province_city l on (l.city = usr.city  or l.city = concat(usr.city,'市') )
where not exists (select 1 from emr_user where user_id = usr.user_id and org_id = usr.org_id and ys_id = usr.ys_id);


insert into emr_task (task_id, task_name, last_value, created_datetime) values(7,'同步用户信息', max_value_date, now());
commit;

drop table sp_sync_user_tmp_user;



END$$
DELIMITER ;
