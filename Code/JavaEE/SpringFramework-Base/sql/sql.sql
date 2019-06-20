# JDBC模板
create table springf_test_user (
  id int(20) NOT NULL AUTO_INCREMENT,
  name varchar(50) DEFAULT NULL,
  primary key (id)
);

# 事务
create table spring_test_account(
  id int(20) NOT NULL AUTO_INCREMENT,
  name varchar(20) NOT NULL,
  money varchar(20) DEFAULT NULL,
  PRIMARY KEY (id)
);