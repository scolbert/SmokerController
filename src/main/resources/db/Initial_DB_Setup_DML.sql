insert into TEMPERATURE_TIMING  values  (DEFAULT, 'Smoking heat level indefinitely', 'Basic Smoking');
insert into TEMPERATURE_TIMING  values  (DEFAULT, '220 degrees indefinitely', '220 Smoking');
insert into TEMPERATURE_TIMING  values  (DEFAULT, '2 hours smoke, 2 hours 220, 1 hour smoke', '2-2-1 Ribs');

insert into TEMPERATURE_TIMING_DETAIL values (DEFAULT, 1, null, 355.3889, select id from TEMPERATURE_TIMING where name = 'Basic Smoking');
insert into TEMPERATURE_TIMING_DETAIL values (DEFAULT, 1,null, 377.6111, select id from TEMPERATURE_TIMING where name = '220 Smoking');

insert into TEMPERATURE_TIMING_DETAIL values (DEFAULT, 1,120, 355.3889, select id from TEMPERATURE_TIMING where name = '2-2-1 Ribs');
insert into TEMPERATURE_TIMING_DETAIL values (DEFAULT, 2,120, 377.6111, select id from TEMPERATURE_TIMING where name = '2-2-1 Ribs');
insert into TEMPERATURE_TIMING_DETAIL values (DEFAULT, 3,60, 355.3889, select id from TEMPERATURE_TIMING where name = '2-2-1 Ribs');
insert into TEMPERATURE_TIMING_DETAIL values (DEFAULT, 4,null, 0, select id from TEMPERATURE_TIMING where name = '2-2-1 Ribs');

insert into settings values (1, 1,'CELSIUS');

insert into SMOKE_SESSION values (DEFAULT, 'best ribs ever', 'ribs', 1, sysdate,3 );