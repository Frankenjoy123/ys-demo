CREATE DEFINER=`root`@`%` PROCEDURE `sp_run_scan_analysis`(IN `chooseDate` date)
	LANGUAGE SQL
	NOT DETERMINISTIC
	CONTAINS SQL
	SQL SECURITY DEFINER
	COMMENT ''
BEGIN

SET SQL_SAFE_UPDATES = 0;

drop table if exists tmp_user_scan_record;

create temporary table tmp_user_scan_record
(
	id varchar(32), 
	user_id  char(19), 
	product_key varchar(22) binary, 
	product_base_id char(19), 
	app_id char(19), 
	ysid varchar(32), 
	device_id varchar(40), 
	ip varchar(15), 
	lat double, 
	lng double,
	province varchar(20),
	city varchar(20),
	address varchar(100),
	details varchar(100),
	user_agent varchar(255),
	scan_date date,
	product_name varchar(255),
	category_id char(19), 
    category_name varchar(50), 
	app_category varchar(50),
	org_id char(19),
	org_name varchar(255)
);


-- 取前一天
if (chooseDate is null) then
set chooseDate = date(DATE_SUB(convert_tz(now(),'+00:00','+08:00'), INTERVAL 1 day));
end if;

insert into tmp_user_scan_record 
SELECT 
ifnull(r.ysid, r.user_id), r.user_id, r.product_key,r.product_base_id,r.app_id, r.ysid, r.device_id, r.ip, r.latitude, r.longitude, r.province, r.city,r.address, r.details, r.user_agent,
date(convert_tz(r.created_datetime,"+00:00","+08:00")) as scan_date, pb.name as product_name, pc.id as category_id, pc.name as category_name, ap.name as app_category,org.id as org_id, org.name as org_name 
FROM user_scan_record r 
inner join product_base pb on r.product_base_id = pb.id COLLATE utf8_general_ci
inner join organization org on org.id = pb.org_id COLLATE utf8_general_ci
left join  product_category pc on pc.id = pb.category_id COLLATE utf8_general_ci 
left join application ap on r.app_id = ap.id COLLATE utf8_general_ci
where date(convert_tz(r.created_datetime,'+00:00','+08:00')) = chooseDate;

start transaction;
-- 删除该天数据
delete from emr_scan_record_analysis where date(scan_date) = chooseDate;

insert into emr_scan_record_analysis select null, scan_date, org_id, product_base_id, product_name, '所有批次' as batch_id, count(1) as pv, count(distinct id,product_key) as uv 
from tmp_user_scan_record 
group by scan_date, org_id, product_base_id, product_name;

insert into emr_task (task_id, task_name, last_value, created_datetime)values(1,'统计扫描率', chooseDate, now());
commit;

drop table tmp_user_scan_record;


END