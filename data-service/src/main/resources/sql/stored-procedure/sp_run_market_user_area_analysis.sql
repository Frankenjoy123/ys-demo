DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `sp_run_market_user_area_analysis`(chooseDate date)
BEGIN

SET SQL_SAFE_UPDATES = 0;

drop table if exists tmp_market_user_area;

create temporary table tmp_market_user_area
(
	draw_date date,
    org_id char(19),
    user_id char(19),
    province varchar(45),
    city varchar(45),
    marketing_id char(19),
    product_base_id char(19),
    sex bit(1),
    age int(11),
    tag_id int,
    tag_name varchar(50)
);


-- 取前一天
if (chooseDate is null) then
set chooseDate = date(DATE_SUB(convert_tz(now(),'+00:00','+08:00'), INTERVAL 1 day));
end if;

-- 插入正常的city
insert into tmp_market_user_area
SELECT date(convert_tz(mr.created_datetime,"+00:00","+08:00")),m.org_id, u.id, usr.province, usr.city,
mr.marketing_id, mr.product_base_id, u.sex, u.age, tag.id, tag.name
FROM mkt_draw_record mr left join user u on mr.user_id = u.id COLLATE utf8_general_ci
inner join marketing m on mr.marketing_id = m.id  COLLATE utf8_general_ci
inner join user_scan_record usr on usr.id = mr.scan_record_id COLLATE utf8_general_ci
inner join lu_province_city l on l.city = usr.city
inner join lu_tag tag on l.tag_id = tag.id
where date(convert_tz(mr.created_datetime,'+00:00','+08:00')) = chooseDate;

-- 插入正常的city
insert into tmp_market_user_area
SELECT date(convert_tz(mr.created_datetime,"+00:00","+08:00")),m.org_id, u.id, usr.province, usr.city,
mr.marketing_id, mr.product_base_id, u.sex, u.age, tag.id, tag.name
FROM mkt_draw_record mr left join user u on mr.user_id = u.id COLLATE utf8_general_ci
inner join marketing m on mr.marketing_id = m.id  COLLATE utf8_general_ci
inner join user_scan_record usr on usr.id = mr.scan_record_id COLLATE utf8_general_ci
inner join lu_province_city l on l.city = concat(usr.city,'市')
inner join lu_tag tag on l.tag_id = tag.id
where date(convert_tz(mr.created_datetime,'+00:00','+08:00')) = chooseDate;

-- 插入非正常的city
insert into tmp_market_user_area
SELECT date(convert_tz(mr.created_datetime,"+00:00","+08:00")),m.org_id, u.id, usr.province, usr.city,
mr.marketing_id, mr.product_base_id, u.sex, u.age, tag.id, tag.name
FROM mkt_draw_record mr left join user u on mr.user_id = u.id COLLATE utf8_general_ci
inner join marketing m on mr.marketing_id = m.id  COLLATE utf8_general_ci
inner join user_scan_record usr on usr.id = mr.scan_record_id COLLATE utf8_general_ci
inner join lu_tag tag on tag.id = 5
where date(convert_tz(mr.created_datetime,'+00:00','+08:00')) = chooseDate
and (usr.city is null or length(usr.city) = 0 or usr.city = '未知' or (usr.city not in (select city from lu_province_city)
and concat(usr.city,'市')  not in (select city from lu_province_city)));


start transaction;
-- 删除该天数据
delete from emr_market_user_area_analysis where date(draw_date) = chooseDate;

insert into emr_market_user_area_analysis
select null, draw_date, tag_id, count(1), org_id, marketing_id, tag_name from tmp_market_user_area group by draw_date, org_id, marketing_id, tag_id;

insert into emr_task (task_id, task_name, last_value, created_datetime)values(3,'营销用户人群分析', chooseDate, now());
commit;

drop table tmp_market_user_area;



END$$
DELIMITER ;
