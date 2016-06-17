CREATE DEFINER =`root`@`%` PROCEDURE `sp_full_run_job`(jobName VARCHAR(200), start_date DATE, end_date DATE)
  BEGIN

    SET @sql_str = concat('call ', jobName, '(?);');

    PREPARE stmt FROM @sql_str;
    SET @start_date = start_date;
    WHILE @start_date < end_date DO

      EXECUTE stmt
      USING @start_date;

      SET @start_date = date_add(@start_date, INTERVAL 1 DAY);

    END WHILE;

    DEALLOCATE PREPARE stmt;

  END;
