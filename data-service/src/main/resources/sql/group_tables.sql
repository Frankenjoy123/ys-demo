#group
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group`
(
  id                  VARCHAR(20) PRIMARY KEY            NOT NULL,
  org_id              VARCHAR(20)                        NOT NULL,
  name                VARCHAR(100)                       NOT NULL,
  description         VARCHAR(255)                       NULL,
  created_account_id  VARCHAR(20)                        NOT NULL,
  created_datetime    DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
  modified_account_id VARCHAR(20),
  modified_datetime   DATETIME
);

#group_permission
DROP TABLE IF EXISTS group_permission;
CREATE TABLE group_permission
(
  id                 VARCHAR(20) PRIMARY KEY                     NOT NULL,
  group_id           VARCHAR(20)                                 NOT NULL,
  org_id             VARCHAR(20)                                 NOT NULL,
  resource_code      VARCHAR(100)                                NOT NULL,
  action_code        VARCHAR(100)                                NOT NULL,
  created_account_id VARCHAR(20)                                 NOT NULL,
  created_datetime   DATETIME DEFAULT CURRENT_TIMESTAMP          NOT NULL
);

#group_permission_policy
DROP TABLE IF EXISTS group_permission_policy;
CREATE TABLE group_permission_policy
(
  id                 VARCHAR(20) PRIMARY KEY                     NOT NULL,
  group_id           VARCHAR(20)                                 NOT NULL,
  org_id             VARCHAR(20)                                 NOT NULL,
  policy_code        VARCHAR(100)                                NOT NULL,
  created_account_id VARCHAR(20)                                 NOT NULL,
  created_datetime   DATETIME DEFAULT CURRENT_TIMESTAMP          NOT NULL
);

#account_group
DROP TABLE IF EXISTS account_group;
CREATE TABLE account_group
(
  id                 VARCHAR(20) PRIMARY KEY            NOT NULL,
  account_id         VARCHAR(20)                        NOT NULL,
  group_id           VARCHAR(20)                        NOT NULL,
  created_account_id VARCHAR(20)                        NOT NULL,
  created_datetime   DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX account_id_group_id ON account_group (account_id, group_id);

