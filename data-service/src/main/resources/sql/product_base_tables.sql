#product_base
CREATE TABLE product_base
(
  id                     VARCHAR(20) PRIMARY KEY NOT NULL,
  version                INT                     NOT NULL,
  org_id                 VARCHAR(20)             NOT NULL,
  category_id            INT                     NOT NULL,
  name                   VARCHAR(255)            NOT NULL,
  description            VARCHAR(255)            NOT NULL,
  shelf_life             INT                     NOT NULL,
  shelf_life_interval    VARCHAR(8)              NOT NULL,
  product_key_type_codes VARCHAR(45)             NOT NULL,
  status_code            VARCHAR(20)             NOT NULL,
  barcode                VARCHAR(100)            NOT NULL,
  child_product_count    INT,
  comments               VARCHAR(500)            NOT NULL,
  created_datetime       DATETIME                NOT NULL,
  modified_datetime      DATETIME
);


#product_base_versions
CREATE TABLE product_base_versions
(
  id                     VARCHAR(20) PRIMARY KEY NOT NULL,
  product_base_id        VARCHAR(20)             NOT NULL,
  version                INT                     NOT NULL,
  org_id                 VARCHAR(20)             NOT NULL,
  category_id            INT                     NOT NULL,
  name                   VARCHAR(255)            NOT NULL,
  description            VARCHAR(255)            NOT NULL,
  shelf_life             INT                     NOT NULL,
  shelf_life_interval    VARCHAR(8)              NOT NULL,
  product_key_type_codes VARCHAR(45)             NOT NULL,
  status_code            VARCHAR(20)             NOT NULL,
  barcode                VARCHAR(100)            NOT NULL,
  child_product_count    INT,
  comments               VARCHAR(500)            NOT NULL,
  created_datetime       DATETIME                NOT NULL,
  modified_datetime      DATETIME
);

