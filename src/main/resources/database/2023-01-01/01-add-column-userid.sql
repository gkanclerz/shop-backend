--liquibase formatted sql
--changeset gkanclerz:21
alter table `order` add column user_id bigint;

--changeset gkanclerz:22
alter table `order` add constraint fk_order_user_id foreign key (user_id) references users(id);