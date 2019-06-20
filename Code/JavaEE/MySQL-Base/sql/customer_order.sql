###################################
#  一对多
###################################

# 1 创建一个用户表
CREATE TABLE `customer` (
  `id`   INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100)     DEFAULT NULL,
  `city` VARCHAR(20)      DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# 2 创建一个订单表，并关联用户表的主键
CREATE TABLE `orders` (
  `id`           INT(11) NOT NULL AUTO_INCREMENT,
  `order_number` VARCHAR(100)     DEFAULT NULL,
  `price`        FLOAT(8, 2)      DEFAULT NULL,
  `customer_id`  INT(11)          DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `customer_id_fk` (`customer_id`),
  CONSTRAINT `customer_id_fk` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# 3 插入订单数据
INSERT INTO `customer`
VALUES (1, '陈冠希', '香港'), (2, '李宗瑞', '台北'), (3, '苍井空', '东京'), (4, '钟欣桐', '香港'), (5, '芙蓉姐姐', NULL);

INSERT INTO `orders` VALUES (1, '0001', 100.00, 1), (2, '0002', 200.00, 1), (3, '0003', 300.00, 1),
  (4, '0004', 100.00, 2), (5, '0005', 200.00, 3), (6, '0006', 100.00, 4),
  (7, '0007', 1000.00, NULL);