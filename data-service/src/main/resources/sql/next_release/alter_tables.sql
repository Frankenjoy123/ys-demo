ALTER TABLE application ADD system_version VARCHAR(10)
AFTER description;

ALTER TABLE organization DROP image_uri;

ALTER TABLE product_key_batch ADD rest_quantity INT NOT NULL DEFAULT 0;

