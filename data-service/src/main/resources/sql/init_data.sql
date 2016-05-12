#organization
INSERT INTO organization (id, name, type_code, status_code, description, details, created_account_id, created_datetime)
VALUES ('2k0r1l55i2rs5544wz5', '云溯科技', 'tech', 'available', '关注互联网+的力量!', '详细描述信息', '0010000000000000000', now());

#account
INSERT INTO account (id, org_id, identifier, status_code, first_name, last_name, email, phone, password, hash_salt, created_account_id, created_datetime, modified_account_id, modified_datetime)
VALUES ('0010000000000000000', '2k0r1l55i2rs5544wz5', 'system', 'available', '系统', '', 'system@yunsu.co', '', '', '',
        '0010000000000000000', now(), NULL, NULL);
INSERT INTO account (id, org_id, identifier, status_code, first_name, last_name, email, phone, password, hash_salt, created_account_id, created_datetime, modified_account_id, modified_datetime)
VALUES ('2k0rahgcybh0l5uxtep', '2k0r1l55i2rs5544wz5', 'admin', 'available', '序猿', '程', 'it@yunsu.co', '',
        'f825dfb56ec9f6b04bd20248a16882b9ffaff4a7', 'ThyX6u6W', '0010000000000000000', now(), NULL, NULL);
INSERT INTO account (id, org_id, identifier, status_code, first_name, last_name, email, phone, password, hash_salt, created_account_id, created_datetime, modified_account_id, modified_datetime)
VALUES ('2kadmvn8uh248k5k7wa', '2k0r1l55i2rs5544wz5', 'lijian', 'available', '黎建', '求', 'lijian@yunsu.co', '',
        'f825dfb56ec9f6b04bd20248a16882b9ffaff4a7', 'ThyX6u6W', '0010000000000000000', now(), NULL, NULL);

#group
INSERT INTO `group` (id, org_id, name, description, created_account_id, created_datetime, modified_account_id, modified_datetime)
VALUES
  ('2keke58h6ucc523vhwc', '2k0r1l55i2rs5544wz5', 'Administrators', '管理员组', '0010000000000000000', now(), NULL, NULL);

#permissoin_allocatoin
INSERT INTO permission_allocation (id, principal, restriction, permission, effect, created_account_id, created_datetime)
VALUES ('2lvt1gk4upnbfsglnrk', 'account/*', 'org/current', 'policy/default', 'allow', '0010000000000000000', now());
INSERT INTO permission_allocation (id, principal, restriction, permission, effect, created_account_id, created_datetime)
VALUES
  ('2lvt6ivt9puz30trzxf', 'group/2keke58h6ucc523vhwc', 'org/*', 'policy/admin', 'allow', '0010000000000000000', now());
INSERT INTO permission_allocation (id, principal, restriction, permission, effect, created_account_id, created_datetime)
VALUES ('2lx15bsrtfiysag4oyk', 'account/2kadmvn8uh248k5k7wa', 'org/*', '*:*', 'allow', '0010000000000000000', now());
INSERT INTO permission_allocation (id, principal, restriction, permission, effect, created_account_id, created_datetime)
VALUES ('2m70vq78rw9f0oxpgtv', 'account/2k0rahgcybh0l5uxtep', 'org/*', '*:*', 'allow', '0010000000000000000', now());

#domain_directory
INSERT INTO domain_directory (name, description, org_id) VALUES ('*', '云溯科技', '2k0r1l55i2rs5544wz5');

#permission_action
INSERT INTO permission_action (code, name, description) VALUES ('*', '完全控制', '完全控制');
INSERT INTO permission_action (code, name, description) VALUES ('create', '新建', '新建');
INSERT INTO permission_action (code, name, description) VALUES ('delete', '删除', '删除');
INSERT INTO permission_action (code, name, description) VALUES ('read', '查看', '查看');
INSERT INTO permission_action (code, name, description) VALUES ('write', '修改', '修改');

#permission_policy
INSERT INTO permission_policy (code, name, description, permissions) VALUES ('admin', 'Admin', '管理员权限', '*:*');
INSERT INTO permission_policy (code, name, description, permissions)
VALUES ('default', 'Default', '所有帐号默认拥有的权限', 'account:read,organization:read');
INSERT INTO permission_policy (code, name, description, permissions) VALUES
  ('enterprise_page_account_manage', 'Account Manage', '账号管理',
   'enterprise_page_account:*,enterprise_page_permission:*,account:*,group:read,account_group:*,permission_allocation:*');
INSERT INTO permission_policy (code, name, description, permissions) VALUES
  ('enterprise_page_account_view', 'Account View', '账号查看',
   'enterprise_page_account:read,enterprise_page_permission:read,account:read,group:read,account_group:read,permission_allocation:read');
INSERT INTO permission_policy (code, name, description, permissions)
VALUES ('enterprise_page_agency_manage', 'Agency Manage', '经销商管理', 'enterprise_page_agency:*,org_agency:*');
INSERT INTO permission_policy (code, name, description, permissions)
VALUES ('enterprise_page_agency_view', 'Agency View', '经销商查看', 'enterprise_page_agency:read,org_agency:read');
INSERT INTO permission_policy (code, name, description, permissions) VALUES
  ('enterprise_page_app_message_manage', 'App Message Manage', 'App消息推送', 'enterprise_page_app_message:*,message:*');
INSERT INTO permission_policy (code, name, description, permissions) VALUES
  ('enterprise_page_app_message_view', 'App Message View', 'App消息推送', 'enterprise_page_app_message:read,message:read');
INSERT INTO permission_policy (code, name, description, permissions) VALUES
  ('enterprise_page_dashboard_view', 'Dashboard View', '控制面板',
   'enterprise_page_dashboard:read,product_base:read,product_key_batch:read,message:read');
INSERT INTO permission_policy (code, name, description, permissions)
VALUES ('enterprise_page_device_manage', 'Device Manage', '设备管理', 'enterprise_page_device:*,device:*');
INSERT INTO permission_policy (code, name, description, permissions)
VALUES ('enterprise_page_device_view', 'Device View', '设备查看', 'enterprise_page_device:read,device:read');
INSERT INTO permission_policy (code, name, description, permissions) VALUES
  ('enterprise_page_group_manage', 'Group Manage', '账号组管理',
   'enterprise_page_group:*,enterprise_page_permission:*,group:*,account_group:*,permission_allocation:*');
INSERT INTO permission_policy (code, name, description, permissions) VALUES
  ('enterprise_page_group_view', 'Group View', '账号组查看',
   'enterprise_page_group:read,enterprise_page_permission:read,group:read,account_group:read,permission_allocation:read');
INSERT INTO permission_policy (code, name, description, permissions)
VALUES ('enterprise_page_logistics_manage', 'Logistics Manage', '物流管理', 'enterprise_page_logistics:*,logistics:*');
INSERT INTO permission_policy (code, name, description, permissions)
VALUES ('enterprise_page_logistics_view', 'Logistics View', '物流查看', 'enterprise_page_logistics:read,logistics:read');
INSERT INTO permission_policy (code, name, description, permissions) VALUES
  ('enterprise_page_marketing_prize_manage', 'Marketing Prize Manage', '兑奖信息管理',
   'enterprise_page_marketing_prize:*,marketing:*');
INSERT INTO permission_policy (code, name, description, permissions) VALUES
  ('enterprise_page_marketing_prize_view', 'Marketing Prize View', '兑奖信息查看',
   'enterprise_page_marketing_prize:read,marketing:read');
INSERT INTO permission_policy (code, name, description, permissions) VALUES
  ('enterprise_page_marketing_rule_manage', 'Marketing Rule Manage', '营销方案管理',
   'enterprise_page_marketing_rule:*,marketing:*');
INSERT INTO permission_policy (code, name, description, permissions) VALUES
  ('enterprise_page_marketing_rule_view', 'Marketing Rule View', '营销方案查看',
   'enterprise_page_marketing_rule:read,marketing:read');
INSERT INTO permission_policy (code, name, description, permissions) VALUES
  ('enterprise_page_package_key_manage', 'Package Key Manage', '包装码管理',
   'enterprise_page_package_key:*,product_key_batch:*');
INSERT INTO permission_policy (code, name, description, permissions) VALUES
  ('enterprise_page_package_key_view', 'Package Key View', '包装码查看',
   'enterprise_page_package_key:read,product_key_batch:read');
INSERT INTO permission_policy (code, name, description, permissions)
VALUES ('enterprise_page_package_manage', 'Package Manage', '包装管理', 'enterprise_page_package:*,package:*');
INSERT INTO permission_policy (code, name, description, permissions)
VALUES ('enterprise_page_package_view', 'Package View', '包装查看', 'enterprise_page_package:read,package:read');
INSERT INTO permission_policy (code, name, description, permissions) VALUES
  ('enterprise_page_product_base_manage', 'Product Base Manage', '产品方案管理',
   'enterprise_page_product_base:*,product_base:*');
INSERT INTO permission_policy (code, name, description, permissions) VALUES
  ('enterprise_page_product_base_view', 'Product Base View', '产品方案查看',
   'enterprise_page_product_base:read,product_base:read');
INSERT INTO permission_policy (code, name, description, permissions) VALUES
  ('enterprise_page_product_feedback_view', 'Product Feedback View', '产品评论查看',
   'enterprise_page_product_feedback:read,product_base:read');
INSERT INTO permission_policy (code, name, description, permissions) VALUES
  ('enterprise_page_product_key_manage', 'Product Key Manage', '产品码管理',
   'enterprise_page_product_key:*,product_key_batch:*,product_base:read,marketing:*');
INSERT INTO permission_policy (code, name, description, permissions) VALUES
  ('enterprise_page_product_key_view', 'Product Key View', '产品码查看',
   'enterprise_page_product_key:read,product_key_batch:read,product_base:read,marketing:read');
INSERT INTO permission_policy (code, name, description, permissions) VALUES
  ('enterprise_page_product_user_report_view', 'Product Report View', '产品举报查看',
   'enterprise_page_product_user_report:read,product_base:read');
INSERT INTO permission_policy (code, name, description, permissions)
VALUES ('enterprise_page_reports_view', 'Reports View', '数据分析查看', 'enterprise_page_reports:read,reports:read');
INSERT INTO permission_policy (code, name, description, permissions) VALUES ('read', 'Read', '只读权限', '*:read');

#location
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0bp', '安徽', 'province', 31.52, 117.17, '安徽省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0bq', '北京', 'province', 39.55, 116.24, '北京市', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0br', '重庆', 'province', 29.59, 106.54, '重庆市', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0bs', '福建', 'province', 26.05, 119.18, '福建省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0bt', '甘肃', 'province', 36.04, 103.51, '甘肃省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0bu', '广东', 'province', 23.08, 113.14, '广东省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0bv', '广西', 'province', 22.48, 108.19, '广西省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0bw', '贵州', 'province', 26.35, 106.42, '贵州省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0bx', '海南', 'province', 20.02, 110.2, '海南省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0by', '河北', 'province', 38.02, 114.3, '河北省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0bz', '河南', 'province', 34.46, 113.4, '河南省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0c0', '黑龙江', 'province', 45.44, 126.36, '黑龙江省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0c1', '湖北', 'province', 30.35, 114.17, '湖北省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0c2', '湖南', 'province', 28.12, 112.59, '湖南省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0c3', '吉林', 'province', 43.54, 125.19, '吉林省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0c4', '江苏', 'province', 32.03, 118.46, '江苏省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0c5', '江西', 'province', 28.4, 115.55, '江西省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0c6', '辽宁', 'province', 41.48, 123.25, '辽宁省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0c7', '内蒙古', 'province', 40.48, 111.41, '内蒙古自治区', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0c8', '宁夏', 'province', 38.27, 106.16, '宁夏回族自治区', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0c9', '青海', 'province', 36.38, 101.48, '青海省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0ca', '山东', 'province', 36.4, 117, '山东省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0cb', '山西', 'province', 37.54, 112.33, '山西省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0cc', '陕西', 'province', 34.17, 108.57, '陕西省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0cd', '上海', 'province', 31.14, 121.29, '上海市', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0ce', '四川', 'province', 30.4, 104.04, '四川省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0cf', '天津', 'province', 39.02, 117.12, '天津市', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0cg', '西藏', 'province', 29.39, 91.08, '西藏自治区', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0ch', '新疆', 'province', 43.45, 87.36, '新疆维吾尔族自治区', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0ci', '云南', 'province', 25.04, 102.42, '云南省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0cj', '浙江', 'province', 30.16, 120.1, '浙江省', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0ck', '香港', 'province', 21.23, 115.12, '香港', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0cl', '澳门', 'province', 21.33, 115.07, '澳门', NULL);
INSERT INTO location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0cm', '台湾', 'province', 25.03, 121.3, '台湾', NULL);

#lookup_code
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('account_status', 'activated', '正常', '正常', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('account_status', 'deactivated', '停用', '已停用', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('application_status', 'active', '激活', '激活可以正常使用', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('application_status', 'created', '新创建', '新创建', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('application_status', 'force_update', '强制修改', '需要被强制修改', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('application_status', 'inactive', '失效', '已经失效', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('application_status', 'updatable', '可修改', '可以被修改', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('device_status', 'activated', '正常', '正常激活', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('org_status', 'blocked', '被阻止', '被加入黑名单', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('org_status', 'created', '已创建', '已经创建', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('org_status', 'deleted', '已删除', '被删除了', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('org_status', 'verified', '已认证', '通过平台认证', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_action', 'create', '创建', 'create', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_action', 'delete', '删除', 'delete', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_action', 'manage', '管理', 'manage', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_action', 'modify', '修改', 'modify', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_action', 'read', '查看', 'read', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'account', '帐号', '帐号', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'accountpermission', '帐号权限', '与帐号直接关联的权限', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'accountpermissionpolicy', '帐号权限策略', '与帐号直接关联的权限策略', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'device', '设备', '设备', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'group', '帐号组', '帐号组', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'grouppermission', '帐号组权限', '与帐号组直接关联的权限', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'grouppermissionpolicy', '帐号组权限策略', '与帐号组直接关联的权限策略', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'message', '消息', '企业推送消息', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'organization', '组织', '组织', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'page-enterprise-account', '帐号', '帐号页面', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'page-enterprise-dashboard', '仪表台', '仪表台页面', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'page-enterprise-device', '设备', '设备页面', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'page-enterprise-group', '帐号组', '帐号组页面', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'page-enterprise-logistics', '物流', '物流页面', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'page-enterprise-message', '消息', '消息页面', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'page-enterprise-package', '包装', '包装页面', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'page-enterprise-productbase', '产品', '产品页面', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'page-enterprise-productkey', '产品码', '产品码页面', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'page-enterprise-profile', '个人设置', '个人帐号设置页面', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'page-enterprise-report-*', '报表', '报表页面', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'permissionaction', '权限操作', '对权限资源的操作', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'permissionpolicy', '权限策略', '定义了一系列权限', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'permissionresource', '权限资源', '权限资源', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'product', '产品', '单件产品', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'productbase', '产品', '产品模板', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'productkey', '产品码', '产品码', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'productkeycredit', '产品码额度', '产品码额度', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('permission_resource', 'productkeyorder', '产品码订单', '产品码订单', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_base_status', 'activated', '激活', '已出厂激活', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_base_status', 'created', '新创建', '产品的初始状态', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_base_status', 'deactivated', '失效', '已经失效', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_base_versions_status', 'activated', '已激活', '版本审批通过，激活可用', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_base_versions_status', 'archived', '已归档', '老的版本归档', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_base_versions_status', 'draft', '初始', '草稿初始状态', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_base_versions_status', 'rejected', '已放弃', '版本审核失败， 被放弃', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_base_versions_status', 'submitted', '已提交', '修改已经提交', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_file_type', 'logistics', '物流', '物流', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_file_type', 'package', '包装', '包装', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_key_batch_status', 'available', '有效', 'available', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_key_batch_status', 'creating', '审核中', 'creating', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_key_batch_status', 'deleted', '已删除', 'deleted', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_key_batch_status', 'downloaded', '已下载', 'downloaded', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_key_batch_status', 'new', '新建', 'new', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_key_transaction_status', 'committed', '提交', '事务提交成功', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_key_transaction_status', 'created', '新建', '事务新建，待提交', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_key_transaction_status', 'rollback', '回滚', '事务出错，回滚', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_key_type', 'package', '包装', '包装类型的key', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_key_type', 'qr_public', '二维码', '二维码', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_key_type', 'qr_secure', '隐藏二维码', '隐藏二维码', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_key_type', 'rfid', 'RFID标签', 'RFID标签', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_status', 'activated', '已激活', '已出厂激活', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_status', 'created', '未激活', '产品初始状态', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_status', 'deleted', '已删除', '已删除', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('product_status', 'recalled', '已召回', '已召回', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('user_point_transaction_status', 'committed', '提交', '事务提交成功', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('user_point_transaction_status', 'created', '新建', '事务新建，待提交', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('user_point_transaction_status', 'rollback', '回滚', '事务出错，回滚', TRUE);
INSERT INTO lookup_code (type_code, code, name, description, active)
VALUES ('user_point_transaction_type', 'sign_in_rewards', '签到', '签到积分回馈', TRUE);

#lu_province_city
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (1, '北京', '北京市', 1);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (2, '天津', '天津市', 3);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (3, '河北', '石家庄市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (4, '河北', '唐山市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (5, '河北', '秦皇岛市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (6, '河北', '邯郸市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (7, '河北', '邢台市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (8, '河北', '保定市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (9, '河北', '张家口市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (10, '河北', '承德市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (11, '河北', '沧州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (12, '河北', '廊坊市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (13, '河北', '衡水市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (14, '山西', '太原市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (15, '山西', '大同市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (16, '山西', '阳泉市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (17, '山西', '长治市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (18, '山西', '晋城市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (19, '山西', '朔州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (20, '山西', '晋中市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (21, '山西', '运城市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (22, '山西', '忻州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (23, '山西', '临汾市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (24, '山西', '吕梁市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (25, '内蒙古', '呼和浩特市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (26, '内蒙古', '包头市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (27, '内蒙古', '乌海市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (28, '内蒙古', '赤峰市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (29, '内蒙古', '通辽市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (30, '内蒙古', '鄂尔多斯市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (31, '内蒙古', '呼伦贝尔市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (32, '内蒙古', '巴彦淖尔市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (33, '内蒙古', '乌兰察布市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (34, '内蒙古', '兴安盟', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (35, '内蒙古', '锡林郭勒盟', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (36, '内蒙古', '阿拉善盟', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (37, '辽宁', '沈阳市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (38, '辽宁', '大连市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (39, '辽宁', '鞍山市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (40, '辽宁', '抚顺市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (41, '辽宁', '本溪市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (42, '辽宁', '丹东市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (43, '辽宁', '锦州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (44, '辽宁', '营口市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (45, '辽宁', '阜新市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (46, '辽宁', '辽阳市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (47, '辽宁', '盘锦市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (48, '辽宁', '铁岭市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (49, '辽宁', '朝阳市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (50, '辽宁', '葫芦岛市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (51, '吉林', '长春市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (52, '吉林', '吉林市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (53, '吉林', '四平市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (54, '吉林', '辽源市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (55, '吉林', '通化市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (56, '吉林', '白山市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (57, '吉林', '松原市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (58, '吉林', '白城市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (59, '吉林', '延边朝鲜族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (60, '黑龙江', '哈尔滨市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (61, '黑龙江', '齐齐哈尔市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (62, '黑龙江', '鸡西市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (63, '黑龙江', '鹤岗市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (64, '黑龙江', '双鸭山市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (65, '黑龙江', '大庆市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (66, '黑龙江', '伊春市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (67, '黑龙江', '佳木斯市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (68, '黑龙江', '七台河市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (69, '黑龙江', '牡丹江市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (70, '黑龙江', '黑河市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (71, '黑龙江', '绥化市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (72, '黑龙江', '大兴安岭地区', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (73, '上海', '上海市', 3);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (74, '江苏', '南京市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (75, '江苏', '无锡市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (76, '江苏', '徐州市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (77, '江苏', '常州市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (78, '江苏', '苏州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (79, '江苏', '南通市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (80, '江苏', '连云港市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (81, '江苏', '淮安市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (82, '江苏', '盐城市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (83, '江苏', '扬州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (84, '江苏', '镇江市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (85, '江苏', '泰州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (86, '江苏', '宿迁市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (87, '浙江', '杭州市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (88, '浙江', '宁波市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (89, '浙江', '温州市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (90, '浙江', '嘉兴市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (91, '浙江', '湖州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (92, '浙江', '绍兴市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (93, '浙江', '金华市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (94, '浙江', '衢州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (95, '浙江', '舟山市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (96, '浙江', '台州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (97, '浙江', '丽水市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (98, '安徽', '合肥市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (99, '安徽', '芜湖市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (100, '安徽', '蚌埠市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (101, '安徽', '淮南市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (102, '安徽', '马鞍山市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (103, '安徽', '淮北市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (104, '安徽', '铜陵市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (105, '安徽', '安庆市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (106, '安徽', '黄山市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (107, '安徽', '滁州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (108, '安徽', '阜阳市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (109, '安徽', '宿州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (110, '安徽', '巢湖市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (111, '安徽', '六安市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (112, '安徽', '亳州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (113, '安徽', '池州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (114, '安徽', '宣城市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (115, '福建', '福州市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (116, '福建', '厦门市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (117, '福建', '莆田市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (118, '福建', '三明市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (119, '福建', '泉州市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (120, '福建', '漳州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (121, '福建', '南平市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (122, '福建', '龙岩市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (123, '福建', '宁德市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (124, '江西', '南昌市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (125, '江西', '景德镇市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (126, '江西', '萍乡市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (127, '江西', '九江市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (128, '江西', '新余市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (129, '江西', '鹰潭市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (130, '江西', '赣州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (131, '江西', '吉安市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (132, '江西', '宜春市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (133, '江西', '抚州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (134, '江西', '上饶市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (135, '山东', '济南市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (136, '山东', '青岛市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (137, '山东', '淄博市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (138, '山东', '枣庄市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (139, '山东', '东营市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (140, '山东', '烟台市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (141, '山东', '潍坊市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (142, '山东', '济宁市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (143, '山东', '泰安市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (144, '山东', '威海市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (145, '山东', '日照市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (146, '山东', '莱芜市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (147, '山东', '临沂市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (148, '山东', '德州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (149, '山东', '聊城市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (150, '山东', '滨州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (151, '山东', '荷泽市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (152, '河南', '郑州市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (153, '河南', '开封市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (154, '河南', '洛阳市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (155, '河南', '平顶山市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (156, '河南', '安阳市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (157, '河南', '鹤壁市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (158, '河南', '新乡市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (159, '河南', '焦作市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (160, '河南', '濮阳市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (161, '河南', '许昌市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (162, '河南', '漯河市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (163, '河南', '三门峡市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (164, '河南', '南阳市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (165, '河南', '商丘市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (166, '河南', '信阳市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (167, '河南', '周口市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (168, '河南', '驻马店市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (169, '湖北', '武汉市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (170, '湖北', '黄石市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (171, '湖北', '十堰市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (172, '湖北', '宜昌市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (173, '湖北', '襄樊市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (174, '湖北', '鄂州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (175, '湖北', '荆门市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (176, '湖北', '孝感市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (177, '湖北', '荆州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (178, '湖北', '黄冈市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (179, '湖北', '咸宁市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (180, '湖北', '随州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (181, '湖北', '恩施土家族苗族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (182, '湖北', '神农架', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (183, '湖南', '长沙市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (184, '湖南', '株洲市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (185, '湖南', '湘潭市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (186, '湖南', '衡阳市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (187, '湖南', '邵阳市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (188, '湖南', '岳阳市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (189, '湖南', '常德市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (190, '湖南', '张家界市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (191, '湖南', '益阳市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (192, '湖南', '郴州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (193, '湖南', '永州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (194, '湖南', '怀化市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (195, '湖南', '娄底市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (196, '湖南', '湘西土家族苗族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (197, '广东', '广州市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (198, '广东', '韶关市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (199, '广东', '深圳市', 3);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (200, '广东', '珠海市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (201, '广东', '汕头市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (202, '广东', '佛山市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (203, '广东', '江门市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (204, '广东', '湛江市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (205, '广东', '茂名市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (206, '广东', '肇庆市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (207, '广东', '惠州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (208, '广东', '梅州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (209, '广东', '汕尾市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (210, '广东', '河源市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (211, '广东', '阳江市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (212, '广东', '清远市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (213, '广东', '东莞市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (214, '广东', '中山市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (215, '广东', '潮州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (216, '广东', '揭阳市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (217, '广东', '云浮市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (218, '广西', '南宁市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (219, '广西', '柳州市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (220, '广西', '桂林市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (221, '广西', '梧州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (222, '广西', '北海市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (223, '广西', '防城港市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (224, '广西', '钦州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (225, '广西', '贵港市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (226, '广西', '玉林市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (227, '广西', '百色市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (228, '广西', '贺州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (229, '广西', '河池市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (230, '广西', '来宾市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (231, '广西', '崇左市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (232, '海南', '海口市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (233, '海南', '三亚市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (234, '重庆', '重庆市', 3);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (235, '四川', '成都市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (236, '四川', '自贡市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (237, '四川', '攀枝花市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (238, '四川', '泸州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (239, '四川', '德阳市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (240, '四川', '绵阳市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (241, '四川', '广元市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (242, '四川', '遂宁市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (243, '四川', '内江市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (244, '四川', '乐山市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (245, '四川', '南充市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (246, '四川', '眉山市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (247, '四川', '宜宾市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (248, '四川', '广安市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (249, '四川', '达州市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (250, '四川', '雅安市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (251, '四川', '巴中市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (252, '四川', '资阳市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (253, '四川', '阿坝藏族羌族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (254, '四川', '甘孜藏族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (255, '四川', '凉山彝族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (256, '贵州', '贵阳市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (257, '贵州', '六盘水市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (258, '贵州', '遵义市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (259, '贵州', '安顺市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (260, '贵州', '铜仁地区', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (261, '贵州', '黔西南布依族苗族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (262, '贵州', '毕节地区', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (263, '贵州', '黔东南苗族侗族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (264, '贵州', '黔南布依族苗族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (265, '云南', '昆明市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (266, '云南', '曲靖市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (267, '云南', '玉溪市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (268, '云南', '保山市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (269, '云南', '昭通市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (270, '云南', '丽江市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (271, '云南', '思茅市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (272, '云南', '临沧市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (273, '云南', '楚雄彝族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (274, '云南', '红河哈尼族彝族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (275, '云南', '文山壮族苗族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (276, '云南', '西双版纳傣族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (277, '云南', '大理白族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (278, '云南', '德宏傣族景颇族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (279, '云南', '怒江傈僳族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (280, '云南', '迪庆藏族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (281, '西藏', '拉萨市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (282, '西藏', '昌都地区', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (283, '西藏', '山南地区', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (284, '西藏', '日喀则地区', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (285, '西藏', '那曲地区', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (286, '西藏', '阿里地区', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (287, '西藏', '林芝地区', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (288, '陕西', '西安市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (289, '陕西', '铜川市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (290, '陕西', '宝鸡市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (291, '陕西', '咸阳市', 4);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (292, '陕西', '渭南市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (293, '陕西', '延安市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (294, '陕西', '汉中市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (295, '陕西', '榆林市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (296, '陕西', '安康市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (297, '陕西', '商洛市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (298, '甘肃', '兰州市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (299, '甘肃', '嘉峪关市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (300, '甘肃', '金昌市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (301, '甘肃', '白银市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (302, '甘肃', '天水市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (303, '甘肃', '武威市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (304, '甘肃', '张掖市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (305, '甘肃', '平凉市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (306, '甘肃', '酒泉市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (307, '甘肃', '庆阳市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (308, '甘肃', '定西市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (309, '甘肃', '陇南市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (310, '甘肃', '临夏回族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (311, '甘肃', '甘南藏族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (312, '青海', '西宁市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (313, '青海', '海东地区', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (314, '青海', '海北藏族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (315, '青海', '黄南藏族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (316, '青海', '海南藏族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (317, '青海', '果洛藏族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (318, '青海', '玉树藏族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (319, '青海', '海西蒙古族藏族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (320, '宁夏', '银川市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (321, '宁夏', '石嘴山市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (322, '宁夏', '吴忠市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (323, '宁夏', '固原市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (324, '宁夏', '中卫市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (325, '新疆', '乌鲁木齐市', 2);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (326, '新疆', '克拉玛依市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (327, '新疆', '吐鲁番地区', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (328, '新疆', '哈密地区', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (329, '新疆', '昌吉回族自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (330, '新疆', '博尔塔拉蒙古自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (331, '新疆', '巴音郭楞蒙古自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (332, '新疆', '阿克苏地区', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (333, '新疆', '克孜勒苏柯尔克孜自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (334, '新疆', '喀什地区', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (335, '新疆', '和田地区', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (336, '新疆', '伊犁哈萨克自治州', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (337, '新疆', '塔城地区', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (338, '新疆', '阿勒泰地区', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (339, '新疆', '石河子市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (340, '新疆', '阿拉尔市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (341, '新疆', '图木舒克市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (342, '新疆', '五家渠市', 5);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (343, '香港', '香港特别行政区', 3);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (344, '澳门', '澳门特别行政区', 3);
INSERT INTO lu_province_city (id, province, city, tag_id) VALUES (345, '台湾', '台湾省', 3);

#lu_tag
INSERT INTO lu_tag (id, name, tag_by, tag_datetime) VALUES (1, '京城', '1', '2016-05-10 02:33:31');
INSERT INTO lu_tag (id, name, tag_by, tag_datetime) VALUES (2, '省会城市', '1', '2016-05-10 02:33:31');
INSERT INTO lu_tag (id, name, tag_by, tag_datetime) VALUES (3, '大都市', '1', '2016-05-10 02:33:31');
INSERT INTO lu_tag (id, name, tag_by, tag_datetime) VALUES (4, '大城市', '1', '2016-05-10 02:33:32');
INSERT INTO lu_tag (id, name, tag_by, tag_datetime) VALUES (5, '县城', '1', '2016-05-10 02:33:32');
