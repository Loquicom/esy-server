--liquibase formatted sql
--changeset loquicom:04

INSERT INTO type_objective(code, libelle, description, account)
VALUES ('spend', 'Dépenser plus que', 'Dépenser au moins une certain montant sur la période', 'acct,save,catg,tag');

INSERT INTO type_objective(code, libelle, description, account)
VALUES ('nospend', 'Ne pas dépenser plus que', 'Ne pas dépenser plus d''un certain montant sur la période', 'aact,save,catg,tag');

INSERT INTO type_objective(code, libelle, description, account)
VALUES ('earn', 'Gagner plus que', 'Gagner au minimum un certaine montant sur la période', 'acct,save,catg,tag');

INSERT INTO type_objective(code, libelle, description, account)
VALUES ('goal', 'Avoir un montant', 'À la fin de la période le solde doit être supérieur au montant', 'acct,save,catg,tag');

INSERT INTO type_objective(code, libelle, description, account)
VALUES ('goal+', 'Avoir augmenté le solde', 'À la fin de la période le solde doit avoir augmenté d''un montant minimum',
        'acct,save,catg,tag');

INSERT INTO type_objective(code, libelle, description, account)
VALUES ('goal-', 'Avoir diminué le solde', 'À la fin de la période le solde doit avoir diminué d''un montant minimum',
        'acct,save,catg,tag');
