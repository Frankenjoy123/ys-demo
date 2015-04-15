#product_key_batch
DROP TABLE IF EXISTS product_key_batch;
CREATE TABLE product_key_batch
(
  id                     VARCHAR(20) PRIMARY KEY NOT NULL,
  quantity               INT                     NOT NULL,
  status_code            VARCHAR(100)            NOT NULL,
  product_key_type_codes VARCHAR(500)            NOT NULL,
  product_base_id        VARCHAR(20),
  product_keys_address   VARCHAR(255),
  org_id                 VARCHAR(20),
  created_client_id      VARCHAR(20)             NOT NULL,
  created_account_id     VARCHAR(20)             NOT NULL,
  created_datetime       DATETIME                NOT NULL
);
CREATE UNIQUE INDEX id_UNIQUE ON product_key_batch (id);
