#user
DROP TABLE IF EXISTS user;
CREATE TABLE user
(
  id               CHAR(19) PRIMARY KEY NOT NULL,
  device_id        VARCHAR(40),
  phone            VARCHAR(20),
  name             VARCHAR(45),
  status_code      VARCHAR(20)          NOT NULL,
  point            INT                  NOT NULL,
  address          VARCHAR(100),
  created_datetime DATETIME             NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX phone ON user (phone);
