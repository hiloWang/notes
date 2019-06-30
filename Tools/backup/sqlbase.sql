-- MySQL dump 10.13  Distrib 5.7.15, for Win64 (x86_64)
--
-- Host: localhost    Database: sqlbase
-- ------------------------------------------------------
-- Server version	5.7.15-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `money` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'a','1000'),(2,'b','1000'),(3,'c','1000'),(4,'aaa','1000'),(5,'bbb','1000'),(6,'ccc','1000');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aop_account`
--

DROP TABLE IF EXISTS `aop_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aop_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) DEFAULT NULL,
  `money` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aop_account`
--

LOCK TABLES `aop_account` WRITE;
/*!40000 ALTER TABLE `aop_account` DISABLE KEYS */;
INSERT INTO `aop_account` VALUES (1,'aaa',900),(2,'bbb',1100),(3,'ccc',1000);
/*!40000 ALTER TABLE `aop_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `cid` varchar(32) NOT NULL,
  `cname` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES ('c001','home appliance'),('c002','costume'),('c003','cosmetics');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `city` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'陈冠希','香港'),(2,'李宗瑞','台北'),(3,'苍井空','东京'),(4,'钟欣桐','香港'),(5,'芙蓉姐姐',NULL);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dbu_user`
--

DROP TABLE IF EXISTS `dbu_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dbu_user` (
  `id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dbu_user`
--

LOCK TABLES `dbu_user` WRITE;
/*!40000 ALTER TABLE `dbu_user` DISABLE KEYS */;
INSERT INTO `dbu_user` VALUES (1,'aa','2002-01-05'),(2,'bb','2001-09-08');
/*!40000 ALTER TABLE `dbu_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hbm_customer`
--

DROP TABLE IF EXISTS `hbm_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hbm_customer` (
  `cust_id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '客户编号(主键)',
  `cust_name` varchar(32) NOT NULL COMMENT '客户名称(公司名称)',
  `cust_source` varchar(32) DEFAULT NULL COMMENT '客户信息来源',
  `cust_industry` varchar(32) DEFAULT NULL COMMENT '客户所属行业',
  `cust_level` varchar(32) DEFAULT NULL COMMENT '客户级别',
  `cust_linkman` varchar(64) DEFAULT NULL COMMENT '联系人',
  `cust_phone` varchar(64) DEFAULT NULL COMMENT '固定电话',
  `cust_mobile` varchar(16) DEFAULT NULL COMMENT '移动电话',
  PRIMARY KEY (`cust_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hbm_customer`
--

LOCK TABLES `hbm_customer` WRITE;
/*!40000 ALTER TABLE `hbm_customer` DISABLE KEYS */;
INSERT INTO `hbm_customer` VALUES (1,'杀生丸',NULL,NULL,'VIP',NULL,NULL,NULL),(2,'杀生丸',NULL,NULL,'VIP',NULL,NULL,NULL);
/*!40000 ALTER TABLE `hbm_customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `multi_department`
--

DROP TABLE IF EXISTS `multi_department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `multi_department` (
  `id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `floor` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `multi_department`
--

LOCK TABLES `multi_department` WRITE;
/*!40000 ALTER TABLE `multi_department` DISABLE KEYS */;
INSERT INTO `multi_department` VALUES (1,'开发部','18层');
/*!40000 ALTER TABLE `multi_department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `multi_employee`
--

DROP TABLE IF EXISTS `multi_employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `multi_employee` (
  `id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `salary` float(8,2) DEFAULT NULL,
  `depart_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `depart_id_fk` (`depart_id`),
  CONSTRAINT `depart_id_fk` FOREIGN KEY (`depart_id`) REFERENCES `multi_department` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `multi_employee`
--

LOCK TABLES `multi_employee` WRITE;
/*!40000 ALTER TABLE `multi_employee` DISABLE KEYS */;
INSERT INTO `multi_employee` VALUES (1,'沙悟净',100000.00,1),(2,'小熊',100000.00,1);
/*!40000 ALTER TABLE `multi_employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `multi_idcard`
--

DROP TABLE IF EXISTS `multi_idcard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `multi_idcard` (
  `id` int(11) NOT NULL,
  `num` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `person_id_fk` FOREIGN KEY (`id`) REFERENCES `multi_person` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `multi_idcard`
--

LOCK TABLES `multi_idcard` WRITE;
/*!40000 ALTER TABLE `multi_idcard` DISABLE KEYS */;
INSERT INTO `multi_idcard` VALUES (1,'42XXXX');
/*!40000 ALTER TABLE `multi_idcard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `multi_person`
--

DROP TABLE IF EXISTS `multi_person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `multi_person` (
  `id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `multi_person`
--

LOCK TABLES `multi_person` WRITE;
/*!40000 ALTER TABLE `multi_person` DISABLE KEYS */;
INSERT INTO `multi_person` VALUES (1,'WJ');
/*!40000 ALTER TABLE `multi_person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `multi_student`
--

DROP TABLE IF EXISTS `multi_student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `multi_student` (
  `id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `grade` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `multi_student`
--

LOCK TABLES `multi_student` WRITE;
/*!40000 ALTER TABLE `multi_student` DISABLE KEYS */;
INSERT INTO `multi_student` VALUES (1,'WJ','A'),(2,'XX','A');
/*!40000 ALTER TABLE `multi_student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `multi_teacher`
--

DROP TABLE IF EXISTS `multi_teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `multi_teacher` (
  `id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `salary` float(8,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `multi_teacher`
--

LOCK TABLES `multi_teacher` WRITE;
/*!40000 ALTER TABLE `multi_teacher` DISABLE KEYS */;
INSERT INTO `multi_teacher` VALUES (1,'BXD',100000.00),(2,'WZT',1000.00);
/*!40000 ALTER TABLE `multi_teacher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `multi_teacher_student`
--

DROP TABLE IF EXISTS `multi_teacher_student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `multi_teacher_student` (
  `t_id` int(11) NOT NULL,
  `s_id` int(11) NOT NULL,
  PRIMARY KEY (`t_id`,`s_id`),
  KEY `s_id_fk` (`s_id`),
  CONSTRAINT `s_id_fk` FOREIGN KEY (`s_id`) REFERENCES `multi_student` (`id`),
  CONSTRAINT `t_id_fk` FOREIGN KEY (`t_id`) REFERENCES `multi_teacher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `multi_teacher_student`
--

LOCK TABLES `multi_teacher_student` WRITE;
/*!40000 ALTER TABLE `multi_teacher_student` DISABLE KEYS */;
INSERT INTO `multi_teacher_student` VALUES (1,1),(2,1),(1,2),(2,2);
/*!40000 ALTER TABLE `multi_teacher_student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mvc_sample_customer`
--

DROP TABLE IF EXISTS `mvc_sample_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mvc_sample_customer` (
  `id` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `gender` varchar(100) NOT NULL,
  `birthday` date NOT NULL,
  `cellphone` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `hobbies` varchar(100) NOT NULL,
  `type` varchar(100) NOT NULL,
  `description` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mvc_sample_customer`
--

LOCK TABLES `mvc_sample_customer` WRITE;
/*!40000 ALTER TABLE `mvc_sample_customer` DISABLE KEYS */;
INSERT INTO `mvc_sample_customer` VALUES ('1','张学友','male','1990-11-21','18817013387','zxy@123.com','吃饭','VIP','歌神'),('12d9c485-3360-4220-8737-b122f1cfdf6c','湛添友','male','1990-11-21','18817013387','ztiany@java.com','吃饭,睡觉,学Java','VIP','伟大的神'),('20e29afe-118b-4b86-8e42-39e27837c6f2','98_外星人','female','1990-11-21','111','98_wxr@123.com','吃饭,睡觉','普通客户','美女'),('248c6b65-eb3c-4d96-99fa-52263d793843','42_外星人','female','1990-11-21','111','42_wxr@123.com','吃饭,睡觉','普通客户','美女'),('2a886670-86a9-43a6-bda4-9cc7460680d7','34_外星人','female','1990-11-21','111','34_wxr@123.com','吃饭,睡觉','普通客户','美女'),('34b53b85-9a70-4438-abbe-a157b44f67ee','28_外星人','female','1990-11-21','111','28_wxr@123.com','吃饭,睡觉','普通客户','美女'),('3b39df03-901f-4500-b37d-84449ced3e06','46_外星人','female','1990-11-21','111','46_wxr@123.com','吃饭,睡觉','普通客户','美女'),('3ceda515-71b1-4a97-aaa0-5603a8c4627b','31_外星人','female','1990-11-21','111','31_wxr@123.com','吃饭,睡觉','普通客户','美女'),('3efc392d-49b7-4c1d-92a2-daa349dcd183','74_外星人','female','1990-11-21','111','74_wxr@123.com','吃饭,睡觉','普通客户','美女'),('417ae535-9028-47cf-b9e6-0ef31001a760','20_外星人','female','1990-11-21','111','20_wxr@123.com','吃饭,睡觉','普通客户','美女'),('477a7c5d-0b18-41aa-a3ad-6f53c2f65c16','29_外星人','female','1990-11-21','111','29_wxr@123.com','吃饭,睡觉','普通客户','美女'),('47b64314-ff57-4acd-aebe-0cd57a504000','49_外星人','female','1990-11-21','111','49_wxr@123.com','吃饭,睡觉','普通客户','美女'),('4b8b9c86-f198-492e-b7d7-708a01279155','87_外星人','female','1990-11-21','111','87_wxr@123.com','吃饭,睡觉','普通客户','美女'),('4cd4dcb8-3f74-462a-acaa-e2802aa4db9d','93_外星人','female','1990-11-21','111','93_wxr@123.com','吃饭,睡觉','普通客户','美女'),('4f4bd291-7257-47ab-aece-75eae2ed0d21','50_外星人','female','1990-11-21','111','50_wxr@123.com','吃饭,睡觉','普通客户','美女'),('4fdb2509-6221-4912-8676-557c911beac2','54_外星人','female','1990-11-21','111','54_wxr@123.com','吃饭,睡觉','普通客户','美女'),('51003793-b752-4dc2-aef0-67f651094d60','17_外星人','female','1990-11-21','111','17_wxr@123.com','吃饭,睡觉','普通客户','美女'),('51adee5c-03b8-4a08-9ddb-4aa9e3b8df98','88_外星人','female','1990-11-21','111','88_wxr@123.com','吃饭,睡觉','普通客户','美女'),('550f7d35-4d2e-4c3d-9f8a-a14586910ac3','0_外星人','female','1990-11-21','111','0_wxr@123.com','吃饭,睡觉','普通客户','美女'),('5641bb85-f6d7-40d1-a90c-dcd8fa6e0723','68_外星人','female','1990-11-21','111','68_wxr@123.com','吃饭,睡觉','普通客户','美女'),('56d3f7cb-d41a-4247-90e9-6acb6628d1e9','4_外星人','female','1990-11-21','111','4_wxr@123.com','吃饭,睡觉','普通客户','美女'),('5775bc5c-ccb1-4c54-8b71-261e9f0d47c4','5_外星人','female','1990-11-21','111','5_wxr@123.com','吃饭,睡觉','普通客户','美女'),('5ca5b480-36ed-41f4-b86b-ca20ee03937c','62_外星人','female','1990-11-21','111','62_wxr@123.com','吃饭,睡觉','普通客户','美女'),('6374b5f3-ef7b-418e-8817-468e707d9539','78_外星人','female','1990-11-21','111','78_wxr@123.com','吃饭,睡觉','普通客户','美女'),('6716a8c6-a69a-4b82-9c51-9f4b10447d48','83_外星人','female','1990-11-21','111','83_wxr@123.com','吃饭,睡觉','普通客户','美女'),('6a14f1a0-c8fd-45a3-8d5b-302c9a563b95','81_外星人','female','1990-11-21','111','81_wxr@123.com','吃饭,睡觉','普通客户','美女'),('6f753f36-9a9d-4957-aec1-41a57af34980','13_外星人','female','1990-11-21','111','13_wxr@123.com','吃饭,睡觉','普通客户','美女'),('7025a007-2ba0-4a1b-97ac-9b257b956a04','10_外星人','female','1990-11-21','111','10_wxr@123.com','吃饭,睡觉','普通客户','美女'),('71ed5ea9-d001-4af4-9e48-ac23607b24d1','53_外星人','female','1990-11-21','111','53_wxr@123.com','吃饭,睡觉','普通客户','美女'),('733d7f8a-3012-4247-abeb-222b4561c6fd','63_外星人','female','1990-11-21','111','63_wxr@123.com','吃饭,睡觉','普通客户','美女'),('768ffdb2-2d2b-43d8-92fb-273cb0396af8','11_外星人','female','1990-11-21','111','11_wxr@123.com','吃饭,睡觉','普通客户','美女'),('78e614cf-8af3-4e7a-ba42-74d638c7aac6','92_外星人','female','1990-11-21','111','92_wxr@123.com','吃饭,睡觉','普通客户','美女'),('794b77da-5a06-411b-81ce-5d309f2ccfc6','90_外星人','female','1990-11-21','111','90_wxr@123.com','吃饭,睡觉','普通客户','美女'),('7a8d7ca4-5505-4b03-a6c0-e055fcfc34f4','40_外星人','female','1990-11-21','111','40_wxr@123.com','吃饭,睡觉','普通客户','美女'),('7f75a3e9-7b52-498e-9162-bdf81a72e0d9','86_外星人','female','1990-11-21','111','86_wxr@123.com','吃饭,睡觉','普通客户','美女'),('84056676-dba0-45b6-ad31-8ae1f6dba9ae','67_外星人','female','1990-11-21','111','67_wxr@123.com','吃饭,睡觉','普通客户','美女'),('858df010-f54e-48ad-b8f1-d962682f10cd','8_外星人','female','1990-11-21','111','8_wxr@123.com','吃饭,睡觉','普通客户','美女'),('862b9ea1-9965-4f04-9e67-a0e59f9227b0','19_外星人','female','1990-11-21','111','19_wxr@123.com','吃饭,睡觉','普通客户','美女'),('8a7f4d68-5c7a-4d01-bb02-edd33b4f2c67','47_外星人','female','1990-11-21','111','47_wxr@123.com','吃饭,睡觉','普通客户','美女'),('8b99b5b6-efa9-4264-8f2b-530d2f14c348','66_外星人','female','1990-11-21','111','66_wxr@123.com','吃饭,睡觉','普通客户','美女'),('8e455e8f-cd2b-418e-9285-444208e5026d','94_外星人','female','1990-11-21','111','94_wxr@123.com','吃饭,睡觉','普通客户','美女'),('8eb3e1a3-f35a-43e7-a344-3be3788084f3','75_外星人','female','1990-11-21','111','75_wxr@123.com','吃饭,睡觉','普通客户','美女'),('8f26f9e0-fc22-40ba-a73e-79835a550b4d','99_外星人','female','1990-11-21','111','99_wxr@123.com','吃饭,睡觉','普通客户','美女'),('8fa599fa-bd86-4812-bc77-4057afa87584','3_外星人','female','1990-11-21','111','3_wxr@123.com','吃饭,睡觉','普通客户','美女'),('93da5e8f-2a19-4740-bd5b-5cf9741b0f08','2_外星人','female','1990-11-21','111','2_wxr@123.com','吃饭,睡觉','普通客户','美女'),('958aeea3-c617-404f-ba06-98b6731230bb','56_外星人','female','1990-11-21','111','56_wxr@123.com','吃饭,睡觉','普通客户','美女'),('969c092b-c1b2-4dab-8b76-230dee86aed2','95_外星人','female','1990-11-21','111','95_wxr@123.com','吃饭,睡觉','普通客户','美女'),('9943ed00-d363-412e-bd65-c495d07d06b9','72_外星人','female','1990-11-21','111','72_wxr@123.com','吃饭,睡觉','普通客户','美女'),('9af094c7-9ba4-4674-a06c-ab6b304ab1f2','73_外星人','female','1990-11-21','111','73_wxr@123.com','吃饭,睡觉','普通客户','美女'),('9d59f24f-acc1-41b9-86c1-184f9a3927f8','18_外星人','female','1990-11-21','111','18_wxr@123.com','吃饭,睡觉','普通客户','美女'),('9daf5949-e358-49ec-a861-29f106a9492f','64_外星人','female','1990-11-21','111','64_wxr@123.com','吃饭,睡觉','普通客户','美女'),('9db75d90-67c6-4f03-94c0-132ed2d1e58c','22_外星人','female','1990-11-21','111','22_wxr@123.com','吃饭,睡觉','普通客户','美女'),('9f76b926-3512-4ecb-8986-d11e4f1a7f0f','71_外星人','female','1990-11-21','111','71_wxr@123.com','吃饭,睡觉','普通客户','美女'),('a1468022-ca7d-4dc5-87b1-3c655d487b8b','55_外星人','female','1990-11-21','111','55_wxr@123.com','吃饭,睡觉','普通客户','美女'),('a8b043a3-80d2-4d2f-bdef-fc91cddd8cfa','14_外星人','female','1990-11-21','111','14_wxr@123.com','吃饭,睡觉','普通客户','美女'),('b006fa76-d680-4fed-8b7e-886b016d3fc8','30_外星人','female','1990-11-21','111','30_wxr@123.com','吃饭,睡觉','普通客户','美女'),('b063483a-3f19-4e54-8d93-8258a83cd569','25_外星人','female','1990-11-21','111','25_wxr@123.com','吃饭,睡觉','普通客户','美女'),('b1c4aca6-eaea-4b02-9653-20996c5cef39','43_外星人','female','1990-11-21','111','43_wxr@123.com','吃饭,睡觉','普通客户','美女'),('b4a0a112-d2fe-4443-a1a8-f089e9d1bc8f','12_外星人','female','1990-11-21','111','12_wxr@123.com','吃饭,睡觉','普通客户','美女'),('b6093c27-73be-4304-92d9-d5172a965961','79_外星人','female','1990-11-21','111','79_wxr@123.com','吃饭,睡觉','普通客户','美女'),('b6cac551-ac95-4639-ba6f-fb0014bd1b60','60_外星人','female','1990-11-21','111','60_wxr@123.com','吃饭,睡觉','普通客户','美女'),('ba8aeec5-8b2a-4306-a670-56201a9e4a6c','69_外星人','female','1990-11-21','111','69_wxr@123.com','吃饭,睡觉','普通客户','美女'),('bb4fa9e8-c17d-438e-aa0f-0391da714a43','48_外星人','female','1990-11-21','111','48_wxr@123.com','吃饭,睡觉','普通客户','美女'),('c1ea34f2-8a41-4e02-b3ff-096f82f59513','32_外星人','female','1990-11-21','111','32_wxr@123.com','吃饭,睡觉','普通客户','美女'),('c605a89d-d2c2-418b-99f4-a55f4ef72f8a','96_外星人','female','1990-11-21','111','96_wxr@123.com','吃饭,睡觉','普通客户','美女'),('c62bfd11-33d4-4f53-bdde-227d07a39edf','27_外星人','female','1990-11-21','111','27_wxr@123.com','吃饭,睡觉','普通客户','美女'),('c64e54fa-1dd7-495b-b3e3-a69ea9753ab5','35_外星人','female','1990-11-21','111','35_wxr@123.com','吃饭,睡觉','普通客户','美女'),('c6744efd-d9bf-402e-b2a0-a524dbecd2c7','44_外星人','female','1990-11-21','111','44_wxr@123.com','吃饭,睡觉','普通客户','美女'),('c67821d3-574e-4043-8f3a-87838fe2bca9','41_外星人','female','1990-11-21','111','41_wxr@123.com','吃饭,睡觉','普通客户','美女'),('ca2f0499-803b-4cbd-b0c4-b8b19b5826b5','65_外星人','female','1990-11-21','111','65_wxr@123.com','吃饭,睡觉','普通客户','美女'),('cc3c8dd9-915c-4bfd-972c-1143411b3993','80_外星人','female','1990-11-21','111','80_wxr@123.com','吃饭,睡觉','普通客户','美女'),('cfe7bf56-1d4c-4259-8db7-c316aadca0c6','9_外星人','female','1990-11-21','111','9_wxr@123.com','吃饭,睡觉','普通客户','美女'),('d13537bf-f0df-403d-b96f-75816fa102aa','85_外星人','female','1990-11-21','111','85_wxr@123.com','吃饭,睡觉','普通客户','美女'),('d5202b4b-1c2e-4b36-8638-0b57f1ccde27','82_外星人','female','1990-11-21','111','82_wxr@123.com','吃饭,睡觉','普通客户','美女'),('d5246ec6-f64f-4ae0-af9c-b591820087de','61_外星人','female','1990-11-21','111','61_wxr@123.com','吃饭,睡觉','普通客户','美女'),('daa7e650-8781-42f6-8634-a97898490908','58_外星人','female','1990-11-21','111','58_wxr@123.com','吃饭,睡觉','普通客户','美女'),('dbc22a7e-0b37-47fe-9e0d-3741be8daea2','24_外星人','female','1990-11-21','111','24_wxr@123.com','吃饭,睡觉','普通客户','美女'),('dd12a71c-7836-4c51-b678-97cc8b830e37','52_外星人','female','1990-11-21','111','52_wxr@123.com','吃饭,睡觉','普通客户','美女'),('dd7eb95a-7e7a-4cb1-9fd1-55b7c222affb','97_外星人','female','1990-11-21','111','97_wxr@123.com','吃饭,睡觉','普通客户','美女'),('debbe13f-734f-4728-9d0a-f1174566ee8f','15_外星人','female','1990-11-21','111','15_wxr@123.com','吃饭,睡觉','普通客户','美女'),('df1a2bff-4360-43da-9648-dfff426f2928','1_外星人','female','1990-11-21','111','1_wxr@123.com','吃饭,睡觉','普通客户','美女'),('e2a8d11f-4914-4d7d-a029-d9f704d720b8','45_外星人','female','1990-11-21','111','45_wxr@123.com','吃饭,睡觉','普通客户','美女'),('e2d16a55-c21d-46dc-965f-0f7908789654','36_外星人','female','1990-11-21','111','36_wxr@123.com','吃饭,睡觉','普通客户','美女'),('e30beef4-23bf-4e6f-95c8-6c0fd8b735c4','84_外星人','female','1990-11-21','111','84_wxr@123.com','吃饭,睡觉','普通客户','美女'),('e4d6f850-c97f-4780-a2f3-b55dc07cbee3','70_外星人','female','1990-11-21','111','70_wxr@123.com','吃饭,睡觉','普通客户','美女'),('e639b577-4c05-4d95-a97e-8c0603c04d2a','77_外星人','female','1990-11-21','111','77_wxr@123.com','吃饭,睡觉','普通客户','美女'),('e6a16c59-a915-4684-9b19-83dcd03a1804','59_外星人','female','1990-11-21','111','59_wxr@123.com','吃饭,睡觉','普通客户','美女'),('e6cc4d3f-9b7e-4606-b85c-eaf62eaa5c77','33_外星人','female','1990-11-21','111','33_wxr@123.com','吃饭,睡觉','普通客户','美女'),('ec6c361c-073b-432f-bd79-b717822ca1a7','16_外星人','female','1990-11-21','111','16_wxr@123.com','吃饭,睡觉','普通客户','美女'),('f3f2c89a-037a-4ac7-a5ac-0bd17ba69433','37_外星人','female','1990-11-21','111','37_wxr@123.com','吃饭,睡觉','普通客户','美女'),('f699fcbf-b18c-43e9-af05-724fcdb78f05','7_外星人','female','1990-11-21','111','7_wxr@123.com','吃饭,睡觉','普通客户','美女'),('fa4f471c-df2c-4115-93ef-a77e12bac796','38_外星人','female','1990-11-21','111','38_wxr@123.com','吃饭,睡觉','普通客户','美女'),('fb761c95-6f19-4439-97c1-e83e6edc9c56','6_外星人','female','1990-11-21','111','6_wxr@123.com','吃饭,睡觉','普通客户','美女'),('fcc41176-2446-402d-a142-f59ae06afc0a','91_外星人','female','1990-11-21','111','91_wxr@123.com','吃饭,睡觉','普通客户','美女'),('ff4c25a0-e45a-4cfc-9b08-181d16ab3f8e','57_外星人','female','1990-11-21','111','57_wxr@123.com','吃饭,睡觉','普通客户','美女'),('ff5c5de1-fbe4-4608-9dbf-8ad7b0e6deab','26_外星人','female','1990-11-21','111','26_wxr@123.com','吃饭,睡觉','普通客户','美女');
/*!40000 ALTER TABLE `mvc_sample_customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mvc_sample_user`
--

DROP TABLE IF EXISTS `mvc_sample_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mvc_sample_user` (
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `birthday` date NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mvc_sample_user`
--

LOCK TABLES `mvc_sample_user` WRITE;
/*!40000 ALTER TABLE `mvc_sample_user` DISABLE KEYS */;
INSERT INTO `mvc_sample_user` VALUES ('ztiany','ICy5YqxZB1uWSwcVLSNLcA==','ztiany@java.com','1990-11-21');
/*!40000 ALTER TABLE `mvc_sample_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mybatis_items`
--

DROP TABLE IF EXISTS `mybatis_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mybatis_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `price` float(10,1) NOT NULL,
  `detail` text,
  `pic` varchar(64) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mybatis_items`
--

LOCK TABLES `mybatis_items` WRITE;
/*!40000 ALTER TABLE `mybatis_items` DISABLE KEYS */;
INSERT INTO `mybatis_items` VALUES (5,'小米6',2000.0,'拍人更美！',NULL,'2018-06-06 23:45:39'),(6,'HuaWei',15310.2,'质量好！1',NULL,NULL),(7,'三星X9',10445.3,'质量好！2',NULL,NULL),(8,'1华为 荣耀8',2399.3,'质量好！3',NULL,NULL);
/*!40000 ALTER TABLE `mybatis_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mybatis_orders`
--

DROP TABLE IF EXISTS `mybatis_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mybatis_orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '下单用户id',
  `number` varchar(32) NOT NULL COMMENT '订单号',
  `createtime` datetime NOT NULL COMMENT '创建订单时间',
  `note` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `FK_orders_1` (`user_id`),
  CONSTRAINT `FK_orders_id` FOREIGN KEY (`user_id`) REFERENCES `mybatis_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mybatis_orders`
--

LOCK TABLES `mybatis_orders` WRITE;
/*!40000 ALTER TABLE `mybatis_orders` DISABLE KEYS */;
INSERT INTO `mybatis_orders` VALUES (3,1,'1000010','2015-02-04 13:22:35',NULL),(4,1,'1000011','2015-02-03 13:22:41',NULL),(5,10,'1000012','2015-02-12 16:13:23',NULL);
/*!40000 ALTER TABLE `mybatis_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mybatis_user`
--

DROP TABLE IF EXISTS `mybatis_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mybatis_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL COMMENT '用户名称',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `sex` char(1) DEFAULT NULL COMMENT '性别',
  `address` varchar(256) DEFAULT NULL COMMENT '地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mybatis_user`
--

LOCK TABLES `mybatis_user` WRITE;
/*!40000 ALTER TABLE `mybatis_user` DISABLE KEYS */;
INSERT INTO `mybatis_user` VALUES (1,'王五',NULL,'2',NULL),(10,'张三','2014-07-10','1','北京市'),(16,'张小明',NULL,'1','河南郑州'),(22,'陈小明',NULL,'1','河南郑州'),(24,'张三丰',NULL,'1','河南郑州'),(25,'陈小明',NULL,'1','河南郑州'),(26,'赵六',NULL,NULL,NULL),(27,'何炅哥',NULL,NULL,'湖南岳阳'),(28,'装学友','2018-05-27','男','香港'),(29,'张学友','2018-05-27','男','香港'),(31,'谭咏麟','2018-05-27','男','香港'),(32,'小马哥','2018-05-27','男','深圳'),(34,'张家辉','2018-05-27','男','香港'),(35,'古天乐','2018-05-27','男','香港'),(36,'香香1','2018-05-27','女','深圳'),(37,'张家辉','2018-05-29','男','香港');
/*!40000 ALTER TABLE `mybatis_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_number` varchar(100) DEFAULT NULL,
  `price` float(8,2) DEFAULT NULL,
  `customer_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `customer_id_fk` (`customer_id`),
  CONSTRAINT `customer_id_fk` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'0001',100.00,1),(2,'0002',200.00,1),(3,'0003',300.00,1),(4,'0004',100.00,2),(5,'0005',200.00,3),(6,'0006',100.00,4),(7,'0007',1000.00,NULL);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `pid` varchar(32) NOT NULL,
  `pname` varchar(40) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `category_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`pid`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES ('p001','Lenovo',5000,'c001'),('p002','Haier',5000,'c001'),('p003','Thor',5000,'c001'),('p004','JACK JONES',800,'c002'),('p005','Jeanswest',200,'c002'),('p006','playboy',440,'c002'),('p007','The eu',2000,'c002'),('p008','Chanel',800,'c003'),('p009','Inoherb',200,'c003');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `springf_test_account`
--

DROP TABLE IF EXISTS `springf_test_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `springf_test_account` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `money` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `springf_test_account`
--

LOCK TABLES `springf_test_account` WRITE;
/*!40000 ALTER TABLE `springf_test_account` DISABLE KEYS */;
INSERT INTO `springf_test_account` VALUES (1,'张三','700'),(2,'李四','1300');
/*!40000 ALTER TABLE `springf_test_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `springf_test_user`
--

DROP TABLE IF EXISTS `springf_test_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `springf_test_user` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `springf_test_user`
--

LOCK TABLES `springf_test_user` WRITE;
/*!40000 ALTER TABLE `springf_test_user` DISABLE KEYS */;
INSERT INTO `springf_test_user` VALUES (1,'rose');
/*!40000 ALTER TABLE `springf_test_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student` (
  `id` int(11) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `chinese` float DEFAULT NULL,
  `english` float DEFAULT NULL,
  `math` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (1,'aaaa',89,78,90),(2,'bbbb',67,98,56),(3,'cccc',87,78,77),(4,'dddd',88,98,90),(5,'eeee',82,84,67),(6,'ffff',55,85,45),(7,'gggg',75,65,30),(8,'hhhh',88,98,90),(9,'iiii',82,84,67),(10,'jjjj',55,85,45),(11,'kkkk',75,65,30);
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) DEFAULT NULL,
  `password` varchar(40) DEFAULT NULL,
  `email` varchar(60) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'zs','123456','zs@sina.com','1980-12-04'),(2,'lisi','123456','lisi@sina.com','1981-12-04'),(3,'wangwu','123456','wangwu@sina.com','1979-12-04');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `weather`
--

DROP TABLE IF EXISTS `weather`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `weather` (
  `city` varchar(100) NOT NULL,
  `temperature` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `weather`
--

LOCK TABLES `weather` WRITE;
/*!40000 ALTER TABLE `weather` DISABLE KEYS */;
INSERT INTO `weather` VALUES ('Austin',48),('Boton Rouge',21),('Jsckson',2),('Montgomery',32),('Sacramento',22),('Santa',1),('Tallahassee',23),('Shenzhen',17);
/*!40000 ALTER TABLE `weather` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-09-12 22:00:17
