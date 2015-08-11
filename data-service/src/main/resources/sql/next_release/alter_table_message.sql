#message
ALTER TABLE `yunsoo2015DB`.`message`
DROP COLUMN `post_show_time`,
DROP COLUMN `last_updated_datetime`,
DROP COLUMN `link`,
DROP COLUMN `body`,
DROP COLUMN `digest`,
CHANGE COLUMN `orgId` `org_id` VARCHAR(25) NOT NULL AFTER `id`,
CHANGE COLUMN `status` `status_code` VARCHAR(25) NOT NULL AFTER `title`,
CHANGE COLUMN `type` `type_code` VARCHAR(25) NOT NULL AFTER `status_code`,
CHANGE COLUMN `last_updated_by` `modified_account_id` VARCHAR(20) NULL AFTER `created_datetime`,
CHANGE COLUMN `Id` `id` VARCHAR(20) NOT NULL ,
CHANGE COLUMN `title` `title` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `created_by` `created_account_id` VARCHAR(20) NOT NULL ,
CHANGE COLUMN `created_datetime` `created_datetime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ,
CHANGE COLUMN `expired_datetime` `modified_datetime` DATETIME NULL ;
