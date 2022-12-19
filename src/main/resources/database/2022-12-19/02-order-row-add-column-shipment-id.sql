--liquibase formatted sql
--changeset gkanclerz:13

alter table order_row MODIFY product_id bigint;
alter table order_row add shipment_id bigint;
alter table order_row add constraint fk_order_row_shipment_id foreign key (shipment_id) references shipment(id);