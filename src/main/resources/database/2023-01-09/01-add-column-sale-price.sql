--liquibase formatted sql
--changeset gkanclerz:26

alter table product add sale_price decimal(9,2);