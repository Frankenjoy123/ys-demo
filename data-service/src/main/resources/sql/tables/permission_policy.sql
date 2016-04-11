DROP TABLE IF EXISTS permission_policy;
CREATE TABLE permission_policy
(
  code        VARCHAR(50) PRIMARY KEY NOT NULL,
  name        VARCHAR(50)             NOT NULL,
  description VARCHAR(255),
  permissions VARCHAR(10000)          NOT NULL
);

INSERT INTO permission_policy (code, name, description, permissions)
VALUES
  ('admin', 'Admin', '管理员权限', '*:*'),
  ('default', 'Default', '所有帐号默认拥有的权限', 'account:read,organization:read'),
  ('read', 'Read', '只读权限', '*:read'),
  ('enterprise_page_dashboard_view', 'Dashboard View', '控制面板', 'enterprise_page_dashboard:read,product_base:read,product_key:read'),
  ('enterprise_page_product_base_view', 'Product Base View', '产品方案查看', 'enterprise_page_product_base:read,product_base:read'),
  ('enterprise_page_product_base_manage', 'Product Base Manage', '产品方案管理', 'enterprise_page_product_base:write,product_base:*'),
  ('enterprise_page_product_key_view', 'Product Key View', '产品码查看', 'enterprise_page_product_key:read,product_key:read'),
  ('enterprise_page_product_key_manage', 'Product Key Manage', '产品码管理', 'enterprise_page_product_key:write,product_key:*'),
  ('enterprise_page_package_key_view', 'Package Key View', '包装码查看', 'enterprise_page_package_key:read'),
  ('enterprise_page_package_key_manage', 'Package Key Manage', '包装码管理', 'enterprise_page_package_key:write'),
  ('enterprise_page_marketing_rule_view', 'Marketing Rule View', '营销方案查看', 'enterprise_page_marketing_rule:read'),
  ('enterprise_page_marketing_rule_manage', 'Marketing Rule Manage', '营销方案管理', 'enterprise_page_marketing_rule:write'),
  ('enterprise_page_marketing_prize_view', 'Marketing Prize View', '兑奖信息查看', 'enterprise_page_marketing_prize:read'),
  ('enterprise_page_marketing_prize_manage', 'Marketing Prize Manage', '兑奖信息管理', 'enterprise_page_marketing_prize:write'),
  ('enterprise_page_app_message_view', 'App Message View', 'App消息推送', 'enterprise_page_app_message:read'),
  ('enterprise_page_app_message_manage', 'App Message Manage', 'App消息推送', 'enterprise_page_app_message:write'),
  ('enterprise_page_product_feedback_view', 'Product Feedback View', '产品评论查看', 'enterprise_page_product_feedback:read'),
  ('enterprise_page_product_report_view', 'Product Report View', '产品举报查看', 'enterprise_page_product_report:read'),
  ('enterprise_page_agency_view', 'Agency View', '经销商查看', 'enterprise_page_agency:read'),
  ('enterprise_page_agency_manage', 'Agency Manage', '经销商管理', 'enterprise_page_agency:write'),
  ('enterprise_page_package_view', 'Package View', '包装查看', 'enterprise_page_package:read'),
  ('enterprise_page_package_manage', 'Package Manage', '包装管理', 'enterprise_page_package:write'),
  ('enterprise_page_logistics_view', 'Logistics View', '物流查看', 'enterprise_page_logistics:read'),
  ('enterprise_page_logistics_manage', 'Logistics Manage', '物流管理', 'enterprise_page_logistics:write'),
  ('enterprise_page_reports_view', 'Reports View', '数据分析查看', 'enterprise_page_reports:read,reports:read'),
  ('enterprise_page_account_view', 'Account View', '账号查看', 'enterprise_page_account:read,account:read,permission_allocation:read,enterprise_page_permission:read'),
  ('enterprise_page_account_manage', 'Account Manage', '账号管理', 'enterprise_page_account:write,account:*,permission_allocation:*,enterprise_page_permission:write'),
  ('enterprise_page_group_view', 'Group View', '账号组查看', 'enterprise_page_group:read,group:read,permission_allocation:read,enterprise_page_permission:read'),
  ('enterprise_page_group_manage', 'Group Manage', '账号组管理', 'enterprise_page_group:write,group:*,permission_allocation:*,enterprise_page_permission:write'),
  ('enterprise_page_device_view', 'Device View', '设备查看', 'enterprise_page_device:read,device:read'),
  ('enterprise_page_device_manage', 'Device Manage', '设备管理', 'enterprise_page_device:write,device:*'),
  ('_test', '_test', NULL, '*:read,account:read,group:write');

