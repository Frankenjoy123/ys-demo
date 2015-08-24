#user_scan_record
DROP TABLE IF EXISTS user_scan_record;
CREATE TABLE user_scan_record
(
  id               CHAR(19) PRIMARY KEY NOT NULL,
  user_id          CHAR(19)             NOT NULL,
  product_key      CHAR(22)             NOT NULL,
  product_base_id  CHAR(19)             NOT NULL,
  app_id           CHAR(19)             NOT NULL,
  device_id        VARCHAR(40)          NOT NULL,
  details          VARCHAR(255),
  latitude         DOUBLE,
  longitude        DOUBLE,
  location         VARCHAR(255),
  created_datetime DATETIME             NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX product_key ON user_scan_record (product_key);
CREATE INDEX user_id ON user_scan_record (user_id);
