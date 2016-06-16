DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `sp_run_market_user_device_analysis`(chooseDate date)
BEGIN

SET SQL_SAFE_UPDATES = 0;

drop table if exists tmp_market_user_device;

create temporary table tmp_market_user_device
(
	draw_date date,
    org_id char(19),
    user_id char(19),
    marketing_id char(19),
    product_base_id char(19),
    device varchar(50),
    sex int(11)
);


-- 取前一天
if (chooseDate is null) then
set chooseDate = date(DATE_SUB(convert_tz(now(),'+00:00','+08:00'), INTERVAL 1 day));
end if;

-- 插入设备型号
insert into tmp_market_user_device
SELECT date(convert_tz(mr.created_datetime,"+00:00","+08:00")),m.org_id, u.id,
mr.marketing_id, mr.product_base_id, 'iPhone',
case u.sex when 1 then 1
when 0 then 0
ELSE 2
END
FROM mkt_draw_record mr
left join user u on mr.user_id = u.id COLLATE utf8_general_ci -- user表join的目的在与明确与user表关联。
inner join marketing m on mr.marketing_id = m.id  COLLATE utf8_general_ci
inner join user_scan_record usr on usr.id = mr.scan_record_id COLLATE utf8_general_ci
where date(convert_tz(mr.created_datetime,'+00:00','+08:00')) = chooseDate
AND (usr.user_agent like '%iphone%' OR usr.user_agent like '%iPad%');

-- 插入设备型号
insert into tmp_market_user_device
SELECT date(convert_tz(mr.created_datetime,"+00:00","+08:00")),m.org_id, u.id,
mr.marketing_id, mr.product_base_id, 'Android',
case u.sex when 1 then 1
when 0 then 0
ELSE 2
END
FROM mkt_draw_record mr
left join user u on mr.user_id = u.id COLLATE utf8_general_ci -- user表join的目的在与明确与user表关联。
inner join marketing m on mr.marketing_id = m.id  COLLATE utf8_general_ci
inner join user_scan_record usr on usr.id = mr.scan_record_id COLLATE utf8_general_ci
where date(convert_tz(mr.created_datetime,'+00:00','+08:00')) = chooseDate
AND (usr.user_agent like '%android%');

-- 插入设备型号
insert into tmp_market_user_device
SELECT date(convert_tz(mr.created_datetime,"+00:00","+08:00")),m.org_id, u.id,
mr.marketing_id, mr.product_base_id, 'Windows',
case u.sex when 1 then 1
when 0 then 0
ELSE 2
END
FROM mkt_draw_record mr
left join user u on mr.user_id = u.id COLLATE utf8_general_ci -- user表join的目的在与明确与user表关联。
inner join marketing m on mr.marketing_id = m.id  COLLATE utf8_general_ci
inner join user_scan_record usr on usr.id = mr.scan_record_id COLLATE utf8_general_ci
where date(convert_tz(mr.created_datetime,'+00:00','+08:00')) = chooseDate
AND (usr.user_agent like '%windows%');

-- 插入设备型号
insert into tmp_market_user_device
SELECT date(convert_tz(mr.created_datetime,"+00:00","+08:00")),m.org_id, u.id,
mr.marketing_id, mr.product_base_id, 'Other',
case u.sex when 1 then 1 -- 女
when 0 then 0 -- 男
ELSE 2 -- 未知
END
FROM mkt_draw_record mr
left join user u on mr.user_id = u.id COLLATE utf8_general_ci -- user表join的目的在与明确与user表关联。
inner join marketing m on mr.marketing_id = m.id  COLLATE utf8_general_ci
inner join user_scan_record usr on usr.id = mr.scan_record_id COLLATE utf8_general_ci
where date(convert_tz(mr.created_datetime,'+00:00','+08:00')) = chooseDate
AND (usr.user_agent not like '%windows%' AND usr.user_agent not like '%iphone%' AND usr.user_agent not like '%ipad%'
AND usr.user_agent not like '%android%'
);



start transaction;
-- 删除该天数据
delete from emr_market_user_device_analysis where date(draw_date) = chooseDate;
delete from emr_market_user_gender_analysis where date(draw_date) = chooseDate;

insert into emr_market_user_device_analysis
select null, draw_date, device, count(1), org_id, marketing_id from tmp_market_user_device group by draw_date, org_id, marketing_id, device;

-- 插入性别统计
insert into emr_market_user_gender_analysis
select null, draw_date, sex, count(1), org_id, marketing_id from tmp_market_user_device group by draw_date, org_id, marketing_id, sex;

insert into emr_task (task_id, task_name, last_value, created_datetime)values(5,'营销用户设备及性别分析', chooseDate, now());
commit;

drop table tmp_market_user_device;



END$$
DELIMITER ;
