--liquibase formatted sql
--changeSet Chima:UpdateProduct

Alter TABLE product
    DROP COLUMN rate;

Alter TABLE product
    ADD sell_rate float(53) NOT NULL,
        buy_rate float(53) NOT NULL;