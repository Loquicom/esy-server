--liquibase formatted sql
--changeset loquicom:01

CREATE TABLE users
(
    us_id    UUID,
    login    VARCHAR(500),
    password VARCHAR(255),
    email    VARCHAR(500),
    PRIMARY KEY (us_id)
);

CREATE TABLE forecast
(
    fo_id   UUID,
    amount  DECIMAL,
    libelle VARCHAR(500),
    date    DATE,
    ba_id   UUID,
    sa_id   UUID,
    PRIMARY KEY (fo_id)
);

CREATE TABLE bank
(
    ba_id       UUID,
    name        VARCHAR(500),
    description TEXT,
    main        BOOLEAN,
    us_id       UUID NOT NULL,
    PRIMARY KEY (ba_id)
);

CREATE TABLE saving
(
    sa_id       UUID,
    name        VARCHAR(500),
    description TEXT,
    us_id       UUID NOT NULL,
    ba_id       UUID,
    PRIMARY KEY (sa_id)
);

CREATE TABLE recurrent
(
    re_id   UUID,
    amount  DECIMAL,
    libelle VARCHAR(500),
    freq    VARCHAR(5),
    date    DATE,
    next    DATE,
    active  BOOLEAN,
    sa_id   UUID,
    ba_id   UUID,
    PRIMARY KEY (re_id)
);

CREATE TABLE income
(
    in_id   UUID,
    amount  DECIMAL,
    libelle VARCHAR(500),
    date    DATE,
    checked BOOLEAN,
    sa_id   UUID,
    ba_id   UUID,
    ta_id   UUID,
    ca_id   UUID,
    PRIMARY KEY (in_id)
);

CREATE TABLE category
(
    ca_id   UUID,
    name    VARCHAR(500),
    income  BOOLEAN,
    expense BOOLEAN,
    PRIMARY KEY (ca_id)
);

CREATE TABLE tag
(
    ta_id   UUID,
    name    VARCHAR(500),
    income  BOOLEAN,
    expense BOOLEAN,
    PRIMARY KEY (ta_id)
);

CREATE TABLE expense
(
    ex_id   UUID,
    amount  DECIMAL,
    libelle VARCHAR(500),
    date    DATE,
    checked BOOLEAN,
    ba_id   UUID,
    sa_id   UUID,
    ta_id   UUID,
    ca_id   UUID,
    PRIMARY KEY (ex_id)
);
