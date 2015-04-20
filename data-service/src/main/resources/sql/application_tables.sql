#application
DROP TABLE IF EXISTS application;
CREATE TABLE application
(
  id          VARCHAR(20) PRIMARY KEY NOT NULL,
  name        VARCHAR(100)            NOT NULL,
  description VARCHAR(255)            NOT NULL
);
CREATE UNIQUE INDEX id_UNIQUE ON application (id);
