CREATE DATABASE ESY;
\c ESY;

CREATE TABLE HISTORY_OBJECTIVE
(
    ho_id     SERIAL,
    success   BOOLEAN,
    date      DATE,
    parameter TEXT,
    ob_id     SERIAL,
    PRIMARY KEY (ho_id)
);

CREATE TABLE OBJECTIVE
(
    ob_id     SERIAL,
    type      VARCHAR(10),
    freq      VARCHAR(5),
    date      DATE,
    next      DATE,
    parameter TEXT,
    us_id     SERIAL,
    PRIMARY KEY (ob_id)
);

CREATE TABLE "USER"
(
    us_id    SERIAL,
    login    VARCHAR(500),
    password VARCHAR(255),
    email    VARCHAR(500),
    PRIMARY KEY (us_id)
);

CREATE TABLE FORECAST
(
    fo_id   SERIAL,
    amount  DECIMAL,
    libelle VARCHAR(500),
    date    DATE,
    ba_id   SERIAL,
    sa_id   SERIAL,
    PRIMARY KEY (fo_id)
);

CREATE TABLE BANK
(
    ba_id       SERIAL,
    name        VARCHAR(500),
    description TEXT,
    main        BOOLEAN,
    us_id       SERIAL,
    PRIMARY KEY (ba_id)
);

CREATE TABLE SAVING
(
    sa_id       SERIAL,
    name        VARCHAR(500),
    description TEXT,
    us_id       SERIAL,
    ba_id       SERIAL,
    PRIMARY KEY (sa_id)
);

CREATE TABLE RECURRENT
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

CREATE TABLE INCOME
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

CREATE TABLE CATEGORY
(
    ca_id SERIAL,
    name  VARCHAR(500),
    PRIMARY KEY (ca_id)
);

CREATE TABLE TAG
(
    ta_id SERIAL,
    name  VARCHAR(500),
    PRIMARY KEY (ta_id)
);

CREATE TABLE EXPENSE
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

ALTER TABLE HISTORY_OBJECTIVE
    ADD FOREIGN KEY (ob_id) REFERENCES OBJECTIVE (ob_id);
ALTER TABLE OBJECTIVE
    ADD FOREIGN KEY (us_id) REFERENCES "USER" (us_id);
ALTER TABLE FORECAST
    ADD FOREIGN KEY (sa_id) REFERENCES SAVING (sa_id);
ALTER TABLE FORECAST
    ADD FOREIGN KEY (ba_id) REFERENCES BANK (ba_id);
ALTER TABLE BANK
    ADD FOREIGN KEY (us_id) REFERENCES "USER" (us_id);
ALTER TABLE SAVING
    ADD FOREIGN KEY (ba_id) REFERENCES BANK (ba_id);
ALTER TABLE SAVING
    ADD FOREIGN KEY (us_id) REFERENCES "USER" (us_id);
ALTER TABLE RECURRENT
    ADD FOREIGN KEY (ba_id) REFERENCES BANK (ba_id);
ALTER TABLE RECURRENT
    ADD FOREIGN KEY (sa_id) REFERENCES SAVING (sa_id);
ALTER TABLE INCOME
    ADD FOREIGN KEY (ca_id) REFERENCES CATEGORY (ca_id);
ALTER TABLE INCOME
    ADD FOREIGN KEY (ta_id) REFERENCES TAG (ta_id);
ALTER TABLE INCOME
    ADD FOREIGN KEY (ba_id) REFERENCES BANK (ba_id);
ALTER TABLE INCOME
    ADD FOREIGN KEY (sa_id) REFERENCES SAVING (sa_id);
ALTER TABLE EXPENSE
    ADD FOREIGN KEY (ca_id) REFERENCES CATEGORY (ca_id);
ALTER TABLE EXPENSE
    ADD FOREIGN KEY (ta_id) REFERENCES TAG (ta_id);
ALTER TABLE EXPENSE
    ADD FOREIGN KEY (sa_id) REFERENCES SAVING (sa_id);
ALTER TABLE EXPENSE
    ADD FOREIGN KEY (ba_id) REFERENCES BANK (ba_id);
