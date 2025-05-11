-- Only insert data if tables are empty

-- Insert authors if none exist
INSERT INTO authors (name, biography)
SELECT 'George Orwell', 'English novelist, essayist, journalist and critic.'
WHERE NOT EXISTS (SELECT 1 FROM authors);

INSERT INTO authors (name, biography)
SELECT 'J.K. Rowling', 'British author best known for the Harry Potter series.'
WHERE NOT EXISTS (SELECT 1 FROM authors WHERE name = 'J.K. Rowling');

-- Insert books if none exist
INSERT INTO books (title, isbn, price, stock_quantity, description, published_date)
SELECT '1984', '978-0451524935', 7.99, 100, 'A dystopian social science fiction novel', '1949-06-08'
WHERE NOT EXISTS (SELECT 1 FROM books);

INSERT INTO books (title, isbn, price, stock_quantity, description, published_date)
SELECT 'Animal Farm', '978-0451526342', 6.99, 80, 'A satirical allegorical novella', '1945-08-17'
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Animal Farm');

INSERT INTO books (title, isbn, price, stock_quantity, description, published_date)
SELECT 'Harry Potter and the Philosopher''s Stone', '978-0747532743', 12.99, 50, 'First novel in the Harry Potter series', '1997-06-26'
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title LIKE 'Harry Potter%');

-- Create book-author relationships if none exist
INSERT INTO book_authors (book_id, author_id)
SELECT b.id, a.id 
FROM books b, authors a 
WHERE b.title = '1984' AND a.name = 'George Orwell'
AND NOT EXISTS (SELECT 1 FROM book_authors);

INSERT INTO book_authors (book_id, author_id)
SELECT b.id, a.id 
FROM books b, authors a 
WHERE b.title = 'Animal Farm' AND a.name = 'George Orwell'
AND NOT EXISTS (SELECT 1 FROM book_authors WHERE book_id = b.id AND author_id = a.id);

INSERT INTO book_authors (book_id, author_id)
SELECT b.id, a.id 
FROM books b, authors a 
WHERE b.title LIKE 'Harry Potter%' AND a.name = 'J.K. Rowling'
AND NOT EXISTS (SELECT 1 FROM book_authors WHERE book_id = b.id AND author_id = a.id);

-- Insert admin user if none exists
INSERT INTO users (username, password, email, role)
SELECT 'admin', '$2a$10$TZVNg.xNiT7BS.jTK5PfoeIqESu2dQgbkZmMBCpaDE4GEGTWg798O', 'admin@bookstore.com', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users);

-- Insert customer user if none exists
INSERT INTO users (username, password, email, role)
SELECT 'customer', '$2a$10$TZVNg.xNiT7BS.jTK5PfoeIqESu2dQgbkZmMBCpaDE4GEGTWg798O', 'customer@bookstore.com', 'CUSTOMER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'customer');

-- Create shopping carts for users if none exist
INSERT INTO shopping_carts (user_id)
SELECT u.id FROM users u 
WHERE u.username = 'admin' 
AND NOT EXISTS (SELECT 1 FROM shopping_carts WHERE user_id = u.id);

INSERT INTO shopping_carts (user_id)
SELECT u.id FROM users u 
WHERE u.username = 'customer' 
AND NOT EXISTS (SELECT 1 FROM shopping_carts WHERE user_id = u.id);

-- Insert cart items for customer 
INSERT INTO cart_items (cart_id, book_id, quantity)
SELECT sc.id, b.id, 2
FROM shopping_carts sc
JOIN users u ON sc.user_id = u.id
JOIN books b ON b.title = '1984'
WHERE u.username = 'customer'
AND NOT EXISTS (
    SELECT 1 FROM cart_items ci WHERE ci.cart_id = sc.id AND ci.book_id = b.id
);

INSERT INTO cart_items (cart_id, book_id, quantity)
SELECT sc.id, b.id, 1
FROM shopping_carts sc
JOIN users u ON sc.user_id = u.id
JOIN books b ON b.title = 'Harry Potter and the Philosopher''s Stone'
WHERE u.username = 'customer'
AND NOT EXISTS (
    SELECT 1 FROM cart_items ci WHERE ci.cart_id = sc.id AND ci.book_id = b.id
);

-- Insert an order for customer
INSERT INTO orders (user_id, total_price, status)
SELECT u.id, 33.97, 'PROCESSING'
FROM users u
WHERE u.username = 'customer'
AND NOT EXISTS (SELECT 1 FROM orders WHERE user_id = u.id);

-- Insert order items 
INSERT INTO order_items (order_id, book_id, quantity, unit_price)
SELECT o.id, b.id, 2, 7.99
FROM orders o
JOIN users u ON o.user_id = u.id AND u.username = 'customer'
JOIN books b ON b.title = '1984'
WHERE NOT EXISTS (
    SELECT 1 FROM order_items oi WHERE oi.order_id = o.id AND oi.book_id = b.id
);

INSERT INTO order_items (order_id, book_id, quantity, unit_price)
SELECT o.id, b.id, 1, 12.99
FROM orders o
JOIN users u ON o.user_id = u.id AND u.username = 'customer'
JOIN books b ON b.title = 'Harry Potter and the Philosopher''s Stone'
WHERE NOT EXISTS (
    SELECT 1 FROM order_items oi WHERE oi.order_id = o.id AND oi.book_id = b.id
);

-- Insert cart items for admin
INSERT INTO cart_items (cart_id, book_id, quantity)
SELECT sc.id, b.id, 1
FROM shopping_carts sc
JOIN users u ON sc.user_id = u.id
JOIN books b ON b.title = 'Animal Farm'
WHERE u.username = 'admin'
AND NOT EXISTS (
    SELECT 1 FROM cart_items ci WHERE ci.cart_id = sc.id AND ci.book_id = b.id
);

INSERT INTO cart_items (cart_id, book_id, quantity)
SELECT sc.id, b.id, 3
FROM shopping_carts sc
JOIN users u ON sc.user_id = u.id
JOIN books b ON b.title = '1984'
WHERE u.username = 'admin'
AND NOT EXISTS (
    SELECT 1 FROM cart_items ci WHERE ci.cart_id = sc.id AND ci.book_id = b.id
);

-- Insert an order for admin
INSERT INTO orders (user_id, total_price, status)
SELECT u.id, 30.96, 'PENDING'
FROM users u
WHERE u.username = 'admin'
AND NOT EXISTS (SELECT 1 FROM orders WHERE user_id = u.id);

-- Insert order items for admin's order
INSERT INTO order_items (order_id, book_id, quantity, unit_price)
SELECT o.id, b.id, 1, 6.99
FROM orders o
JOIN users u ON o.user_id = u.id AND u.username = 'admin'
JOIN books b ON b.title = 'Animal Farm'
WHERE NOT EXISTS (
    SELECT 1 FROM order_items oi WHERE oi.order_id = o.id AND oi.book_id = b.id
);

INSERT INTO order_items (order_id, book_id, quantity, unit_price)
SELECT o.id, b.id, 3, 7.99
FROM orders o
JOIN users u ON o.user_id = u.id AND u.username = 'admin'
JOIN books b ON b.title = '1984'
WHERE NOT EXISTS (
    SELECT 1 FROM order_items oi WHERE oi.order_id = o.id AND oi.book_id = b.id
);
