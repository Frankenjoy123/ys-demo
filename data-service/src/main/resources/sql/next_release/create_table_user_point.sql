#user_point
DROP TABLE IF EXISTS user_point;
CREATE TABLE user_point
(
  user_id                 VARCHAR(20) PRIMARY KEY NOT NULL,
  point                   INT                     NOT NULL,
  last_sign_in_datetime   DATETIME,
  continuous_sign_in_days INT DEFAULT 0
);

#user_point_transaction
DROP TABLE IF EXISTS user_point_transaction;
CREATE TABLE user_point_transaction
(
  id                 VARCHAR(20) PRIMARY KEY NOT NULL,
  user_id            VARCHAR(20)             NOT NULL,
  point              INT                     NOT NULL,
  is_increase        BIT                     NOT NULL,
  type_code          VARCHAR(20)             NOT NULL,
  status_code        VARCHAR(20)             NOT NULL,
  created_account_id VARCHAR(20),
  created_datetime   DATETIME                NOT NULL
);

