--liquibase formatted sql
--changeset gkanclerz:2

alter table product add image varchar(128) after currency;

