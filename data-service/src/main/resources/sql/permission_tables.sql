#permission_policy
DROP TABLE permission_policy;
CREATE TABLE permission_policy
(
  id            INT PRIMARY KEY          NOT NULL AUTO_INCREMENT,
  code          VARCHAR(255)             NOT NULL,
  name          VARCHAR(100)             NOT NULL,
  resource_code VARCHAR(100)             NOT NULL,
  action_code   VARCHAR(100)             NOT NULL,
  description   VARCHAR(255)
);
CREATE INDEX code ON permission_policy (code);
INSERT INTO permission_policy (code, name, resource_code, action_code, description)
VALUES
  ('*:*', '管理员', '*', '*', 'admin'),
  ('productkey:*', '产品码完全控制', 'productkey', '*', ''),
  ('product:read', '产品读取', 'product', 'read', '');

