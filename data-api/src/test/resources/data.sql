INSERT INTO organization
(id, name, type_code, status_code, description, details, created_account_id, created_datetime)
VALUES ('2k0r1l55i2rs5544wz5', '云溯科技', 'tech', 'available', '关注互联网+的力量!', '详细描述信息', '2k0rahgcybh0l5uxtep',
        '2015-05-01 00:00:00');

INSERT INTO account (id, org_id, identifier, status_code, first_name, last_name, email, phone, password, hash_salt, created_account_id, created_datetime, modified_account_id, modified_datetime)
VALUES ('0010000000000000000', '2k0r1l55i2rs5544wz5', 'system', 'available', '系统帐号', '云溯', 'it@yunsu.co', '', '', '',
        '0010000000000000000', '2015-05-01 00:00:00', NULL, NULL);

INSERT INTO user (id, device_id, phone, name, status_code, point, address, created_datetime)
VALUES
  ('0020000000000000000', '00000000000000000000000000000000', NULL, '匿名', 'enabled', 0, NULL, '2015-05-01 00:00:00');
