delimiter |


CREATE EVENT IF NOT EXISTS EVENT_DAILY_RUN_ANALYSIS
ON SCHEDULE EVERY 1 day 
STARTS TIMESTAMP(CURRENT_DATE,'16:30:00')
ON COMPLETION PRESERVE ENABLE
DO 
BEGIN
 CALL sp_run_location_analysis(null);
 CALL sp_run_scan_analysis(null);
 CALL sp_run_market_user_area_analysis(null);
 CALL sp_run_market_user_device_analysis(null);
 CALL sp_run_market_user_location_analysis(null);
 CALL sp_run_market_user_usage_analysis(null);
END |

delimiter ;