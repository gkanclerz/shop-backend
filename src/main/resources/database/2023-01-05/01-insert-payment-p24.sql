--liquibase formatted sql
--changeset gkanclerz:24
update payment set default_payment=false;
insert into payment (name, type, default_payment, note)
values ('Płatność online Przelewy 24', 'P24_ONLINE', true, '');