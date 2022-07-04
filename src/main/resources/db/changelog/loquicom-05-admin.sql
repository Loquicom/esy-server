--liquibase formatted sql
--changeset loquicom:05

INSERT INTO users(us_id, login, password, email, role)
VALUES (gen_random_uuid(), 'admin', '$2a$10$cpN55juucePp7ATLrA2.QuPm3oeotkmVAqS2xtDhYhgdLBufQctCi', 'no-contact@email.com', 'ADMIN');
