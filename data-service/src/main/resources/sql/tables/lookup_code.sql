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

  ('permission_action', 'create', '创建', 'create', TRUE),
  ('permission_action', 'delete', '删除', 'delete', TRUE),
  ('permission_action', 'manage', '管理', 'manage', TRUE),
  ('permission_action', 'modify', '修改', 'modify', TRUE),
  ('permission_action', 'read', '查看', 'read', TRUE),

  ('permission_resource', 'account', '帐号', '帐号', TRUE),
  ('permission_resource', 'accountpermission', '帐号权限', '与帐号直接关联的权限', TRUE),
  ('permission_resource', 'accountpermissionpolicy', '帐号权限策略', '与帐号直接关联的权限策略', TRUE),
  ('permission_resource', 'device', '设备', '设备', TRUE),
  ('permission_resource', 'group', '帐号组', '帐号组', TRUE),
  ('permission_resource', 'grouppermission', '帐号组权限', '与帐号组直接关联的权限', TRUE),
  ('permission_resource', 'grouppermissionpolicy', '帐号组权限策略', '与帐号组直接关联的权限策略', TRUE),
  ('permission_resource', 'message', '消息', '企业推送消息', TRUE),
  ('permission_resource', 'organization', '组织', '组织', TRUE),
  ('permission_resource', 'permissionaction', '权限操作', '对权限资源的操作', TRUE),
  ('permission_resource', 'permissionpolicy', '权限策略', '定义了一系列权限', TRUE),
  ('permission_resource', 'permissionresource', '权限资源', '权限资源', TRUE),
  ('permission_resource', 'product', '产品', '单件产品', TRUE),
  ('permission_resource', 'productbase', '产品', '产品模板', TRUE),
  ('permission_resource', 'productkey', '产品码', '产品码', TRUE),
  ('permission_resource', 'productkeycredit', '产品码额度', '产品码额度', TRUE),
  ('permission_resource', 'productkeyorder', '产品码订单', '产品码订单', TRUE),
  ('permission_resource', 'page-enterprise-account', '帐号', '帐号页面', TRUE),
  ('permission_resource', 'page-enterprise-dashboard', '仪表台', '仪表台页面', TRUE),
  ('permission_resource', 'page-enterprise-device', '设备', '设备页面', TRUE),
  ('permission_resource', 'page-enterprise-group', '帐号组', '帐号组页面', TRUE),
  ('permission_resource', 'page-enterprise-logistics', '物流', '物流页面', TRUE),
  ('permission_resource', 'page-enterprise-message', '消息', '消息页面', TRUE),
  ('permission_resource', 'page-enterprise-package', '包装', '包装页面', TRUE),
  ('permission_resource', 'page-enterprise-productbase', '产品', '产品页面', TRUE),
  ('permission_resource', 'page-enterprise-productkey', '产品码', '产品码页面', TRUE),
  ('permission_resource', 'page-enterprise-profile', '个人设置', '个人帐号设置页面', TRUE),
  ('permission_resource', 'page-enterprise-report', '报表', '报表相关页面', TRUE),
  ('permission_resource', 'page-enterprise-marketing', '市场营销', '市场营销相关页面', TRUE),

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
