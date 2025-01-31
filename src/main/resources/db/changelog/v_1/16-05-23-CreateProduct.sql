--liquibase formatted sql
--changeSet Chima:CreateProduct
CREATE TABLE product
(
    id                     bigint IDENTITY (1, 1) NOT NULL,
    merchant_id_id         bigint,
    trading_pair           varchar(255),
    merchant_internal_rate float(53)              NOT NULL,
    rate                   float(53)              NOT NULL,
    CONSTRAINT pk_product PRIMARY KEY (id)
)

ALTER TABLE product
    ADD CONSTRAINT FK_PRODUCT_ON_MERCHANT_ID FOREIGN KEY (merchant_id_id) REFERENCES merchant (id)
