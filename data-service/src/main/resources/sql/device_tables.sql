#device
DROP TABLE IF EXISTS device;
CREATE TABLE device
(
  id                  VARCHAR(40) PRIMARY KEY NOT NULL,
  device_name         VARCHAR(255),
  device_os           VARCHAR(255),
  org_id              VARCHAR(20)             NOT NULL,
  status_code         VARCHAR(20)             NOT NULL,
  check_point_id      VARCHAR(20),
  created_account_id  VARCHAR(20)             NOT NULL,
  created_datetime    DATETIME                NOT NULL,
  modified_account_id VARCHAR(20),
  modified_datetime   DATETIME
);
