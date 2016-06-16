DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `sp_full_run_job`(jobName varchar(200), start_date date, end_date date)
BEGIN

set @sql_str = concat('call ', jobName, '(?);');

prepare stmt from @sql_str;
set @start_date = start_date;
while @start_date < end_date do

EXECUTE stmt using @start_date;

set @start_date = date_add(@start_date, interval 1 day);

end while;

deallocate prepare stmt;

END$$
DELIMITER ;
