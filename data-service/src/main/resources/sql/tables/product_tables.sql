#product_key_batch
DROP TABLE IF EXISTS product_key_batch;
CREATE TABLE product_key_batch
(
  id                     VARCHAR(20) PRIMARY KEY NOT NULL,
  quantity               INT                     NOT NULL,
  status_code            VARCHAR(100)            NOT NULL,
  product_key_type_codes VARCHAR(500)            NOT NULL,
  product_base_id        VARCHAR(20),
  product_keys_uri       VARCHAR(255),
  org_id                 VARCHAR(20),
  created_app_id         VARCHAR(20)             NOT NULL,
  created_account_id     VARCHAR(20)             NOT NULL,
  created_datetime       DATETIME                NOT NULL
);

DROP TABLE IF EXISTS product_key_order;
CREATE TABLE product_key_order
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

DROP TABLE IF EXISTS product_key_transaction_detail;
CREATE TABLE product_key_transaction_detail
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
