INSERT INTO users (
    id, user_type, first_name, last_name,
    email, password, address, phone_number
)
    OVERRIDING SYSTEM VALUE
VALUES
    (1, 'CUSTOMER', 'John', 'Smith', 'john.smith@example.com', 'password123', '12 Main Street, Brussels', '+32470111222'),
    (2, 'CUSTOMER', 'Sarah', 'Johnson', 'sarah.johnson@example.com', 'password123', '24 Oak Avenue, Antwerp', '+32470222333'),
    (3, 'CUSTOMER', 'Michael', 'Brown', 'michael.brown@example.com', 'password123', '8 Park Lane, Ghent', '+32470333444'),
    (4, 'CUSTOMER', 'Emma', 'Wilson', 'emma.wilson@example.com', 'password123', '45 River Road, Leuven', '+32470444555'),
    (5, 'CUSTOMER', 'David', 'Miller', 'david.miller@example.com', 'password123', '19 Green Street, Bruges', '+32470555666'),
    (6, 'CUSTOMER', 'Olivia', 'Davis', 'olivia.davis@example.com', 'password123', '7 Church Street, Mechelen', '+32470666777'),
    (7, 'CUSTOMER', 'James', 'Garcia', 'james.garcia@example.com', 'password123', '33 Station Road, Hasselt', '+32470777888'),
    (8, 'CUSTOMER', 'Sophia', 'Martinez', 'sophia.martinez@example.com', 'password123', '16 School Street, Namur', '+32470888999'),
    (9, 'CUSTOMER', 'William', 'Anderson', 'william.anderson@example.com', 'password123', '5 Hill Street, Brussels', '+32470999111'),
    (10, 'CUSTOMER', 'Isabella', 'Taylor', 'isabella.taylor@example.com', 'password123', '29 Market Square, Antwerp', '+32470000111'),
    (11, 'ADMIN', 'Admin', 'One', 'admin1@example.com', 'password123', NULL, NULL),
    (12, 'ADMIN', 'Admin', 'Two', 'admin2@example.com', 'password123', NULL, NULL);

INSERT INTO item (
    id, name, description, price, amount
)
    OVERRIDING SYSTEM VALUE
VALUES
    (1, 'Laptop', 'High-performance laptop', 899.99, 25),
    (2, 'Wireless Mouse', 'Ergonomic wireless mouse', 29.99, 150),
    (3, 'Mechanical Keyboard', 'RGB mechanical keyboard', 89.99, 40),
    (4, 'Monitor', '27-inch 4K monitor', 349.99, 12),
    (5, 'USB-C Cable', 'Fast charging USB-C cable', 12.50, 200),
    (6, 'Headphones', 'Noise-cancelling headphones', 149.99, 8),
    (7, 'Webcam', 'Full HD webcam', 59.99, 0),
    (8, 'Desk Lamp', 'LED adjustable desk lamp', 34.99, 75),
    (9, 'Office Chair', 'Ergonomic office chair', 249.99, 5),
    (10, 'External SSD', '1TB portable SSD', 119.99, 30),
    (11, 'Smartphone', 'Latest generation smartphone', 799.99, 3),
    (12, 'Tablet', '10-inch tablet', 429.99, 0),
    (13, 'Power Bank', '20000mAh power bank', 39.99, 100),
    (14, 'Bluetooth Speaker', 'Portable Bluetooth speaker', 69.99, 18),
    (15, 'Laptop Stand', 'Adjustable aluminium laptop stand', 44.99, 60);

INSERT INTO eurder (
    id, customer_id, archived
)
    OVERRIDING SYSTEM VALUE
VALUES
    (1, 1, FALSE),
    (2, 1, FALSE),
    (3, 1, TRUE),
    (4, 2, FALSE),
    (5, 2, FALSE),
    (6, 3, FALSE),
    (7, 3, FALSE),
    (8, 3, TRUE),
    (9, 4, FALSE),
    (10, 4, FALSE),
    (11, 5, FALSE),
    (12, 5, FALSE),
    (13, 6, FALSE),
    (14, 6, FALSE),
    (15, 7, FALSE),
    (16, 7, FALSE),
    (17, 8, FALSE),
    (18, 8, FALSE),
    (19, 8, TRUE),
    (20, 1, FALSE);

INSERT INTO item_group (
    id, order_id, item_id, amount, unit_price, shipping_date
)
    OVERRIDING SYSTEM VALUE
VALUES
-- Order 1: Customer 1
(1, 1, 1, 1, 899.99, CURRENT_DATE + 1),
(2, 1, 2, 2, 29.99, CURRENT_DATE + 1),
(3, 1, 5, 3, 12.50, CURRENT_DATE + 1),

-- Order 2: Customer 1
(4, 2, 4, 2, 349.99, CURRENT_DATE + 7),
(5, 2, 6, 1, 149.99, CURRENT_DATE + 1),

-- Order 3: Customer 1 (archived)
(6, 3, 3, 1, 89.99, CURRENT_DATE - 10),
(7, 3, 8, 2, 34.99, CURRENT_DATE - 10),

-- Order 4: Customer 2
(8, 4, 7, 2, 59.99, CURRENT_DATE + 7),
(9, 4, 13, 1, 39.99, CURRENT_DATE + 1),

-- Order 5: Customer 2
(10, 5, 9, 1, 249.99, CURRENT_DATE + 1),
(11, 5, 10, 2, 119.99, CURRENT_DATE + 1),

-- Order 6: Customer 3
(12, 6, 11, 1, 799.99, CURRENT_DATE + 1),
(13, 6, 2, 1, 27.99, CURRENT_DATE + 1),

-- Order 7: Customer 3
(14, 7, 12, 1, 429.99, CURRENT_DATE + 7),
(15, 7, 14, 2, 69.99, CURRENT_DATE + 1),

-- Order 8: Customer 3 (archived)
(16, 8, 5, 5, 12.50, CURRENT_DATE - 5),
(17, 8, 15, 1, 44.99, CURRENT_DATE - 5),

-- Order 9: Customer 4
(18, 9, 1, 2, 849.99, CURRENT_DATE + 1),
(19, 9, 3, 1, 89.99, CURRENT_DATE + 1),

-- Order 10: Customer 4
(20, 10, 6, 10, 139.99, CURRENT_DATE + 7),
(21, 10, 8, 2, 34.99, CURRENT_DATE + 1),

-- Order 11: Customer 5
(22, 11, 13, 3, 39.99, CURRENT_DATE + 1),
(23, 11, 14, 1, 69.99, CURRENT_DATE + 1),

-- Order 12: Customer 5
(24, 12, 4, 1, 329.99, CURRENT_DATE - 2),
(25, 12, 10, 1, 119.99, CURRENT_DATE - 2),

-- Order 13: Customer 6
(26, 13, 7, 1, 59.99, CURRENT_DATE + 7),
(27, 13, 5, 4, 12.50, CURRENT_DATE + 1),

-- Order 14: Customer 6
(28, 14, 9, 2, 239.99, CURRENT_DATE + 7),
(29, 14, 15, 2, 44.99, CURRENT_DATE + 1),

-- Order 15: Customer 7
(30, 15, 11, 2, 779.99, CURRENT_DATE + 7),
(31, 15, 2, 3, 29.99, CURRENT_DATE + 1),

-- Order 16: Customer 7
(32, 16, 1, 1, 899.99, CURRENT_DATE + 1),
(33, 16, 13, 2, 39.99, CURRENT_DATE + 1),

-- Order 17: Customer 8
(34, 17, 12, 2, 419.99, CURRENT_DATE + 7),
(35, 17, 14, 1, 69.99, CURRENT_DATE + 1),

-- Order 18: Customer 8
(36, 18, 3, 2, 89.99, CURRENT_DATE + 1),
(37, 18, 8, 3, 34.99, CURRENT_DATE + 1),

-- Order 19: Customer 8 (archived)
(38, 19, 10, 1, 119.99, CURRENT_DATE - 7),
(39, 19, 5, 2, 12.50, CURRENT_DATE - 7),

-- Order 20: Customer 1
(40, 20, 6, 2, 149.99, CURRENT_DATE + 7),
(41, 20, 15, 1, 44.99, CURRENT_DATE + 1);

SELECT setval(pg_get_serial_sequence('users', 'id'), (SELECT MAX(id) FROM users));
SELECT setval(pg_get_serial_sequence('item', 'id'), (SELECT MAX(id) FROM item));
SELECT setval(pg_get_serial_sequence('eurder', 'id'), (SELECT MAX(id) FROM eurder));
SELECT setval(pg_get_serial_sequence('item_group', 'id'), (SELECT MAX(id) FROM item_group));