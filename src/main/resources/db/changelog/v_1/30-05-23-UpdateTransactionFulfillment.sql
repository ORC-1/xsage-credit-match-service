--liquibase formatted sql
--changeSet Chima:UpdateTransactionFulfillment

Alter TABLE transaction_fulfillment
    ADD
        merchant_id bigint;