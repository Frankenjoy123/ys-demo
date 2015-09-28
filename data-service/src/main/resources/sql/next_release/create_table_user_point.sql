#user_point
DROP TABLE IF EXISTS user_activity;
CREATE TABLE user_activity
(
  user_id CHAR(19) PRIMARY KEY NOT NULL,
  last_sign_in_datetime        DATETIME,
  last_sign_in_continuous_days INT DEFAULT 0
);

#user_point_transaction
DROP TABLE IF EXISTS user_point_transaction;
CREATE TABLE user_point_transaction
(
  id                 CHAR(19) PRIMARY KEY NOT NULL,
  user_id            CHAR(19)             NOT NULL,
  point              INT                  NOT NULL,
  type_code          VARCHAR(20)          NOT NULL,
  status_code        VARCHAR(20)          NOT NULL,
  created_account_id CHAR(19),
  created_datetime   DATETIME             NOT NULL
);

