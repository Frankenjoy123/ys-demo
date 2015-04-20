#account
DROP TABLE IF EXISTS account;
CREATE TABLE account
(
  id                  VARCHAR(20) PRIMARY KEY            NOT NULL,
  org_id              VARCHAR(20)                        NOT NULL,
  identifier          VARCHAR(255)                       NOT NULL,
  status_code         VARCHAR(100)                       NOT NULL,
  first_name          VARCHAR(100)                       NOT NULL,
  last_name           VARCHAR(100)                       NOT NULL,
  email               VARCHAR(255)                       NOT NULL,
  phone               VARCHAR(100)                       NOT NULL,
  password            VARCHAR(100)                       NOT NULL,
  hash_salt           VARCHAR(100)                       NOT NULL,
  created_account_id  VARCHAR(20)                        NOT NULL,
  created_datetime    DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
  modified_account_id VARCHAR(20),
  modified_datetime   DATETIME
);
CREATE UNIQUE INDEX org_id_identifier ON account (org_id, identifier);
INSERT INTO account (id, org_id, identifier, status_code, first_name, last_name, email, phone, password, hash_salt, created_account_id)
VALUES ('2k0rahgcybh0l5uxtep', '2k0r1l55i2rs5544wz5', 'admin', 'available', '��Գ', '��', 'it@yunsu.co', '1234567890',
        'AF7E21CF2CD07F61D563EE1FFFCA650D', 'hacksalt', '2k0rahgcybh0l5uxtep');


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
  ('2k0dlcxvp541aodm0zq', '2k0rahgcybh0l5uxtep', '*', '*', '*', '2k0rahgcybh0l5uxtep');


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
  ('2k0domdqac60ediu9hf', '2k0rahgcybh0l5uxtep', '2k0r1l55i2rs5544wz5', 'productkey:*', '2k0rahgcybh0l5uxtep');


#account_token
DROP TABLE IF EXISTS account_token;
CREATE TABLE account_token
(
  id                               VARCHAR(20) PRIMARY KEY NOT NULL,
  account_id                       VARCHAR(20)             NOT NULL,
  app_id                           VARCHAR(20)             NOT NULL,
  device_id                        VARCHAR(255)            NULL,
  permanent_token                  VARCHAR(100)            NOT NULL,
  permanent_token_datetime         DATETIME                NOT NULL,
  permanent_token_expires_datetime DATETIME                NULL
);
CREATE UNIQUE INDEX permanent_token ON account_token (permanent_token);
