DROP TABLE IF EXISTS user_organization_following;
CREATE TABLE user_organization_following (
  id      CHAR(19) PRIMARY KEY NOT NULL,
  user_id CHAR(19)             NOT NULL,
  org_id  CHAR(19)             NOT NULL,
  created_datetime DATETIME
);

CREATE UNIQUE INDEX id ON user_organization_following (id);

DROP TABLE IF EXISTS user_product_following;
CREATE TABLE user_product_following (
  id              CHAR(19) PRIMARY KEY NOT NULL,
  user_id         CHAR(19)             NOT NULL,
  product_base_id CHAR(19)             NOT NULL,
  created_datetime DATETIME
);

CREATE UNIQUE INDEX id ON user_product_following (id);
