--liquibase formatted sql
--changeSet Chima:AlterMerchantBankInfo
ALTER TABLE merchant_bank_info ADD COLUMN balance DOUBLE DEFAULT 0.0 NOT NULL;
