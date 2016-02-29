#location
DROP TABLE IF EXISTS location;
CREATE TABLE location
(
  id          CHAR(19) PRIMARY KEY NOT NULL,
  name VARCHAR(20) NOT NULL,
  type_code   VARCHAR(20)          NOT NULL,
  longitude   DOUBLE               NOT NULL,
  latitude    DOUBLE               NOT NULL,
  description VARCHAR(255),
  parent_id   CHAR(19)
);

INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0bp', '安徽', 'province', 31.52, 117.17, '安徽省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0bq', '北京', 'province', 39.55, 116.24, '北京市', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0br', '重庆', 'province', 29.59, 106.54, '重庆市', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0bs', '福建', 'province', 26.05, 119.18, '福建省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0bt', '甘肃', 'province', 36.04, 103.51, '甘肃省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0bu', '广东', 'province', 23.08, 113.14, '广东省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0bv', '广西', 'province', 22.48, 108.19, '广西省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0bw', '贵州', 'province', 26.35, 106.42, '贵州省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0bx', '海南', 'province', 20.02, 110.2, '海南省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0by', '河北', 'province', 38.02, 114.3, '河北省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0bz', '河南', 'province', 34.46, 113.4, '河南省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0c0', '黑龙江', 'province', 45.44, 126.36, '黑龙江省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0c1', '湖北', 'province', 30.35, 114.17, '湖北省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0c2', '湖南', 'province', 28.12, 112.59, '湖南省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0c3', '吉林', 'province', 43.54, 125.19, '吉林省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0c4', '江苏', 'province', 32.03, 118.46, '江苏省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0c5', '江西', 'province', 28.4, 115.55, '江西省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0c6', '辽宁', 'province', 41.48, 123.25, '辽宁省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0c7', '内蒙古', 'province', 40.48, 111.41, '内蒙古自治区', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0c8', '宁夏', 'province', 38.27, 106.16, '宁夏回族自治区', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0c9', '青海', 'province', 36.38, 101.48, '青海省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0ca', '山东', 'province', 36.4, 117, '山东省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0cb', '山西', 'province', 37.54, 112.33, '山西省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0cc', '陕西', 'province', 34.17, 108.57, '陕西省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0cd', '上海', 'province', 31.14, 121.29, '上海市', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0ce', '四川', 'province', 30.4, 104.04, '四川省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0cf', '天津', 'province', 39.02, 117.12, '天津市', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0cg', '西藏', 'province', 29.39, 91.08, '西藏自治区', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0ch', '新疆', 'province', 43.45, 87.36, '新疆维吾尔族自治区', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0ci', '云南', 'province', 25.04, 102.42, '云南省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0cj', '浙江', 'province', 30.16, 120.1, '浙江省', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0ck', '香港', 'province', 21.23, 115.12, '香港', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0cl', '澳门', 'province', 21.33, 115.07, '澳门', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxsz1hak7lre1mj0cm', '台湾', 'province', 25.03, 121.3, '台湾', NULL);
INSERT INTO yunsoo2015DB.location (id, name, type_code, longitude, latitude, description, parent_id)
VALUES ('2kxt26hj0fhic24lv5e', '杭州', 'city', 120.16, 30.25, '杭州市', '2kxsz1hak7lre1mj0cj');
