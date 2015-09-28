ALTER TABLE application ADD system_version VARCHAR(10)
AFTER description;

ALTER TABLE organization DROP image_uri;

