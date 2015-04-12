#account_permission
DROP TABLE account_permission;
CREATE TABLE account_permission
(
  id               BIGINT PRIMARY KEY                     NOT NULL AUTO_INCREMENT,
  account_id       BIGINT                                 NOT NULL,
  org_id           VARCHAR(20)                            NOT NULL,
  resource_code    VARCHAR(100)                           NOT NULL,
  action_code      VARCHAR(100)                           NOT NULL,
  created_datetime DATETIME DEFAULT CURRENT_TIMESTAMP     NOT NULL,
  created_by       BIGINT                                 NOT NULL
);
CREATE INDEX account_id ON account_permission (account_id);
INSERT INTO account_permission (account_id, org_id, resource_code, action_code, created_by)
VALUES
  (1, '*', '*', '*', 1);

