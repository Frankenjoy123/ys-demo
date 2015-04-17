#product_status_lkp
DROP TABLE IF EXISTS product_status_lkp;
CREATE TABLE product_status_lkp
(
  code        VARCHAR(100) PRIMARY KEY NOT NULL,
  name        VARCHAR(100)             NOT NULL,
  description VARCHAR(255),
  active      BIT DEFAULT b'1'         NOT NULL
);
INSERT INTO product_status_lkp (code, name, description, active)
VALUES
  ('new', '未激活', '产品初始状态', TRUE),
  ('activated', '已激活', '已出厂激活', TRUE),
  ('recalled', '已召回', '已召回', TRUE),
  ('deleted', '已删除', '已删除', TRUE);


#product_key_type_lkp
DROP TABLE IF EXISTS product_key_type_lkp;
CREATE TABLE product_key_type_lkp
(
  code        VARCHAR(100) PRIMARY KEY NOT NULL,
  name        VARCHAR(100)             NOT NULL,
  description VARCHAR(255),
  active      BIT DEFAULT b'1'         NOT NULL
);
INSERT INTO product_key_type_lkp (code, name, description, active)
VALUES
  ('qr_public', '二维码', '二维码', TRUE),
  ('qr_secure', '隐藏二维码', '隐藏二维码', TRUE),
  ('rfid', 'RFID标签', 'RFID标签', TRUE);


#product_key_batch_status_lkp
DROP TABLE IF EXISTS product_key_batch_status_lkp;
CREATE TABLE product_key_batch_status_lkp
(
  code        VARCHAR(100) PRIMARY KEY NOT NULL,
  name        VARCHAR(100)             NOT NULL,
  description VARCHAR(255),
  active      BIT DEFAULT b'1'         NOT NULL
);
INSERT INTO product_key_batch_status_lkp (code, name, description, active)
VALUES
  ('new', '新建', 'new', TRUE),
  ('ready', '有效', 'ready', TRUE);


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
  ('*', '全部', 'all', TRUE),
  ('product', '产品', 'product', TRUE),
  ('productkey', '产品码', 'productkey', TRUE);


#permission_action_lkp
DROP TABLE IF EXISTS permission_action_lkp;
CREATE TABLE permission_action_lkp
(
  code        VARCHAR(100) PRIMARY KEY NOT NULL,
  name        VARCHAR(100)             NOT NULL,
  description VARCHAR(255),
  active      BIT DEFAULT b'1'         NOT NULL
);
INSERT INTO permission_action_lkp (code, name, description, active)
VALUES
  ('read', '读取', 'read', TRUE),
  ('create', '创建', 'create', TRUE),
  ('modify', '修改', 'modify', TRUE),
  ('delete', '删除', 'delete', TRUE),
  ('*', '完全控制', 'full control', TRUE);

