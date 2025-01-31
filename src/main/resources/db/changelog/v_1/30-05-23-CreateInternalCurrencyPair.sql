--liquibase formatted sql
--changeSet Chima:
CREATE TABLE internal_currency_pair_price
(
    id                  bigint IDENTITY (1, 1) NOT NULL,
    valid_currency_pair varchar(255),
    buy_rate            float(53)              NOT NULL,
    sell_rate           float(53)              NOT NULL,
    CONSTRAINT pk_internalcurrencypairprice PRIMARY KEY (id)
)