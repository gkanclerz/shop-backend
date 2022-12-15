--liquibase formatted sql
--changeset gkanclerz:9

alter table review add column moderated boolean default false after content;