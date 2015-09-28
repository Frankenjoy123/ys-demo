#message
DROP TABLE IF EXISTS message;
CREATE TABLE message
(
  id                  CHAR(19) PRIMARY KEY NOT NULL,
  org_id              CHAR(19)             NOT NULL,
  title               VARCHAR(255)         NOT NULL,
  status_code         VARCHAR(20)          NOT NULL,
  type_code           VARCHAR(20)          NOT NULL,
  created_account_id  CHAR(19)             NOT NULL,
  created_datetime    DATETIME             NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modified_account_id CHAR(19),
  modified_datetime   DATETIME
);