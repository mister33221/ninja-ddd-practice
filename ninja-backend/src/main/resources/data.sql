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


-- 插入初始用户資訊
INSERT INTO users (id, username, hashed_password, last_login_time, full_name, email, phone_number, date_of_birth, address, random_salt)
VALUES (9999, 'user1', 'hashed_password1', NULL, 'Full Name 1', 'user1@example.com', '1234567890', '1990-01-01', '123 Main St', 'random_salt1');

INSERT INTO users (id, username, hashed_password, last_login_time, full_name, email, phone_number, date_of_birth, address, random_salt)
VALUES (8888, 'user2', 'hashed_password2', NULL, 'Full Name 2', 'user2@example.com', '0987654321', '1991-02-02', '456 Elm St', 'random_salt2');

INSERT INTO users (id, username, hashed_password, last_login_time, full_name, email, phone_number, date_of_birth, address, random_salt)
VALUES (7777, 'kai', '$2a$10$oddsa4nCrau7uCviwlCye.DfaS1uRrz7U8aqAqU4OmgtZX0KsKrW2', NULL, '謝凱威', 'mister332212@example.com', '0987654321', '1991-02-02', '456 Elm St', '69jjseLWTxEauLJlqD4k1Q==');


-- 插入初始產品类别資訊
INSERT INTO product_categories (id, name, description)
VALUES (1, 'Electronics', 'Electronics category');

INSERT INTO product_categories (id, name, description)
VALUES (2, 'Books', 'Books category');

-- 插入初始產品資訊
INSERT INTO product (id, name, description, price, stock_quantity, category_id, status, image_url)
VALUES (9999, 'Kunai', 'A small throwing knife used by ninjas', 10.99, 100, 1, 'PULL_ON_SHELVES', 'kunai.jpg');

INSERT INTO product (id, name, description, price, stock_quantity, category_id, status, image_url)
VALUES (8888, 'Shuriken', 'A star-shaped throwing weapon', 5.99, 200, 1, 'PULL_ON_SHELVES', 'shuriken.jpg');

INSERT INTO product (id, name, description, price, stock_quantity, category_id, status, image_url)
VALUES (7777, 'Explosive Tag', 'A paper tag that explodes after a set time', 15.99, 50, 1, 'PULL_ON_SHELVES', 'explosive_tag.jpg');

INSERT INTO product (id, name, description, price, stock_quantity, category_id, status, image_url)
VALUES (6666, 'Smoke Bomb', 'A bomb that releases smoke to obscure vision', 7.99, 75, 1, 'PULL_ON_SHELVES', 'smoke_bomb.jpg');

INSERT INTO product (id, name, description, price, stock_quantity, category_id, status, image_url)
VALUES (5555, 'Ninja Sword', 'A short sword used by ninjas', 49.99, 30, 1, 'PULL_ON_SHELVES', 'ninja_sword.jpg');

INSERT INTO product (id, name, description, price, stock_quantity, category_id, status, image_url)
VALUES (4444, 'Chakra Blade', 'A blade that can channel chakra', 99.99, 20, 1, 'PULL_ON_SHELVES', 'chakra_blade.jpg');

INSERT INTO product (id, name, description, price, stock_quantity, category_id, status, image_url)
VALUES (3333, 'Senbon', 'A needle-like weapon used for precise attacks', 2.99, 300, 1, 'PULL_ON_SHELVES', 'senbon.jpg');

INSERT INTO product (id, name, description, price, stock_quantity, category_id, status, image_url)
VALUES (2222, 'Paper Bomb Kunai', 'A kunai with an explosive tag attached', 12.99, 60, 1, 'PULL_ON_SHELVES', 'paper_bomb_kunai.jpg');

INSERT INTO product (id, name, description, price, stock_quantity, category_id, status, image_url)
VALUES (1111, 'Windmill Shuriken', 'A large shuriken that can be folded', 29.99, 40, 1, 'PULL_ON_SHELVES', 'windmill_shuriken.jpg');

INSERT INTO product (id, name, description, price, stock_quantity, category_id, status, image_url)
VALUES (99999, 'Ninja Wire', 'A thin, strong wire used for traps and binding', 3.99, 150, 1, 'PULL_ON_SHELVES', 'ninja_wire.jpg');

INSERT INTO product (id, name, description, price, stock_quantity, category_id, status, image_url)
VALUES (88888, 'Poison', 'A vial of poison for coating weapons', 19.99, 25, 1, 'PULL_ON_SHELVES', 'poison.jpg');

INSERT INTO product (id, name, description, price, stock_quantity, category_id, status, image_url)
VALUES (77777, 'Flash Bomb', 'A bomb that emits a bright flash to blind enemies', 8.99, 80, 1, 'PULL_ON_SHELVES', 'flash_bomb.jpg');

INSERT INTO product (id, name, description, price, stock_quantity, category_id, status, image_url)
VALUES (66666, 'Ninja Scroll', 'A scroll containing ninja techniques', 24.99, 10, 1, 'PULL_ON_SHELVES', 'ninja_scroll.jpg');

INSERT INTO product (id, name, description, price, stock_quantity, category_id, status, image_url)
VALUES (55555, 'Giant Fan', 'A large fan used for wind-based attacks', 59.99, 5, 1, 'PULL_ON_SHELVES', 'giant_fan.jpg');

INSERT INTO product (id, name, description, price, stock_quantity, category_id, status, image_url)
VALUES (44444, 'Ninja Armor', 'Protective armor worn by ninjas', 149.99, 15, 1, 'PULL_ON_SHELVES', 'ninja_armor.jpg');
-- 插入初始购物车資訊
INSERT INTO shopping_carts (id, user_id)
VALUES (9999, 9999);

INSERT INTO shopping_carts (id, user_id)
VALUES (8888, 8888);

-- 插入初始购物车项目資訊
INSERT INTO cart_items (id, product_id, cart_id, quantity, price)
VALUES (9999, 9999, 9999, 2, 49.99);

INSERT INTO cart_items (id, product_id, cart_id, quantity, price)
VALUES (8888, 8888, 8888, 1, 999.99);

-- 插入初始订单資訊
INSERT INTO orders (id, user_id, total_amount, status, payment_time)
VALUES (9999, 1, 99.98, 'PENDING', CURRENT_TIMESTAMP());

INSERT INTO orders (id, user_id, total_amount, status, payment_time)
VALUES (8888, 2, 999.99, 'PAID', CURRENT_TIMESTAMP());

-- 插入初始订单项目資訊
INSERT INTO order_items (id, product_id, quantity, price, order_id)
VALUES (9999, 9999, 2, 49.99, 9999);

INSERT INTO order_items (id, product_id, quantity, price, order_id)
VALUES (8888, 8888, 1, 999.99, 8888);

-- 插入初始库存資訊
INSERT INTO inventory_items (product_id, quantity, reorder_threshold)
VALUES (9999, 50, 10);

INSERT INTO inventory_items (product_id, quantity, reorder_threshold)
VALUES (8888, 10, 5);
