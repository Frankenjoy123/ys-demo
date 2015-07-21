#product_base
ALTER TABLE yunsoo2015DB.product_base
CHANGE COLUMN id id VARCHAR(20) NOT NULL,
ADD COLUMN version INT NOT NULL DEFAULT 1
AFTER id,
CHANGE COLUMN status status_code VARCHAR(20) NOT NULL
AFTER version,
CHANGE COLUMN org_id org_id VARCHAR(20) NOT NULL
AFTER status_code,
CHANGE COLUMN category_id category_id VARCHAR(20) NOT NULL,
ADD COLUMN description VARCHAR(255) NULL
AFTER name,
CHANGE COLUMN barcode barcode VARCHAR(30) NOT NULL
AFTER description,
CHANGE COLUMN product_key_type_ids product_key_type_codes VARCHAR(100) NOT NULL
AFTER barcode,
CHANGE COLUMN shelf_life shelf_life INT(11) NULL,
CHANGE COLUMN shelf_life_interval shelf_life_interval VARCHAR(8) NULL,
CHANGE COLUMN child_pt_count child_product_count INT(11) NULL,
CHANGE COLUMN comment comments VARCHAR(500) NOT NULL
AFTER child_product_count,
ADD COLUMN deleted BIT NOT NULL DEFAULT 0
AFTER comments,
ADD COLUMN created_account_id VARCHAR(20) NOT NULL
AFTER deleted,
CHANGE COLUMN created_datetime created_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN modified_account_id VARCHAR(20)
AFTER created_datetime;

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

