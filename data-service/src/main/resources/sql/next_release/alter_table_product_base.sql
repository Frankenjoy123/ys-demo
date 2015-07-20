ALTER TABLE yunsoo2015DB.product_base
CHANGE COLUMN id id VARCHAR(20) NOT NULL,
ADD COLUMN version INT NOT NULL DEFAULT 1
AFTER id,
CHANGE COLUMN status status_code VARCHAR(20) NOT NULL
AFTER version,
CHANGE COLUMN org_id org_id VARCHAR(20) NOT NULL
AFTER status_code,
ADD COLUMN description VARCHAR(255) NULL
AFTER name,
CHANGE COLUMN barcode barcode VARCHAR(30) NOT NULL
AFTER description,
CHANGE COLUMN product_key_type_ids product_key_type_codes VARCHAR(100) NOT NULL
AFTER barcode,
CHANGE COLUMN shelf_life shelf_life INT(11) NULL,
CHANGE COLUMN shelf_life_interval shelf_life_interval VARCHAR(8) NULL,
CHANGE COLUMN child_pt_count child_product_count INT(11) NULL,
CHANGE COLUMN comment comments VARCHAR(500) NOT NULL
AFTER child_product_count,
ADD COLUMN is_deleted BIT NOT NULL DEFAULT 0
AFTER comments;
