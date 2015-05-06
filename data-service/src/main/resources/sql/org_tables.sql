#organization
DROP TABLE IF EXISTS organization;
CREATE TABLE organization
(
  id                 VARCHAR(20) PRIMARY KEY                     NOT NULL,
  name               VARCHAR(255)                                NOT NULL,
  type_code          VARCHAR(100)                                NOT NULL,
  status_code        VARCHAR(100)                                NOT NULL,
  description        VARCHAR(4000)                               NULL,
  image_uri          VARCHAR(255)                                NULL,
  details            VARCHAR(4000)                               NULL,
  created_account_id VARCHAR(20)                                 NOT NULL,
  created_datetime   DATETIME DEFAULT CURRENT_TIMESTAMP          NOT NULL

);
INSERT INTO organization (id, name, type_code, status_code, description, image_uri, details, created_account_id)
VALUES
  ('2k0r1l55i2rs5544wz5', '云溯科技', 'tech', 'available', '关注互联网+的力量!', NULL, '详细描述信息', '2k0rahgcybh0l5uxtep'),
  ('2k0r2yvydbxbvibvgfm', '某凉茶有限公司', 'manufacturer', 'available', '喝凉茶，不上火', NULL, '', '2k0rahgcybh0l5uxtep'),
  ('2k0r306o609oljxd1wz', '某山泉水公司', 'manufacturer', 'available', '大自然的搬运工', NULL, '', '2k0rahgcybh0l5uxtep'),
  ('2k0r2yvydbxbvibvgft', '某广东公司', 'manufacturer', 'available', '三个自信', NULL, '', '2k0rahgcybh0l5uxtep');


DROP TABLE IF EXISTS org_product_key_order;
CREATE TABLE org_product_key_order
(
  id                 VARCHAR(20) PRIMARY KEY                     NOT NULL,
  org_id             VARCHAR(20)                                 NOT NULL,
  total              BIGINT                                      NOT NULL,
  remain             BIGINT                                      NOT NULL,
  active             BIT                                         NOT NULL        DEFAULT b'1',
  product_base_id    VARCHAR(20),
  description        VARCHAR(255),
  created_account_id VARCHAR(20)                                 NOT NULL,
  created_datetime   DATETIME DEFAULT CURRENT_TIMESTAMP          NOT NULL,
  expire_datetime    DATETIME
);

DROP TABLE IF EXISTS org_product_key_transaction_detail;
CREATE TABLE org_product_key_transaction_detail
(
  id                   VARCHAR(20) PRIMARY KEY                       NOT NULL,
  transaction_id       VARCHAR(20)                                   NOT NULL,
  org_id               VARCHAR(20)                                   NOT NULL,
  product_key_batch_id VARCHAR(20)                                   NOT NULL,
  order_id             VARCHAR(20)                                   NOT NULL,
  quantity             BIGINT                                        NOT NULL,
  status_code          VARCHAR(20)                                   NOT NULL,
  created_account_id   VARCHAR(20)                                   NOT NULL,
  created_datetime     DATETIME DEFAULT CURRENT_TIMESTAMP            NOT NULL
);
