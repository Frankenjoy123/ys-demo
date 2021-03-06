#product_base
CREATE TABLE product_base
(
  id                     VARCHAR(20) PRIMARY KEY                             NOT NULL,
  version                INT DEFAULT 1                                       NOT NULL,
  status_code            VARCHAR(20)                                         NOT NULL,
  org_id                 VARCHAR(20)                                         NOT NULL,
  category_id            VARCHAR(20)                                         NOT NULL,
  name                   VARCHAR(255)                                        NOT NULL,
  description            VARCHAR(255),
  barcode                VARCHAR(30)                                         NOT NULL,
  product_key_type_codes VARCHAR(100)                                        NOT NULL,
  shelf_life             INT,
  shelf_life_interval    VARCHAR(8),
  child_product_count    INT,
  comments               VARCHAR(500),
  deleted                BIT DEFAULT b'0'                                    NOT NULL,
  created_account_id     VARCHAR(20)                                         NOT NULL,
  created_datetime       DATETIME DEFAULT CURRENT_TIMESTAMP                  NOT NULL,
  modified_account_id    VARCHAR(20),
  modified_datetime      DATETIME
);


#product_base_versions
CREATE TABLE product_base_versions
(
  id                     VARCHAR(20) PRIMARY KEY                           NOT NULL,
  product_base_id        VARCHAR(20)                                       NOT NULL,
  version                INT                                               NOT NULL,
  status_code            VARCHAR(20)                                       NOT NULL,
  org_id                 VARCHAR(20)                                       NOT NULL,
  category_id            VARCHAR(20)                                       NOT NULL,
  name                   VARCHAR(255)                                      NOT NULL,
  description            VARCHAR(255),
  barcode                VARCHAR(30)                                       NOT NULL,
  product_key_type_codes VARCHAR(100)                                      NOT NULL,
  shelf_life             INT,
  shelf_life_interval    VARCHAR(8),
  child_product_count    INT,
  comments               VARCHAR(500),
  review_comments        VARCHAR(4000),
  created_account_id     VARCHAR(20)                                       NOT NULL,
  created_datetime       DATETIME DEFAULT CURRENT_TIMESTAMP                NOT NULL,
  modified_account_id    VARCHAR(20),
  modified_datetime      DATETIME
);
CREATE UNIQUE INDEX product_base_id_version ON product_base_versions (product_base_id, version);

