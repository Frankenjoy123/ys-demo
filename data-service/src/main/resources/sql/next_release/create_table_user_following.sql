DROP TABLE IF EXISTS user_organization_following;
CREATE TABLE user_organization_following (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  user_id VARCHAR(25) NOT NULL,
  org_id VARCHAR(20) NOT NULL,
  is_following BIT,
  created_datetime DATETIME,
  modified_datetime DATETIME

);

DROP TABLE IF EXISTS user_product_following;
CREATE TABLE user_product_following (
  id INT NOT NULL PRIMARY KEY  AUTO_INCREMENT,
  user_id VARCHAR(25) NOT NULL,
  product_base_id VARCHAR(20) NOT NULL,
  is_following BIT,
  created_datetime DATETIME,
  modified_datetime DATETIME
);
