--liquibase formatted sql
--changeSet Chima:CreateTransactionFulfillment
CREATE TABLE transaction_fulfillment
(
    id                 bigint IDENTITY (1, 1) NOT NULL,
    request_time_stamp datetime,
    fulfillment_time   datetime,
    status             varchar(255),
    CONSTRAINT pk_transactionfulfillment PRIMARY KEY (id)
)