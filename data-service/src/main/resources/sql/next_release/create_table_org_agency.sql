#org_agency
DROP TABLE IF EXISTS org_agency;
CREATE TABLE org_agency
(
  id                  CHAR(19) PRIMARY KEY   NOT NULL,
  name                VARCHAR(100)           NOT NULL,
  org_id              CHAR(19)               NOT NULL,
  location_id         CHAR(19)               NOT NULL,
  parent_id           CHAR(19),
  address             VARCHAR(100),
  description         VARCHAR(500),
  status_code         VARCHAR(20)            NOT NULL,
  created_account_id  CHAR(19)               NOT NULL,
  created_datetime    DATETIME               NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modified_account_id CHAR(19),
  modified_datetime   DATETIME
);