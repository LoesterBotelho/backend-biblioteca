INSERT INTO role (name) VALUES ('ROLE_ADMIN');
INSERT INTO role (name) VALUES ('ROLE_USER');

INSERT INTO app_user (username, password, full_name, enabled) VALUES ('admin', '$2a$10$7q0v0Jm0qQzqYw8d1rKqUe0Q7mZ7sZfQp1f1oJb0l8QeQm2g0m8x2', 'Administrator', TRUE);
INSERT INTO app_user (username, password, full_name, enabled) VALUES ('student1', '$2a$10$N9k8v2pX9cQw3mR8tY1fQeK0aZpL7sD8vB2nH5xC9mQ1eR6tY7uI0', 'Student One', TRUE);

INSERT INTO user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO user_role (user_id, role_id) VALUES (2, 2);

INSERT INTO branch (name, location) VALUES ('Matriz', 'Campus Principal');
INSERT INTO branch (name, location) VALUES ('Filial', 'Campus Secundario');

INSERT INTO author (name, biography) VALUES ('George Orwell', 'English novelist and essayist.');
INSERT INTO author (name, biography) VALUES ('Jane Austen', 'English novelist known for her six major novels.');

INSERT INTO book (title, isbn, summary, price, available, author_id) VALUES ('1984', '9780451524935', 'Dystopian novel about surveillance.', 0, TRUE, 1);
INSERT INTO book (title, isbn, summary, price, available, author_id) VALUES ('Pride and Prejudice', '9780141439518', 'Classic novel of manners.', 5.00, TRUE, 2);

INSERT INTO inventory_item (book_id, branch_id, quantity, unusable_quantity) VALUES (1, 1, 5, 0);
INSERT INTO inventory_item (book_id, branch_id, quantity, unusable_quantity) VALUES (2, 2, 3, 0);
