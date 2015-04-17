#organization
DROP TABLE IF EXISTS organization;
CREATE TABLE organization
(
  id                 VARCHAR(20) PRIMARY KEY                     NOT NULL,
  name               VARCHAR(255)                                NOT NULL,
  type_code          VARCHAR(100)                                NOT NULL,
  status_code        VARCHAR(100)                                NOT NULL,
  description        VARCHAR(4000)                               NULL,
  image_uri          VARCHAR(255)                                NULL,
  details            VARCHAR(4000)                               NULL,
  created_account_id VARCHAR(20)                                 NOT NULL,
  created_datetime   DATETIME DEFAULT CURRENT_TIMESTAMP          NOT NULL

);
INSERT INTO organization (id, name, type_code, status_code, description, image_uri, details, created_account_id)
VALUES
  ('2k0r1l55i2rs5544wz5', '���ݿƼ�', 'tech', 'available', '��ע������+������!', NULL, '��ϸ������Ϣ', '1'),
  ('2k0r2yvydbxbvibvgfm', 'ĳ�������޹�˾', 'manufacturer', 'available', '�����裬���ϻ�', NULL, '', '1'),
  ('2k0r306o609oljxd1wz', 'ĳɽȪˮ��˾', 'manufacturer', 'available', '����Ȼ�İ��˹�', NULL, '', '1');

