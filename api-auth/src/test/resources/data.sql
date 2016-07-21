INSERT INTO organization
(id, name, type_code, status_code, description, created_account_id, created_datetime)
VALUES
  ('2k0r1l55i2rs5544wz5', '云溯科技', 'tech', 'available', '关注互联网+的力量!', '2k0rahgcybh0l5uxtep', '2015-05-01 00:00:00');

INSERT INTO account (id, org_id, identifier, status_code, first_name, last_name, email, phone, password, hash_salt, created_account_id, created_datetime, modified_account_id, modified_datetime)
VALUES
  ('0010000000000000000', '2k0r1l55i2rs5544wz5', 'system', 'available', '系统帐号', '云溯', 'it@yunsu.co', '', '', '',
   '0010000000000000000', '2015-05-01 00:00:00', NULL, NULL);

INSERT INTO permission_resource (code, name, description, actions)
VALUES
  ('account', '账号', '账号', 'create,delete,modify,read'),
  ('group', '账号组', '账号组', 'create,delete,modify,read'),
  ('organization', '组织', '组织', 'create,delete,modify,read'),
  ('product_base', '产品方案', '产品方案', 'create,delete,modify,read'),
  ('product_key', '产品码', '产品码', 'create,delete,modify,read');

INSERT INTO permission_action (code, name, description)
VALUES
  ('*', '完全控制', '完全控制'),
  ('create', '新建', '新建'),
  ('delete', '删除', '删除'),
  ('modify', '修改', '修改'),
  ('read', '查看', '查看');

INSERT INTO permission_policy (code, name, description, permissions)
VALUES
  ('default', 'Default', NULL, 'account:read,organization:read'),
  ('admin', 'Admin', NULL, '*:*'),
  ('read', 'Read', NULL, '*:read');

INSERT INTO permission_allocation (id, principal, restriction, permission, effect, created_account_id, created_datetime)
VALUES
  ('2ml1ogye3znqihtdnnd', 'account/0010000000000000000', 'org/*', '*:*', 'allow', '0010000000000000000',
   CURRENT_TIMESTAMP)