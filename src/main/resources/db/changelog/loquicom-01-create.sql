--liquibase formatted sql
--changeset loquicom:01

CREATE TABLE users
(
    us_id    SERIAL,
    login    VARCHAR(500),
    password VARCHAR(255),
    email    VARCHAR(500),
    PRIMARY KEY (us_id)
);

CREATE TABLE forecast
(
    fo_id   SERIAL,
    amount  DECIMAL,
    libelle VARCHAR(500),
    date    DATE,
    ba_id   SERIAL,
    sa_id   SERIAL,
    PRIMARY KEY (fo_id)
);

CREATE TABLE bank
(
    ba_id       SERIAL,
    name        VARCHAR(500),
    description TEXT,
    main        BOOLEAN,
    us_id       SERIAL,
    PRIMARY KEY (ba_id)
);

CREATE TABLE saving
(
    sa_id       SERIAL,
    name        VARCHAR(500),
    description TEXT,
    us_id       SERIAL,
    ba_id       SERIAL,
    PRIMARY KEY (sa_id)
);

CREATE TABLE recurrent
(
    re_id   SERIAL,
    amount  DECIMAL,
    libelle VARCHAR(500),
    freq    VARCHAR(5),
    date    DATE,
    next    DATE,
    active  BOOLEAN,
    sa_id   SERIAL,
    ba_id   SERIAL,
    PRIMARY KEY (re_id)
);

CREATE TABLE income
(
    in_id   SERIAL,
    amount  DECIMAL,
    libelle VARCHAR(500),
    date    DATE,
    checked BOOLEAN,
    sa_id   SERIAL,
    ba_id   SERIAL,
    ta_id   SERIAL,
    ca_id   SERIAL,
    PRIMARY KEY (in_id)
);

CREATE TABLE category
(
    ca_id   SERIAL,
    name    VARCHAR(500),
    income  BOOLEAN,
    expense BOOLEAN,
    PRIMARY KEY (ca_id)
);

CREATE TABLE tag
(
    ta_id   SERIAL,
    name    VARCHAR(500),
    income  BOOLEAN,
    expense BOOLEAN,
    PRIMARY KEY (ta_id)
);

CREATE TABLE expense
(
    ex_id   SERIAL,
    amount  DECIMAL,
    libelle VARCHAR(500),
    date    DATE,
    checked BOOLEAN,
    ba_id   SERIAL,
    sa_id   SERIAL,
    ta_id   SERIAL,
    ca_id   SERIAL,
    PRIMARY KEY (ex_id)
);
