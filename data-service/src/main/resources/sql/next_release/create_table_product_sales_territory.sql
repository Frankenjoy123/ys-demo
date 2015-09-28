#product_sales_territory
DROP TABLE IF EXISTS product_sales_territory;
CREATE TABLE product_sales_territory
(
  id                  CHAR(19) PRIMARY KEY NOT NULL,
  product_key         CHAR(22)             NOT NULL,
  org_agency_id       CHAR(19)             NOT NULL,
  created_account_id  CHAR(19)             NOT NULL,
  created_datetime    DATETIME             NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modified_account_id CHAR(19),
  modified_datetime   DATETIME
);
CREATE INDEX product_key ON product_sales_territory (product_key);
