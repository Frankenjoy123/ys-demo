#message
CREATE TABLE message
(
  id                     VARCHAR(20) PRIMARY KEY                             NOT NULL,
  org_id                 VARCHAR(25)                                         NOT NULL,
  title                  VARCHAR(255)                                       NOT NULL,
  status_code            VARCHAR(25)                                         NOT NULL,
  type_code               VARCHAR(25)                                         NOT NULL,
  created_account_id     VARCHAR(20)                                         NOT NULL,
  created_datetime       DATETIME DEFAULT CURRENT_TIMESTAMP                  NOT NULL,
  modified_account_id    VARCHAR(20),
  modified_datetime      DATETIME
);