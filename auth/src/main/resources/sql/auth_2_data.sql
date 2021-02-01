--liquibase formatted sql

--changeSet Alexander Zubkov:auth_2_data splitStatements:true

insert into auth.users
(username, password, authorities)
values
  ('admin', '$2a$10$I3eTy.fXPqxsR5p0hXEEkudBJlANaVH2xYlbf8MWzsYjjMqXRAYd.', 'ADMIN'),
  ('user', '$2a$10$dWUX63GB0MW4mixJFhA8Due/2BJ2RwUUl2IG6giZBD27D/3tqCGlC', 'USER'),
  ('audit', '$2a$10$tIRIs7NxepqlwAvAsoIrpe5DIA/QkUgfuU8CsnOP.Jk6goWPqJCjW', 'AUDIT');