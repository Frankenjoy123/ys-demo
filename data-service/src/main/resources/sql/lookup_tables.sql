#product_status_lkp
DROP TABLE IF EXISTS product_status_lkp;
CREATE TABLE product_status_lkp
(
  id          INT PRIMARY KEY  NOT NULL,
  code        VARCHAR(20)      NOT NULL,
  name        VARCHAR(40)      NOT NULL,
  description VARCHAR(255),
  active      BIT DEFAULT b'1' NOT NULL
);
CREATE UNIQUE INDEX id_UNIQUE ON product_status_lkp (id);

INSERT INTO product_status_lkp (id, code, name, description, active) VALUES (0, 'NEW', '未激活', '产品初始状态', TRUE);
INSERT INTO product_status_lkp (id, code, name, description, active) VALUES (1, 'ACTIVATED', '已激活', '已出厂激活', TRUE);
INSERT INTO product_status_lkp (id, code, name, description, active) VALUES (2, 'RECALLED', '已召回', '已召回', TRUE);
INSERT INTO product_status_lkp (id, code, name, description, active) VALUES (3, 'DELETED', '已删除', '已删除', TRUE);

#product_key_type_lkp
DROP TABLE IF EXISTS product_key_type_lkp;
CREATE TABLE product_key_type_lkp
(
  id          INT PRIMARY KEY  NOT NULL,
  code        VARCHAR(20)      NOT NULL,
  name        VARCHAR(40)      NOT NULL,
  description VARCHAR(255),
  active      BIT DEFAULT b'1' NOT NULL
);
CREATE UNIQUE INDEX id_UNIQUE ON product_key_type_lkp (id);

INSERT INTO product_key_type_lkp (id, code, name, description, active) VALUES (1, 'QRCODE_PUBLIC', '二维码', '二维码', TRUE);
INSERT INTO product_key_type_lkp (id, code, name, description, active)
VALUES (2, 'QRCODE_SECURE', '隐藏二维码', '隐藏二维码', TRUE);
INSERT INTO product_key_type_lkp (id, code, name, description, active) VALUES (3, 'RFID', 'RFID标签', 'RFID标签', TRUE);

#product_key_batch_status_lkp
DROP TABLE IF EXISTS product_key_batch_status_lkp;
CREATE TABLE product_key_batch_status_lkp
(
  id          INT PRIMARY KEY  NOT NULL,
  code        VARCHAR(20)      NOT NULL,
  name        VARCHAR(40)      NOT NULL,
  description VARCHAR(255),
  active      BIT DEFAULT b'1' NOT NULL
);
CREATE UNIQUE INDEX id_UNIQUE ON product_key_batch_status_lkp (id);

INSERT INTO product_key_batch_status_lkp (id, code, name, description, active) VALUES (0, 'NEW', '新建', '新建', TRUE);

#resource_lkp
DROP TABLE IF EXISTS permission_resource_lkp;
CREATE TABLE permission_resource_lkp
(
  code        VARCHAR(100) PRIMARY KEY NOT NULL,
  name        VARCHAR(100)             NOT NULL,
  description VARCHAR(255),
  active      BIT DEFAULT b'1'         NOT NULL
);
INSERT INTO permission_resource_lkp (code, name, description)
VALUES
  ('*', '全部', 'all'),
  ('product', '产品', 'product'),
  ('productkey', '产品码', 'productkey');

#action_lkp
DROP TABLE IF EXISTS permission_action_lkp;
CREATE TABLE permission_action_lkp
(
  code        VARCHAR(100) PRIMARY KEY NOT NULL,
  name        VARCHAR(100)             NOT NULL,
  description VARCHAR(255),
  active      BIT DEFAULT b'1'         NOT NULL
);
INSERT INTO permission_action_lkp (code, name, description)
VALUES
  ('read', '读取', 'read'),
  ('create', '创建', 'create'),
  ('modify', '修改', 'modify'),
  ('delete', '删除', 'delete'),
  ('*', '完全控制', 'full control');

