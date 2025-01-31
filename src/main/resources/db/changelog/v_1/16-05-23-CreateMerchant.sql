--liquibase formatted sql
--changeSet Chima:CreateMerchant
CREATE TABLE merchant
(
    id                  bigint IDENTITY (1, 1) NOT NULL,
    user_id             varchar(255)           NOT NULL,
    business_name       varchar(255)           NOT NULL,
    location            varchar(255)           NOT NULL,
    verified            bit,
    verified_time_stamp datetime,
    latitude            float(53),
    longitude           float(53),
    CONSTRAINT pk_merchant PRIMARY KEY (id)
)