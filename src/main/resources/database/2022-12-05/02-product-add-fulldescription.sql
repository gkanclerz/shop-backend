--liquibase formatted sql
--changeset gkanclerz:4

alter table product add full_description text after description;