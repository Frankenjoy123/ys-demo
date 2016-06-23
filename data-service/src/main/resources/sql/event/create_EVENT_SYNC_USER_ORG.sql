delimiter |


CREATE EVENT IF NOT EXISTS EVENT_SYNC_USER_EVENT
ON SCHEDULE EVERY 2 minute
ON COMPLETION PRESERVE ENABLE
DO 
BEGIN
 CALL sp_sync_user();
 CALL sp_sync_event();
END |

delimiter ;