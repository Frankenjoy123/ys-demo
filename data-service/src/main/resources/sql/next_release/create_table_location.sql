#location
DROP TABLE IF EXISTS location;
CREATE TABLE location
(
  id          CHAR(19) PRIMARY KEY NOT NULL,
  name        VARCHAR(100)         NOT NULL,
  type_code   VARCHAR(20)          NOT NULL,
  longitude   DOUBLE               NOT NULL,
  latitude    DOUBLE               NOT NULL,
  description VARCHAR(255),
  parent_id   CHAR(19)
);