create database if not exists weatherinfo;
	
use weatherinfo;

drop table if exists weather;

create table weather(
		city varchar(100) not null,
		temperature integer not null
);

insert into weather (city,temperature) values ('Austin', 48);
insert into weather (city,temperature) values ('Boton Rouge', 21);
insert into weather (city,temperature) values ('Jsckson', 2);
insert into weather (city,temperature) values ('Montgomery', 32);
insert into weather (city,temperature) values ('Sacramento', 22);
insert into weather (city,temperature) values ('Santa', 1);
insert into weather (city,temperature) values ('Tallahassee', 23);
insert into weather (city,temperature) values ('Shenzhen', 17);