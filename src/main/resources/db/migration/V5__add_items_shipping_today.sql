-- Set three existing item groups to ship today
UPDATE ITEM_GROUP
SET SHIPPING_DATE = CURRENT_DATE
WHERE ID IN (1, 2, 3);