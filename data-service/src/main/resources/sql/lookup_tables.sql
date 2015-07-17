#product_status_lkp
DROP TABLE IF EXISTS product_status_lkp;
CREATE TABLE product_status_lkp
(
  code        VARCHAR(20) PRIMARY KEY  NOT NULL,
  name        VARCHAR(100)             NOT NULL,
  description VARCHAR(255),
  active      BIT DEFAULT b'1'         NOT NULL
);
INSERT INTO product_status_lkp (code, name, description, active)
VALUES
  ('created', '未激活', '产品初始状态', TRUE),
  ('activated', '已激活', '已出厂激活', TRUE),
  ('recalled', '已召回', '已召回', TRUE),
  ('deleted', '已删除', '已删除', TRUE);


#product_key_type_lkp
DROP TABLE IF EXISTS product_key_type_lkp;
CREATE TABLE product_key_type_lkp
(
  code        VARCHAR(20) PRIMARY KEY  NOT NULL,
  name        VARCHAR(100)             NOT NULL,
  description VARCHAR(255),
  active      BIT DEFAULT b'1'         NOT NULL
);
INSERT INTO product_key_type_lkp (code, name, description, active)
VALUES
  ('qr_secure', '隐藏二维码', '隐藏二维码', TRUE),
  ('qr_public', '二维码', '二维码', TRUE),
  ('rfid', 'RFID标签', 'RFID标签', TRUE);


#product_key_batch_status_lkp
DROP TABLE IF EXISTS product_key_batch_status_lkp;
CREATE TABLE product_key_batch_status_lkp
(
  code        VARCHAR(20) PRIMARY KEY  NOT NULL,
  name        VARCHAR(100)             NOT NULL,
  description VARCHAR(255),
  active      BIT DEFAULT b'1'         NOT NULL
);
INSERT INTO product_key_batch_status_lkp (code, name, description, active)
VALUES
  ('new', '新建', 'new', TRUE),
  ('creating', '审核中', 'creating', TRUE),
  ('available', '有效', 'available', TRUE),
  ('downloaded', '已下载', 'downloaded', TRUE),
  ('deleted', '已删除', 'deleted', TRUE);


#org_status_lkp
DROP TABLE IF EXISTS org_status_lkp;
CREATE TABLE org_status_lkp
(
  code        VARCHAR(20) PRIMARY KEY  NOT NULL,
  name        VARCHAR(100)             NOT NULL,
  description VARCHAR(255),
  active      BIT DEFAULT b'1'         NOT NULL
);
INSERT INTO org_status_lkp (code, name, description, active)
VALUES
  ('created', '已创建', '已经创建', TRUE),
  ('verified', '已认证', '通过平台认证', TRUE),
  ('blocked', '被阻止', '被加入黑名单', TRUE),
  ('deleted', '已删除', '被删除了', TRUE);


#account_status_lkp
DROP TABLE IF EXISTS account_status_lkp;
CREATE TABLE account_status_lkp
(
  code        VARCHAR(20) PRIMARY KEY  NOT NULL,
  name        VARCHAR(100)             NOT NULL,
  description VARCHAR(255),
  active      BIT DEFAULT b'1'         NOT NULL
);
INSERT INTO account_status_lkp (code, name, description, active)
VALUES
  ('activated', '正常', '正常', TRUE),
  ('deactivated', '停用', '已停用', TRUE);

#user_status_lkp
DROP TABLE IF EXISTS user_status_lkp;
CREATE TABLE user_status_lkp
(
  code        VARCHAR(20) PRIMARY KEY  NOT NULL,
  name        VARCHAR(100)             NOT NULL,
  description VARCHAR(255),
  active      BIT DEFAULT b'1'         NOT NULL
);


#message_status_lkp
DROP TABLE IF EXISTS message_status_lkp;
CREATE TABLE message_status_lkp
(
  code        VARCHAR(20) PRIMARY KEY  NOT NULL,
  name        VARCHAR(100)             NOT NULL,
  description VARCHAR(255),
  active      BIT DEFAULT b'1'         NOT NULL
);


#message_type_lkp
DROP TABLE IF EXISTS message_type_lkp;
CREATE TABLE message_type_lkp
(
  code        VARCHAR(20) PRIMARY KEY  NOT NULL,
  name        VARCHAR(100)             NOT NULL,
  description VARCHAR(255),
  active      BIT DEFAULT b'1'         NOT NULL
);


#product_file_type_lkp
DROP TABLE IF EXISTS product_file_type_lkp;
CREATE TABLE product_file_type_lkp
(
  code        VARCHAR(20) PRIMARY KEY  NOT NULL,
  name        VARCHAR(100)             NOT NULL,
  description VARCHAR(255),
  active      BIT DEFAULT b'1'         NOT NULL
);
INSERT INTO product_file_type_lkp (code, name, description, active)
VALUES
  ('package', '包装', '包装', TRUE),
  ('logistics', '物流', '物流', TRUE);

