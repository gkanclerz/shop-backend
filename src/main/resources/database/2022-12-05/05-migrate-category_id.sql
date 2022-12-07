--liquibase formatted sql
--changeset gkanclerz:7
insert into category (id, name, description, slug) values(1, 'Inne', '', 'inne');
update product set category_id=1;
alter table product MODIFY category_id bigint not null;