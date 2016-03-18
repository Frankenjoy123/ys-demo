INSERT INTO organization
(id, name, type_code, status_code, description, details, created_account_id, created_datetime)
VALUES
  ('2k0r1l55i2rs5544wz5', '云溯科技', 'tech', 'available', '关注互联网+的力量!', '详细描述信息', '2k0rahgcybh0l5uxtep',
   '2015-05-01 00:00:00');

INSERT INTO account (id, org_id, identifier, status_code, first_name, last_name, email, phone, password, hash_salt, created_account_id, created_datetime, modified_account_id, modified_datetime)
VALUES
  ('0010000000000000000', '2k0r1l55i2rs5544wz5', 'system', 'available', '系统帐号', '云溯', 'it@yunsu.co', '', '', '',
   '0010000000000000000', '2015-05-01 00:00:00', NULL, NULL);

INSERT INTO user (id, device_id, phone, name, status_code, point, address, created_datetime)
VALUES
  ('0020000000000000000', '00000000000000000000000000000000', NULL, '匿名', 'enabled', 0, NULL, '2015-05-01 00:00:00');

--lookup
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES
  ('account_status', 'activated', '正常', '正常', TRUE),
  ('account_status', 'deactivated', '停用', '已停用', TRUE),

  ('application_status', 'active', '激活', '激活可以正常使用', TRUE),
  ('application_status', 'created', '新创建', '新创建', TRUE),
  ('application_status', 'force_update', '强制更新', '需要被强制更新', TRUE),
  ('application_status', 'inactive', '失效', '已经失效', TRUE),
  ('application_status', 'updatable', '可修改', '可以被修改', TRUE),

  ('device_status', 'activated', '正常', '正常激活', TRUE),

  ('org_status', 'blocked', '被阻止', '被加入黑名单', TRUE),
  ('org_status', 'created', '已创建', '已经创建', TRUE),
  ('org_status', 'deleted', '已删除', '被删除了', TRUE),
  ('org_status', 'verified', '已认证', '通过平台认证', TRUE),

  ('product_base_status', 'activated', '激活', '已出厂激活', TRUE),
  ('product_base_status', 'created', '新创建', '产品的初始状态', TRUE),
  ('product_base_status', 'deactivated', '失效', '已经失效', TRUE),

  ('product_base_versions_status', 'activated', '已激活', '版本审批通过，激活可用', TRUE),
  ('product_base_versions_status', 'archived', '已归档', '老的版本归档', TRUE),
  ('product_base_versions_status', 'draft', '初始', '草稿初始状态', TRUE),
  ('product_base_versions_status', 'rejected', '已放弃', '版本审核失败， 被放弃', TRUE),
  ('product_base_versions_status', 'submitted', '已提交', '修改已经提交', TRUE),

  ('product_file_type', 'logistics', '物流', '物流', TRUE),
  ('product_file_type', 'package', '包装', '包装', TRUE),

  ('product_key_batch_status', 'available', '有效', 'available', TRUE),
  ('product_key_batch_status', 'creating', '审核中', 'creating', TRUE),
  ('product_key_batch_status', 'deleted', '已删除', 'deleted', TRUE),
  ('product_key_batch_status', 'downloaded', '已下载', 'downloaded', TRUE),
  ('product_key_batch_status', 'new', '新建', 'new', TRUE),

  ('product_key_transaction_status', 'committed', '提交', '事务提交成功', TRUE),
  ('product_key_transaction_status', 'created', '新建', '事务新建，待提交', TRUE),
  ('product_key_transaction_status', 'rollback', '回滚', '事务出错，回滚', TRUE),

  ('product_key_type', 'package', '包装', '包装类型的key', TRUE),
  ('product_key_type', 'qr_public', '二维码', '二维码', TRUE),
  ('product_key_type', 'qr_secure', '隐藏二维码', '隐藏二维码', TRUE),
  ('product_key_type', 'rfid', 'RFID标签', 'RFID标签', TRUE),

  ('product_status', 'activated', '已激活', '已出厂激活', TRUE),
  ('product_status', 'created', '未激活', '产品初始状态', TRUE),
  ('product_status', 'deleted', '已删除', '已删除', TRUE),
  ('product_status', 'recalled', '已召回', '已召回', TRUE),

  ('user_point_transaction_status', 'committed', '提交', '事务提交成功', TRUE),
  ('user_point_transaction_status', 'created', '新建', '事务新建，待提交', TRUE),
  ('user_point_transaction_status', 'rollback', '回滚', '事务出错，回滚', TRUE),

  ('user_point_transaction_type', 'sign_in_rewards', '签到', '签到积分回馈', TRUE);

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

