--liquibase formatted sql
--changeset gkanclerz:25
alter table `order` add order_hash varchar(12);