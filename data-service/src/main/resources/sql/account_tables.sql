#account_permission
DROP TABLE IF EXISTS account_permission;
CREATE TABLE account_permission
(
  id                 VARCHAR(20) PRIMARY KEY                     NOT NULL,
  account_id         VARCHAR(20)                                 NOT NULL,
  org_id             VARCHAR(20)                                 NOT NULL,
  resource_code      VARCHAR(100)                                NOT NULL,
  action_code        VARCHAR(100)                                NOT NULL,
  created_account_id VARCHAR(20)                                 NOT NULL,
  created_datetime   DATETIME DEFAULT CURRENT_TIMESTAMP          NOT NULL
);
CREATE INDEX account_id ON account_permission (account_id);
INSERT INTO account_permission (id, account_id, org_id, resource_code, action_code, created_account_id)
VALUES
  ('2k0dlcxvp541aodm0zq', '1', '*', '*', '*', '1');


#account_permission_policy
DROP TABLE IF EXISTS account_permission_policy;
CREATE TABLE account_permission_policy
(
  id                 VARCHAR(20) PRIMARY KEY                     NOT NULL,
  account_id         VARCHAR(20)                                 NOT NULL,
  org_id             VARCHAR(20)                                 NOT NULL,
  policy_code        VARCHAR(100)                                NOT NULL,
  created_account_id VARCHAR(20)                                 NOT NULL,
  created_datetime   DATETIME DEFAULT CURRENT_TIMESTAMP          NOT NULL
);
CREATE INDEX account_id ON account_permission_policy (account_id);
INSERT INTO account_permission_policy (id, account_id, org_id, policy_code, created_account_id)
VALUES
  ('2k0domdqac60ediu9hf', '1', '1', 'productkey:*', '1');


