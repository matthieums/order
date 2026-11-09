ALTER TABLE item_group
ALTER COLUMN shipping_date TYPE DATE
USING shipping_date::DATE;