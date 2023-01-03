--liquibase formatted sql
--changeset gkanclerz:23

create table password_reminder(
    id bigint not null auto_increment PRIMARY KEY,
    username varchar(50) not null unique,
    place_date datetime not null,
    link varchar(100) not null
)