DROP TABLE IF EXISTS lookup_code;

CREATE TABLE lookup_code(
  code VARCHAR(100),
  name VARCHAR(100),
  description VARCHAR(255),
  active bit,
  type_code VARCHAR(50)
);

INSERT into lookup_code (code, name, description, active, type_code)
  select code, name, description, active, 'account_status' from account_status_lkp;

INSERT into lookup_code (code, name, description, active, type_code)
  select code, name, description, active, 'message_status' from message_status_lkp;

INSERT into lookup_code (code, name, description, active, type_code)
  select code, name, description, active, 'message_type' from message_type_lkp;

INSERT into lookup_code (code, name, description, active, type_code)
  select code, name, description, active, 'org_status' from org_status_lkp;

INSERT into lookup_code (code, name, description, active, type_code)
  select code, name, description, active, 'permission_action' from permission_action_lkp;

INSERT into lookup_code (code, name, description, active, type_code)
  select code, name, description, active, 'permission_resource' from permission_resource_lkp;

INSERT into lookup_code (code, name, description, active, type_code)
  select code, name, description, active, 'product_file_type' from product_file_type_lkp;

INSERT into lookup_code (code, name, description, active, type_code)
  select code, name, description, active, 'product_key_batch_status' from product_key_batch_status_lkp;


INSERT into lookup_code (code, name, description, active, type_code)
  select code, name, description, active, 'product_key_type' from product_key_type_lkp;

INSERT into lookup_code (code, name, description, active, type_code)
  select code, name, description, active, 'product_status' from product_status_lkp;

INSERT into lookup_code (code, name, description, active, type_code)
  select code, name, description, active, 'user_status' from user_status_lkp;

INSERT into lookup_code (code, name, description, active, type_code) values
  ('created', '新创建', '新创建', 1, 'application_status'),
  ('active', '激活', '激活可以正常使用',  1, 'application_status'),
  ('updatable', '可修改', '可以被修改',  1, 'application_status'),
  ('force_update', '强制修改', '需要被强制修改', 1, 'application_status'),
  ('inactive', '失效', '已经失效', 1, 'application_status');

INSERT into lookup_code (code, name, description, active, type_code) values
  ('created', '新创建', '产品的初始状态', 1, 'product_status'),
  ('activated', '激活', '已出厂激活', 1, 'product_status'),
  ('deactivated', '失效', '已经失效', 1, 'product_status');

INSERT into lookup_code (code, name, description, active, type_code) values
  ('draft', '初始', '草稿初始状态', 1, 'product_status'),
  ('submitted', '已提交', '修改已经提交', 1, 'product_status'),
  ('rejected', '已放弃', '版本审核失败， 被放弃', 1, 'product_status'),
  ('activated', '已激活', '版本审批通过，激活可用', 1, 'product_status'),
  ('archived', '已归档', '老的版本归档', 1, 'product_status');

INSERT into lookup_code (code, name, description, active, type_code) values
  ('approved', '审核通过', '版本审核通过', 1, 'product_base_version_approval_status'),
  ('rejected', '审核失败', '版本审核失败', 1, 'product_base_version_approval_status');

INSERT into lookup_code (code, name, description, active, type_code) values
  ('created', '新建', '事务新建，待提交', 1, 'product_key_transaction_status'),
  ('committed', '提交', '事务提交成功', 1, 'product_key_transaction_status'),
  ('rollback', '回滚', '事务出错，回滚', 1, 'product_key_transaction_status');

INSERT into lookup_code (code, name, description, active, type_code) values
  ('activated', '正常', '正常激活', 1, 'device_status');

INSERT into lookup_code (code, name, description, active, type_code) values
  ('created', '新建', '事务新建，待提交', 1, 'user_point_transaction_status'),
  ('committed', '提交', '事务提交成功', 1, 'user_point_transaction_status'),
  ('rollback', '回滚', '事务出错，回滚' , 1,'user_point_transaction_status');

INSERT into lookup_code (code, name, description, active, type_code) values
  ('sign_in_rewards', '签到', '签到积分回馈', 1, 'user_point_transaction_type');

DROP TABLE IF EXISTS account_status_lkp;
DROP TABLE IF EXISTS message_status_lkp;
DROP TABLE IF EXISTS message_type_lkp;
DROP TABLE IF EXISTS org_status_lkp;
DROP TABLE IF EXISTS permission_action_lkp;
DROP TABLE IF EXISTS permission_resource_lkp;
DROP TABLE IF EXISTS product_file_type_lkp;
DROP TABLE IF EXISTS product_key_batch_status_lkp;
DROP TABLE IF EXISTS product_key_type_lkp;
DROP TABLE IF EXISTS product_status_lkp;
DROP TABLE IF EXISTS user_status_lkp;


