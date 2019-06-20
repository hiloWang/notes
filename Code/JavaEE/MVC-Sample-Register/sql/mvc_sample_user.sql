create table user(
  username varchar(100) primary key,
  password varchar(100) not null,
  email varchar(100) not null,
  birthday date not null
);