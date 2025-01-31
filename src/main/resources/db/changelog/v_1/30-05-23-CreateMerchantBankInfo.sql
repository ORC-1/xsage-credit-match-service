--liquibase formatted sql
--changeSet Chima:CreateMerchantBankInfo

CREATE TABLE merchant_bank_info
(
    id                                 bigint IDENTITY (1, 1) NOT NULL,
    merchant_id_id                     bigint,
    bank_name                          varchar(255)           NOT NULL,
    bank_account_number                varchar(255)           NOT NULL,
    bank_account_currency_denomination varchar(255)           NOT NULL,
    CONSTRAINT pk_merchantbankinfo PRIMARY KEY (id)
)


ALTER TABLE merchant_bank_info
    ADD CONSTRAINT FK_MERCHANTBANKINFO_ON_MERCHANT_ID FOREIGN KEY (merchant_id_id) REFERENCES merchant (id)
