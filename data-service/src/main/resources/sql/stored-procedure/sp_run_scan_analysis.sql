CREATE DEFINER=`root`@`%` PROCEDURE `sp_run_scan_analysis`(chooseDate date)
BEGIN

SET SQL_SAFE_UPDATES = 0;

-- 取前一天
if (chooseDate is null) then
set chooseDate = date(DATE_SUB(convert_tz(now(),''+00:00'',''+08:00''), INTERVAL 1 day));
end if;

start transaction;
-- 删除该天数据
delete from emr_scan_record_analysis where date(scan_date) = chooseDate;

insert into emr_scan_record_analysis select null, chooseDate, ev.org_id, ev.product_base_id, ev.product_name, 
''所有批次'' as batch_id, count(1) as pv, count(distinct user_id, ys_id) as uv, count(first_scan.first_id)
from emr_event ev left join (select min(id) as first_id, product_base_id from emr_event where name =''scan'' group by product_base_id, product_key )
as first_scan on  ev.id = first_scan.first_id and ev.product_base_id = first_scan.product_base_id
 where name =''scan'' and date(convert_tz(scan_datetime,''+00:00'',''+08:00'')) = chooseDate
group by ev.org_id, ev.product_base_id;


insert into emr_scan_record_analysis select null, chooseDate, ev.org_id, ''All'', ''所有产品'', ''所有批次'' as batch_id, count(1) as pv, count(distinct user_id, ys_id) as uv,
count(first_scan.first_id)
from emr_event ev
left join (select min(id) as first_id from emr_event where name =''scan'' group by product_key )
as first_scan on  ev.id = first_scan.first_id
where name =''scan'' and date(convert_tz(scan_datetime,''+00:00'',''+08:00'')) = chooseDate
group by org_id;

insert into emr_task (task_id, task_name, last_value, created_datetime)values(1,''统计扫描率'', chooseDate, now());
commit;

END