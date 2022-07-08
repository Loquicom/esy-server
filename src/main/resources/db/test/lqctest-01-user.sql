--liquibase formatted sql
--changeset lqctest:01

INSERT INTO users(us_id, login, password, email, role)
VALUES (UUID(), 'admin', '$2a$10$cpN55juucePp7ATLrA2.QuPm3oeotkmVAqS2xtDhYhgdLBufQctCi', 'no-contact@email.com', 'ADMIN');

INSERT INTO users(us_id, login, password, email, role)
VALUES ('122b04c8-7235-4ec4-b10c-9ee2ad2617be', 'test-login', '$2a$12$4dEDgh0zJQ62x.QZmZ9.m.rFS5TWFiJAlU5Mm4ZuwXCliHgaFlw02',
        'test@email.com', 'USER');
