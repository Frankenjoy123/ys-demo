#product_comments
DROP TABLE IF EXISTS product_comments;
CREATE TABLE product_comments
(
  id                 VARCHAR(20) PRIMARY KEY NOT NULL,
  product_base_id   VARCHAR(20)             NOT NULL,
  comments           VARCHAR(500)           NOT NULL,
  score               INT                    NOT NULL,
  created_account_id VARCHAR(20),          NOT NULL,
  created_datetime   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);