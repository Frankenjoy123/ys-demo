#product_sales_territory
DROP TABLE IF EXISTS product_sales_territory;
CREATE TABLE product_sales_territory
(
  id                 VARCHAR(20) PRIMARY KEY NOT NULL,
  product_key       VARCHAR(25)             NOT NULL,
  location_id           VARCHAR(20)           NOT NULL,
  created_account_id VARCHAR(20)          NOT NULL,
  created_datetime   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modified_account_id VARCHAR(20)          DEFAULT NULL,
  modified_datetime   DATETIME       DEFAULT NULL
);