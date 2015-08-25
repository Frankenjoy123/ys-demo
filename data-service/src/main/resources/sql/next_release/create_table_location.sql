#location
DROP TABLE IF EXISTS location;
CREATE TABLE location
(
  id                 VARCHAR(20) PRIMARY KEY NOT NULL,
  latitude            DOUBLE             NOT NULL,
  longitude           DOUBLE             NOT NULL,
  name                VARCHAR(100)        NOT NULL,
  type_code          VARCHAR(100)        NOT NULL,
  parent_id          VARCHAR(20),          DEFAULT NULL,
  description      VARCHAR(255),       DEFAULT NULL
);