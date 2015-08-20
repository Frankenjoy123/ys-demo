#user
DROP TABLE IF EXISTS user;
CREATE TABLE user
(
  id                   VARCHAR(20) PRIMARY KEY            NOT NULL,
  device_id            VARCHAR(40),
  phone                VARCHAR(20),
  name                 VARCHAR(45),
  status_code          VARCHAR(20)                        NOT NULL,
  point                INT                                NOT NULL,
  address              VARCHAR(100),
  last_access_datetime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
  created_datetime     DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL
);
CREATE INDEX phone ON user (phone);
