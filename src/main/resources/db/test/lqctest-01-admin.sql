--liquibase formatted sql
--changeset lqctest:01

INSERT INTO users(us_id, login, password, email, role)
VALUES (UUID(), 'admin', '$2a$10$cpN55juucePp7ATLrA2.QuPm3oeotkmVAqS2xtDhYhgdLBufQctCi', 'no-contact@email.com', 'ADMIN');
