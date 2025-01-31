--liquibase formatted sql
--changeSet Chima:UpdateMerchant

Alter TABLE merchant
    ADD penalty varchar(255) NOT NULL,
        customer_repeat_rate float(53);