--liquibase formatted sql

--changeSet Alexander Zubkov:data_2_data splitStatements:true

insert into data.data
  (code, name, description, date, username)
values
  ('one', 'One data', 'Description for one data', current_timestamp, 'admin'),
  ('two', 'Two data', 'Description for two data', current_timestamp, 'admin'),
  ('three', 'Three data', 'Description for three data', current_timestamp, 'admin'),
  ('four', 'Four data', 'Description for four data', current_timestamp, 'admin'),
  ('five', 'Five data', 'Description for five data', current_timestamp, 'admin'),
  ('six', 'Six data', 'Description for six data', current_timestamp, 'admin'),
  ('seven', 'Seven data', 'Description for seven data', current_timestamp, 'admin'),
  ('eight', 'Eight data', 'Description for eight data', current_timestamp, 'admin'),
  ('nine', 'Nine data', 'Description for nine data', current_timestamp, 'admin'),
  ('ten', 'Ten data', 'Description for ten data', current_timestamp, 'admin');
