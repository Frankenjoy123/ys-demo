#product_comments
DROP TABLE IF EXISTS product_comments;
CREATE TABLE product_comments
(
  id               CHAR(19) PRIMARY KEY NOT NULL,
  product_base_id  CHAR(19)             NOT NULL,
  comments         VARCHAR(500)         NOT NULL,
  score            INT                  NOT NULL,
  user_id          CHAR(19)             NOT NULL,
  created_datetime DATETIME             NOT NULL DEFAULT CURRENT_TIMESTAMP
);