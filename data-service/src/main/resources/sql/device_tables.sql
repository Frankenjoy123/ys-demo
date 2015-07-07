#device
DROP TABLE IF EXISTS device;
CREATE TABLE device
(
  id                  VARCHAR(40) PRIMARY KEY NOT NULL,
  org_id              VARCHAR(20)             NOT NULL,
  login_account_id VARCHAR(20),
  name             VARCHAR(50),
  os               VARCHAR(50),
  status_code         VARCHAR(20)             NOT NULL,
  check_point_id      VARCHAR(20),
  comments         VARCHAR(100),
  created_account_id  VARCHAR(20)             NOT NULL,
  created_datetime    DATETIME                NOT NULL,
  modified_account_id VARCHAR(20),
  modified_datetime   DATETIME
);

