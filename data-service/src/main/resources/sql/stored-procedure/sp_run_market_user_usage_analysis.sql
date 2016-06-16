DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `sp_run_market_user_usage_analysis`(chooseDate date)
BEGIN

SET SQL_SAFE_UPDATES = 0;

drop table if exists tmp_market_user_usage;

create temporary table tmp_market_user_usage
(
	draw_date date,
    org_id char(19),
    user_id char(19),
    marketing_id char(19),
    product_base_id char(19),
    draw_datetime datetime
);


-- 取前一天
if (chooseDate is null) then
set chooseDate = date(DATE_SUB(convert_tz(now(),'+00:00','+08:00'), INTERVAL 1 day));
end if;

-- 插入对小时的分析
insert into tmp_market_user_usage
SELECT date(convert_tz(mr.created_datetime,"+00:00","+08:00")),m.org_id, u.id
, mr.marketing_id, mr.product_base_id,convert_tz(mr.created_datetime,"+00:00","+08:00") as draw_datetime
FROM mkt_draw_record mr left join user u on mr.user_id = u.id COLLATE utf8_general_ci
inner join marketing m on mr.marketing_id = m.id  COLLATE utf8_general_ci
inner join user_scan_record usr on usr.id = mr.scan_record_id COLLATE utf8_general_ci
where date(convert_tz(mr.created_datetime,'+00:00','+08:00')) = chooseDate;

start transaction;
-- 删除该天数据
delete from emr_market_user_usage_analysis where draw_date = chooseDate;

-- 睡觉时间
insert into emr_market_user_usage_analysis
select null, draw_date, '00:00-06:00', count(1), org_id, marketing_id
from tmp_market_user_usage where hour(draw_datetime) < 6 group by draw_date, org_id, marketing_id;

-- 早上时间
insert into emr_market_user_usage_analysis
select null, draw_date, '06:00-08:00', count(1), org_id, marketing_id
from tmp_market_user_usage where hour(draw_datetime) >= 6 and hour(draw_datetime) < 8 group by draw_date, org_id, marketing_id;

-- 上午上班时间
insert into emr_market_user_usage_analysis
select null, draw_date, '08:00-12:00', count(1), org_id, marketing_id
from tmp_market_user_usage where hour(draw_datetime) >= 8 and hour(draw_datetime) < 12 group by draw_date, org_id, marketing_id;

-- 中午午休时间
insert into emr_market_user_usage_analysis
select null, draw_date, '12:00-14:00', count(1), org_id, marketing_id
from tmp_market_user_usage where hour(draw_datetime) >= 12 and hour(draw_datetime) < 14 group by draw_date, org_id, marketing_id;

-- 下午上班时间
insert into emr_market_user_usage_analysis
select null, draw_date, '14:00-16:00', count(1), org_id, marketing_id
from tmp_market_user_usage where hour(draw_datetime) >= 14 and hour(draw_datetime) < 16 group by draw_date, org_id, marketing_id;

-- 下午时间
insert into emr_market_user_usage_analysis
select null, draw_date, '16:00-18:00', count(1), org_id, marketing_id
from tmp_market_user_usage where hour(draw_datetime) >= 16 and hour(draw_datetime) < 18 group by draw_date, org_id, marketing_id;

-- 晚上活动时间
insert into emr_market_user_usage_analysis
select null, draw_date, '18:00-22:00', count(1), org_id, marketing_id
from tmp_market_user_usage where hour(draw_datetime) >= 18 and hour(draw_datetime) < 22 group by draw_date, org_id, marketing_id;

-- 晚上睡觉时间
insert into emr_market_user_usage_analysis
select null, draw_date, '22:00-24:00', count(1), org_id, marketing_id
from tmp_market_user_usage where hour(draw_datetime) >= 22 and hour(draw_datetime) <= 23 group by draw_date, org_id, marketing_id;



insert into emr_task (task_id, task_name, last_value, created_datetime)values(4,'营销用户人群使用时间分析', chooseDate, now());
commit;

drop table tmp_market_user_usage;



END$$
DELIMITER ;
