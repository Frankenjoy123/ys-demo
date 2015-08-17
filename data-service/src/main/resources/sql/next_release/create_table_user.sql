#user
DROP TABLE IF EXISTS user;
CREATE TABLE user
(
  id               VARCHAR(25) PRIMARY KEY NOT NULL,
  device_code      VARCHAR(80),
  cellular         VARBINARY(45),
  thumbnail        VARCHAR(100),
  address          VARCHAR(100),
  ys_creadit       INT,
  level            INT,
  status           VARCHAR(20),
  created_datetime DATETIME,
  name             VARCHAR(45)
);
CREATE UNIQUE INDEX id ON user (id);
