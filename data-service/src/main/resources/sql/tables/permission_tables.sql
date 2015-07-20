#permission_resource_lkp
DROP TABLE IF EXISTS permission_resource_lkp;
CREATE TABLE permission_resource_lkp
(
  code        VARCHAR(100) PRIMARY KEY NOT NULL,
  name        VARCHAR(100)             NOT NULL,
  description VARCHAR(255),
  active      BIT DEFAULT b'1'         NOT NULL
);
INSERT INTO permission_resource_lkp (code, name, description, active)
VALUES
  ('permissionresource', '权限资源', '权限资源', TRUE),
  ('permissionaction', '权限操作', '对权限资源的操作', TRUE),
  ('permissionpolicy', '权限策略', '定义了一系列权限', TRUE),
  ('accountpermission', '帐号权限', '与帐号直接关联的权限', TRUE),
  ('accountpermissionpolicy', '帐号权限策略', '与帐号直接关联的权限策略', TRUE),
  ('grouppermission', '帐号组权限', '与帐号组直接关联的权限', TRUE),
  ('grouppermissionpolicy', '帐号组权限策略', '与帐号组直接关联的权限策略', TRUE),
  ('account', '帐号', '帐号', TRUE),
  ('group', '帐号组', '帐号组', TRUE),
  ('device', '设备', '设备', TRUE),
  ('organization', '组织', '组织', TRUE),
  ('productbase', '产品', '产品模板', TRUE),
  ('product', '产品', '单件产品', TRUE),
  ('productkey', '产品码', '产品码', TRUE),
  ('productkeycredit', '产品码额度', '产品码额度', TRUE),
  ('productkeyorder', '产品码订单', '产品码订单', TRUE),
  ('message', '消息', '企业推送消息', TRUE),
  ('page-enterprise-dashboard', '仪表台', '仪表台页面', TRUE),
  ('page-enterprise-productkey', '产品码', '产品码页面', TRUE),
  ('page-enterprise-package', '包装', '包装页面', TRUE),
  ('page-enterprise-logistics', '物流', '物流页面', TRUE),
  ('page-enterprise-productbase', '产品', '产品页面', TRUE),
  ('page-enterprise-message', '消息', '消息页面', TRUE),
  ('page-enterprise-device', '设备', '设备页面', TRUE),
  ('page-enterprise-account', '帐号', '帐号页面', TRUE),
  ('page-enterprise-group', '帐号组', '帐号组页面', TRUE),
  ('page-enterprise-profile', '个人设置', '个人帐号设置页面', TRUE),
  ('page-enterprise-report-*', '报表', '报表页面', TRUE);


#permission_action_lkp
DROP TABLE IF EXISTS permission_action_lkp;
CREATE TABLE permission_action_lkp
(
  code        VARCHAR(20) PRIMARY KEY  NOT NULL,
  name        VARCHAR(100)             NOT NULL,
  description VARCHAR(255),
  active      BIT DEFAULT b'1'         NOT NULL
);
INSERT INTO permission_action_lkp (code, name, description, active)
VALUES
  ('read', '查看', 'read', TRUE),
  ('create', '创建', 'create', TRUE),
  ('modify', '修改', 'modify', TRUE),
  ('delete', '删除', 'delete', TRUE),
  ('manage', '管理', 'manage', TRUE);

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
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khkcj9p0dyt4n3sw4i', '*:*', '管理员', '*', '*', '完全控制权限');
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khkckcmia93qcfpk0z', '*:read', '查看所以资源', '*', 'read', '所有资源只读权限');
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlaejt12dv5gungxm', 'page-enterprise-dashboard:read', '仪表台查看', 'page-enterprise-dashboard', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlaejt12dv5gungxn', 'page-enterprise-dashboard:read', '仪表台查看', 'productkeycredit', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlaejt12dv5gungxo', 'page-enterprise-dashboard:read', '仪表台查看', 'productkeybatch', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlaejt12dv5gungxp', 'page-enterprise-dashboard:read', '仪表台查看', 'message', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlj8l9c4hgmhd0mks', 'page-enterprise-productkey:read', '产品码查看', 'page-enterprise-productkey', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlj8l9c4hgmhd0mkt', 'page-enterprise-productkey:read', '产品码查看', 'productkey', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlj8l9c4hgmhd0mku', 'page-enterprise-productkey:read', '产品码查看', 'productbase', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlj8l9c4hgmhd0mkv', 'page-enterprise-productkey:read', '产品码查看', 'productkeycredit', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description) VALUES
  ('2khll11ikdj0k0oalhs', 'page-enterprise-productkey:manage', '产品码管理', 'page-enterprise-productkey', 'manage', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khll11ikdj0k0oalht', 'page-enterprise-productkey:manage', '产品码管理', 'productkey', '*', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khll11ikdj0k0oalhu', 'page-enterprise-productkey:manage', '产品码管理', 'productbase', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khll11ikdj0k0oalhv', 'page-enterprise-productkey:manage', '产品码管理', 'productkeycredit', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlna1ep8y2pmcjxas', 'page-enterprise-package:read', '包装查看', 'page-enterprise-package', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlned4ou394fo6kwl', 'page-enterprise-package:manage', '包装管理', 'page-enterprise-package', 'manage', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlngqrzepwg6lkuae', 'page-enterprise-logistics:manage', '物流管理', 'page-enterprise-logistics', 'manage', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlnid6895ccqlfu53', 'page-enterprise-logistics:read', '物流查看', 'page-enterprise-logistics', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlnqh5ghajvikqteg', 'page-enterprise-productbase:read', '产品查看', 'page-enterprise-productbase', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlnqh5ghajvikqteh', 'page-enterprise-productbase:read', '产品查看', 'productkeycredit', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description) VALUES
  ('2khlnsusr1x779i52sa', 'page-enterprise-productbase:manage', '产品管理', 'page-enterprise-productbase', 'manage', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlnsusr1x779i52sb', 'page-enterprise-productbase:manage', '产品管理', 'productkeycredit', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khloahd5kl011713ks', 'page-enterprise-message:read', '消息查看', 'page-enterprise-message', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khloahd5kl011713kt', 'page-enterprise-message:read', '消息查看', 'message', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlof4rlbt9mcx15jy', 'page-enterprise-message:manage', '消息管理', 'page-enterprise-message', 'manage', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlof4rlbt9mcx15jz', 'page-enterprise-message:manage', '消息管理', 'message', '*', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlom1x69n5h9fn04g', 'page-enterprise-device:manage', '设备管理', 'page-enterprise-device', 'manage', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlom1x69n5h9fn04h', 'page-enterprise-device:manage', '设备管理', 'device', '*', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlonw3pw4ni5p2xk2', 'page-enterprise-device:read', '设备查看', 'page-enterprise-device', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlonw3pw4ni5p2xk3', 'page-enterprise-device:read', '设备查看', 'device', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlp4fqmii5q822ag6', 'page-enterprise-account:read', '帐号查看', 'page-enterprise-account', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlp4fqmii5q822ag7', 'page-enterprise-account:read', '帐号查看', 'account', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlp4fqmii5q822ag8', 'page-enterprise-account:read', '帐号查看', 'group', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlp8jobbla0p440h5', 'page-enterprise-account:manage', '帐号管理', 'page-enterprise-account', 'manage', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlp8jobbla0p440h6', 'page-enterprise-account:manage', '帐号管理', 'account', '*', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlp8jobbla0p440h7', 'page-enterprise-account:manage', '帐号管理', 'group', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlpb17ra8yem6aqng', 'page-enterprise-group:manage', '帐号组管理', 'page-enterprise-group', 'manage', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlpb17ra8yem6aqnh', 'page-enterprise-group:manage', '帐号组管理', 'group', '*', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlpb17ra8yem6aqni', 'page-enterprise-group:manage', '帐号组管理', 'account', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlpd36losijupblnz', 'page-enterprise-group:read', '帐号组查看', 'page-enterprise-group', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlpd36losijupblo0', 'page-enterprise-group:read', '帐号组查看', 'group', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlpd36losijupblo1', 'page-enterprise-group:read', '帐号组查看', 'account', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlpitijcb2qvr8bjm', 'page-enterprise-profile:read', '个人设置查看', 'page-enterprise-profile', 'read', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlpkjsxkrjplvvs6r', 'page-enterprise-profile:manage', '个人设置管理', 'page-enterprise-profile', 'manage', NULL);
INSERT INTO yunsoo2015DB.permission_policy (id, code, name, resource_code, action_code, description)
VALUES ('2khlpr1dwyhbbtvbrlg', 'page-enterprise-report-*:read', '全部报表查看', 'page-enterprise-report-*', 'read', NULL);



