#permission_policy
DROP TABLE IF EXISTS permission_policy;
CREATE TABLE permission_policy
(
  id            VARCHAR(20) PRIMARY KEY          NOT NULL,
  code          VARCHAR(255)                     NOT NULL,
  name          VARCHAR(100)                     NOT NULL,
  resource_code VARCHAR(100)                     NOT NULL,
  action_code   VARCHAR(100)                     NOT NULL,
  description   VARCHAR(255)
);
CREATE INDEX code ON permission_policy (code);
INSERT INTO permission_policy (id, code, name, resource_code, action_code, description)
VALUES
  ('2k0dl02lvrpj4ejiiyr', '*:*', '管理员', '*', '*', 'admin'),
  ('2k0dl5l5in6173bub9g', 'productkey:*', '产品码完全控制', 'productkey', '*', ''),
  ('2k0dl73nm3kg1h6wubp', 'product:read', '产品读取', 'product', 'read', '');

