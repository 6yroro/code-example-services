--liquibase formatted sql

--changeSet Alexander Zubkov:1_data_init splitStatements:true

drop schema if exists data cascade;

create schema data;

create table data.data
(
  id          bigserial,
  code        character varying(10),
  name        character varying(100),
  description character varying(200),
  date        timestamp,
  username    character varying(100)
);