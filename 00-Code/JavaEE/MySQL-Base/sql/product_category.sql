###################################
#  一对多关系
###################################

# 1.分类表
CREATE TABLE category (
  cid   VARCHAR(32) PRIMARY KEY,
  cname VARCHAR(100)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# 2.商品表
CREATE TABLE product (
  pid         VARCHAR(32) PRIMARY KEY,
  pname       VARCHAR(40),
  price       DOUBLE,
  category_id VARCHAR(32)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# 3.设置外键约束
ALTER TABLE product
  ADD FOREIGN KEY (category_id) REFERENCES category (cid);

#命名方式：ALTER TABLE product ADD CONSTRAINT product_fk FOREIGN KEY (category_id) REFERENCES category (cid);

# 家电
INSERT INTO category (cid, cname) VALUES ('c001', 'home appliance');
# 服装
INSERT INTO category (cid, cname) VALUES ('c002', 'costume');
# 化妆品
INSERT INTO category (cid, cname) VALUES ('c003', 'cosmetics');

INSERT INTO product (pid, pname, price, category_id) VALUES ('p001', 'Lenovo', '5000', 'c001');
INSERT INTO product (pid, pname, price, category_id) VALUES ('p002', 'Haier', '5000', 'c001');
INSERT INTO product (pid, pname, price, category_id) VALUES ('p003', 'Thor', '5000', 'c001');

INSERT INTO product (pid, pname, price, category_id) VALUES ('p004', 'JACK JONES', '800', 'c002');
INSERT INTO product (pid, pname, price, category_id) VALUES ('p005', 'Jeanswest', '200', 'c002');
INSERT INTO product (pid, pname, price, category_id) VALUES ('p006', 'playboy', '440', 'c002');
INSERT INTO product (pid, pname, price, category_id) VALUES ('p007', 'The eu', '2000', 'c002');

INSERT INTO product (pid, pname, price, category_id) VALUES ('p008', 'Chanel', '800', 'c003');
INSERT INTO product (pid, pname, price, category_id) VALUES ('p009', 'Inoherb', '200', 'c003');




