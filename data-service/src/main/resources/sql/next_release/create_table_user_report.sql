DROP TABLE IF EXISTS user_report;
CREATE TABLE user_report
(
  id                  CHAR(19) PRIMARY KEY NOT NULL,
  user_id             CHAR(19) NOT NULL,
  product_base_id     CHAR(19) NOT NULL,
  store_name          VARCHAR(50),
  store_address       VARCHAR(100),
  report_details      VARCHAR(500),
  created_datetime    DATETIME NOT NULL
);