--liquibase formatted sql

--changeSet Alexander Zubkov:auth_1_init splitStatements:true

drop schema if exists auth cascade;

create schema auth;

create table auth.users
(
  id          bigserial,
  username    character varying(100),
  password    character varying(200),
  authorities character varying(100)
);