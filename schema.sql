create database playon365 default charset='utf8';
create user playon identified by 'playon';
grant all on playon365.* to playon;
use playon365;

create table member(
    id serial, 
    name varchar(100), 
    email varchar(100) unique, 
    password varchar(200) 
);

create table post(
	id       serial,
	topic    varchar(500),
	detail   varchar(8000),
	member   bigint,
	time     timestamp
);

alter table post add photo varchar(200);