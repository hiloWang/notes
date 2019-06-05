###################################
#  test for aop
###################################

CREATE TABLE aop_account (
  id    INT PRIMARY KEY AUTO_INCREMENT,
  name  VARCHAR(40),
  money FLOAT
)
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

INSERT INTO account (name, money) VALUES ('aaa', 1000);
INSERT INTO account (name, money) VALUES ('bbb', 1000);
INSERT INTO account (name, money) VALUES ('ccc', 1000);