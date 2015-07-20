#application
DROP TABLE IF EXISTS application;
CREATE TABLE application
(
  id          VARCHAR(20) PRIMARY KEY NOT NULL,
  name        VARCHAR(100)            NOT NULL,
  description VARCHAR(255)            NOT NULL
);
CREATE UNIQUE INDEX id_UNIQUE ON application (id);

INSERT INTO `yunsoo2015DB`.`application` (`id`, `name`, `description`) VALUES ('1', '云溯 iOS 1.0', '云溯 iOS 1.0');
INSERT INTO `yunsoo2015DB`.`application` (`id`, `name`, `description`) VALUES ('2', '云溯 安卓 1.0', '云溯 安卓 1.0');
INSERT INTO `yunsoo2015DB`.`application` (`id`, `name`, `description`) VALUES ('3', '云溯移动Web 1.0', '云溯移动Web 1.0');

