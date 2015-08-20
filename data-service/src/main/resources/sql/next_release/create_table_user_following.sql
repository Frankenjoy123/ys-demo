DROP TABLE IF EXISTS user_organization_following;
CREATE TABLE user_organization_following (
  id VARCHAR(20) PRIMARY KEY NOT NULL ,
  user_id VARCHAR(20) NOT NULL,
  org_id VARCHAR(20) NOT NULL,
  created_datetime DATETIME
);

CREATE UNIQUE INDEX id ON user_organization_following (id);

DROP TABLE IF EXISTS user_product_following;
CREATE TABLE user_product_following (
  id VARCHAR(20) PRIMARY KEY NOT NULL ,
  user_id VARCHAR(20) NOT NULL,
  product_base_id VARCHAR(20) NOT NULL,
  created_datetime DATETIME
);

CREATE UNIQUE INDEX id ON user_product_following (id);
