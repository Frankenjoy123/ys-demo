#permission_policy
DROP TABLE permission_policy;
CREATE TABLE permission_policy
(
  code          VARCHAR(100) PRIMARY KEY NOT NULL,
  name          VARCHAR(100)             NOT NULL,
  resource_code VARCHAR(100)             NOT NULL,
  action_code   VARCHAR(100)             NOT NULL,
  description   VARCHAR(255)
);
INSERT INTO permission_policy (code, name, resource_code, action_code, description)
VALUES
  ('*:*', '����Ա', '*', '*', 'admin'),
  ('productkey:*', '��Ʒ����ȫ����', 'productkey', 'read', ''),
  ('product:read', '��Ʒ��ȡ', 'product', 'read', '');

