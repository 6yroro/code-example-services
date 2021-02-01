--liquibase formatted sql

--changeSet Alexander Zubkov:audit_1_init splitStatements:true

drop schema if exists audit cascade;

create schema audit;

create table audit.records
(
  id        bigserial,
  username  character varying(100),
  action    character varying(100),
  params    character varying(100),
  result    character varying(100),
  date      timestamp
);