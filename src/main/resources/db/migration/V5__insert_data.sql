INSERT INTO t_permission (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO t_permission (id, name) VALUES (2, 'ROLE_ADMIN');

INSERT INTO t_user_account (id, name, password)
VALUES (1, 'admin', '$2a$10$8.UnVuG9HHgffUDAlk8qfOpNa.hPAxSTKwantmDGB.gQv.9Jbpe.6');

INSERT INTO t_user_account (id, name, password)
VALUES (2, 'rafi', '$2a$10$8.UnVuG9HHgffUDAlk8qfOpNa.hPAxSTKwantmDGB.gQv.9Jbpe.6');

INSERT INTO t_user_account (id, name, password)
VALUES (3, 'nurken', '$2a$10$8.UnVuG9HHgffUDAlk8qfOpNa.hPAxSTKwantmDGB.gQv.9Jbpe.6');


INSERT INTO user_permissions (user_id, permission_id) VALUES (1, 2);
-- INSERT INTO user_permissions (user_id, permission_id) VALUES (1, 1);

INSERT INTO user_permissions (user_id, permission_id) VALUES (2, 1);

INSERT INTO user_permissions (user_id, permission_id) VALUES (3, 1);


INSERT INTO t_category (id, name, color) VALUES (1, 'IT', '#0000FF');       -- Синий
INSERT INTO t_category (id, name, color) VALUES (2, 'Спорт', '#FF0000');    -- Красный
INSERT INTO t_category (id, name, color) VALUES (3, 'Новости', '#00FF00');  -- Зеленый


INSERT INTO t_post (id, title, text, user_id)
VALUES (1, 'Правила платформы', 'Всем привет, ведите себя хорошо!', 1);

INSERT INTO t_post (id, title, text, user_id)
VALUES (2, 'Как я учил Spring', 'Было сложно, я не справился.', 2);

INSERT INTO t_post (id, title, text, user_id)
VALUES (3, 'Вопрос про Docker', 'Почему контейнер не собирается?', 2);

INSERT INTO t_post (id, title, text, user_id)
VALUES (4, 'Пост про Рафи', 'ХАПХАПХАПХАХПАХПАХП', 3);

INSERT INTO t_post (id, title, text, user_id)
VALUES (5, 'Булим тех кто не знает Docker', 'пишите + чтобы присоединиться в группу', 3);


INSERT INTO post_categories (post_id, category_id) VALUES (1, 3);

INSERT INTO post_categories (post_id, category_id) VALUES (2, 1);

INSERT INTO post_categories (post_id, category_id) VALUES (3, 1);

INSERT INTO post_categories (post_id, category_id) VALUES (4, 3);

INSERT INTO post_categories (post_id, category_id) VALUES (5, 3);


-- postgre начнет создавать ID с последнего, не с 1

SELECT setval(pg_get_serial_sequence('t_user_account', 'id'), coalesce(max(id)+1, 1), false) FROM t_user_account;
SELECT setval(pg_get_serial_sequence('t_permission', 'id'), coalesce(max(id)+1, 1), false) FROM t_permission;
SELECT setval(pg_get_serial_sequence('t_category', 'id'), coalesce(max(id)+1, 1), false) FROM t_category;
SELECT setval(pg_get_serial_sequence('t_post', 'id'), coalesce(max(id)+1, 1), false) FROM t_post;