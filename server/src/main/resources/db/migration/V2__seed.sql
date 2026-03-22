INSERT INTO categories (id, name, parent_id, sort_order) VALUES
    (1, 'Овощи и фрукты', NULL, 1),
    (2, 'Молочные продукты', NULL, 2),
    (3, 'Бакалея', NULL, 3);

INSERT INTO products (category_id, name, description, price, unit, image_url, stock, brand) VALUES
    (1, 'Помидоры', 'Свежие помидоры', 120.00, 'кг', NULL, 50, 'Ферма+'),
    (1, 'Огурцы', 'Огурцы тепличные', 95.50, 'кг', NULL, 40, 'Ферма+'),
    (2, 'Молоко 3.2%', 'Пастеризованное', 89.90, 'л', NULL, 100, 'Простоквашино'),
    (2, 'Творог 5%', 'Зернистый', 145.00, '400г', NULL, 30, 'Домик в деревне'),
    (3, 'Гречка', 'Крупа ядрица', 95.00, 'кг', NULL, 200, 'Увелка'),
    (3, 'Макароны', 'Из твердых сортов', 72.00, '400г', NULL, 150, 'Barilla');

SELECT setval(pg_get_serial_sequence('categories', 'id'), (SELECT MAX(id) FROM categories));
SELECT setval(pg_get_serial_sequence('products', 'id'), (SELECT MAX(id) FROM products));
