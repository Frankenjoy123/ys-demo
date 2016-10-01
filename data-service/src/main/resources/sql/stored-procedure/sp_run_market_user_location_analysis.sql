CREATE DEFINER=`root`@`%` PROCEDURE `sp_run_market_user_location_analysis`(chooseDate date)
BEGIN

SET SQL_SAFE_UPDATES = 0;

drop table if exists tmp_market_user_location;

create temporary table tmp_market_user_location
(
	draw_date date,
    org_id char(19),
    user_id char(19),
    province varchar(45),
    city varchar(45),
    marketing_id char(19),
    product_base_id char(19)
);


-- 取前一天
if (chooseDate is null) then
set chooseDate = date(DATE_SUB(convert_tz(now(),'+00:00','+08:00'), INTERVAL 1 day));
end if;

-- 插入正常的city
insert into tmp_market_user_location 
SELECT date(convert_tz(mr.created_datetime,"+00:00","+08:00")),m.org_id, u.id, ifnull(l.province, usr.province), ifnull(l.city,'未知城市'),
mr.marketing_id, mr.product_base_id
FROM mkt_draw_record mr left join user u on mr.user_id = u.id COLLATE utf8_general_ci
inner join marketing m on mr.marketing_id = m.id  COLLATE utf8_general_ci 
inner join user_scan_record usr on usr.id = mr.scan_record_id COLLATE utf8_general_ci 
left join lu_province_city l on (l.city = usr.city  or l.city = concat(usr.city,'市') or (length(usr.city) >0 and l.city like concat(usr.city,'%')) )
where date(convert_tz(mr.created_datetime,'+00:00','+08:00')) = chooseDate;

update tmp_market_user_location
set province = trim(trailing'省'FROM province) where province like '%省';

    UPDATE tmp_market_user_location
    SET province = '未知省份'
    WHERE province IS NULL;

start transaction;
-- 删除该天数据
delete from emr_market_user_location_analysis where date(draw_date) = chooseDate;

insert into emr_market_user_location_analysis 
select null, draw_date, province, city, count(1),org_id, marketing_id from tmp_market_user_location group by draw_date, org_id, marketing_id, province, city;  

insert into emr_task (task_id, task_name, last_value, created_datetime)values(6,'营销用户地点分析', chooseDate, now());
commit;

drop table tmp_market_user_location;



END