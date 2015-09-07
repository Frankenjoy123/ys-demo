#org_agency
DROP TABLE IF EXISTS org_agency;
CREATE TABLE org_agency
(
  id                    VARCHAR(20) PRIMARY KEY   NOT NULL,
  name                  VARCHAR(100)              NOT NULL,
  org_id                VARCHAR(20)               NOT NULL,
  location_id           VARCHAR(20)               NOT NULL,
  parent_id             VARCHAR(20)               DEFAULT NULL,
  address               VARCHAR(100)             NOT NULL,
  description           VARCHAR(500)             NOT NULL,
  status_code           VARCHAR(20)              NOT NULL,
  created_account_id    VARCHAR(20)               NOT NULL,
  created_datetime      DATETIME                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modified_account_id   VARCHAR(20)               DEFAULT NULL,
  modified_datetime     DATETIME                 DEFAULT NULL
);