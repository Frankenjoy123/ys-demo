#organization
INSERT INTO organization (id, name, type_code, status_code, description, details, created_account_id, created_datetime)
VALUES ('2wm774cv7pjg1cikcsn', '乔盛集团', 'carrier', 'available', NULL, NULL, '0010000000000000000', now());

#account
INSERT INTO account (id, org_id, identifier, status_code, first_name, last_name, email, phone, password, hash_salt, created_account_id, created_datetime, modified_account_id, modified_datetime)
VALUES ('2m70se22fyhp1f37b02', '2wm774cv7pjg1cikcsn', 'admin', 'available', '管理员', '', 'it@qsact.cn', '',
        'f825dfb56ec9f6b04bd20248a16882b9ffaff4a7', 'ThyX6u6W', '0010000000000000000', now(), NULL, NULL);

#group
INSERT INTO `group` (id, org_id, name, description, created_account_id, created_datetime, modified_account_id, modified_datetime)
VALUES
  ('2m70rf0pxp85i7z0701', '2wm774cv7pjg1cikcsn', 'Administrators', '管理员组', '0010000000000000000', now(), NULL, NULL);

#permissoin_allocatoin
INSERT INTO permission_allocation (id, principal, restriction, permission, effect, created_account_id, created_datetime)
VALUES
  ('2m70w1nwn7ajer1iwl0', 'group/2m70rf0pxp85i7z0701', 'org/*', 'policy/admin', 'allow', '0010000000000000000', now());
INSERT INTO permission_allocation (id, principal, restriction, permission, effect, created_account_id, created_datetime)
VALUES ('2m70w2mxzpjsya8n3p2', 'account/2m70se22fyhp1f37b02', 'org/*', '*:*', 'allow', '0010000000000000000', now());

#domain_directory
INSERT INTO domain_directory (name, description, org_id) VALUES ('*.qsact.cn', '乔盛集团', '2wm774cv7pjg1cikcsn');
INSERT INTO domain_directory (name, description, org_id) VALUES ('*.qsact.com', '乔盛集团', '2wm774cv7pjg1cikcsn');
