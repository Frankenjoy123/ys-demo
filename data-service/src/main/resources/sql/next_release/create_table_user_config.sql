#user_config
DROP TABLE IF EXISTS user_config;
CREATE TABLE user_config
(
  user_id           CHAR(19) PRIMARY KEY NOT NULL,
  auto_following    BIT                  NOT NULL DEFAULT b'1',
  modified_datetime DATETIME             NOT NULL
);

