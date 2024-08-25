-- -- 插入產品分類
-- INSERT INTO product_categories (name, description) VALUES ('Electronics', 'Electronic devices and accessories');
-- INSERT INTO product_categories (name, description) VALUES ('Books', 'Physical and digital books');
--
-- -- 插入產品
-- INSERT INTO product(name, description, price, stock_quantity, category_id, status, image_url)
-- VALUES ('Java Programming Book', 'Comprehensive guide to Java programming', 49.99, 50, 2, 'PULL_ON_SHELVES', 'java_book_main.jpg');
--
--
-- -- 插入用戶
-- INSERT INTO users(username, full_name, email, phone_number, date_of_birth, address, hashed_password, last_login_time)
-- VALUES ('john_doe', 'John Doe', 'john@example.com', '1234567890', '1990-01-01', '123 Main St, City, Country', 'hashedPassword123', CURRENT_TIMESTAMP());


-- 插入初始用户数据
INSERT INTO users (id, username, hashed_password, last_login_time, full_name, email, phone_number, date_of_birth, address)
VALUES (9999, 'user1', 'hashed_password1', NULL, 'Full Name 1', 'user1@example.com', '1234567890', '1990-01-01', '123 Main St');

INSERT INTO users (id, username, hashed_password, last_login_time, full_name, email, phone_number, date_of_birth, address)
VALUES (8888, 'user2', 'hashed_password2', NULL, 'Full Name 2', 'user2@example.com', '0987654321', '1991-02-02', '456 Elm St');

-- 插入初始产品类别数据
INSERT INTO product_categories (id, name, description)
VALUES (1, 'Electronics', 'Electronics category');

INSERT INTO product_categories (id, name, description)
VALUES (2, 'Books', 'Books category');

-- 插入初始产品数据
INSERT INTO product (id, name, description, price, stock_quantity, category_id, status, image_url)
VALUES (1, 'Java Programming Book', 'Comprehensive guide to Java programming', 49.99, 50, 2, 'PULL_ON_SHELVES', 'java_book_main.jpg');

INSERT INTO product (id, name, description, price, stock_quantity, category_id, status, image_url)
VALUES (2, 'Laptop', 'High-performance laptop', 999.99, 10, 1, 'PULL_ON_SHELVES', 'laptop_main.jpg');

-- 插入初始购物车数据
INSERT INTO shopping_carts (id, user_id)
VALUES (1, 1);

INSERT INTO shopping_carts (id, user_id)
VALUES (2, 2);

-- 插入初始购物车项目数据
INSERT INTO cart_items (id, product_id, cart_id, quantity, price)
VALUES (1, 1, 1, 2, 49.99);

INSERT INTO cart_items (id, product_id, cart_id, quantity, price)
VALUES (2, 2, 2, 1, 999.99);

-- 插入初始订单数据
INSERT INTO orders (id, user_id, total_amount, status, payment_time)
VALUES (1, 1, 99.98, 'PENDING', CURRENT_TIMESTAMP());

INSERT INTO orders (id, user_id, total_amount, status, payment_time)
VALUES (2, 2, 999.99, 'PAID', CURRENT_TIMESTAMP());

-- 插入初始订单项目数据
INSERT INTO order_items (id, product_id, quantity, price, order_id)
VALUES (1, 1, 2, 49.99, 1);

INSERT INTO order_items (id, product_id, quantity, price, order_id)
VALUES (2, 2, 1, 999.99, 2);

-- 插入初始库存数据
INSERT INTO inventory_items (product_id, quantity, reorder_threshold)
VALUES (1, 50, 10);

INSERT INTO inventory_items (product_id, quantity, reorder_threshold)
VALUES (2, 10, 5);
