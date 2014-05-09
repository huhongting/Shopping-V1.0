-- MySQL dump 10.10
--
-- Host: localhost    Database: shopping
-- ------------------------------------------------------
-- Server version	5.0.22-community

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
CREATE TABLE `account` (
  `id` int(11) NOT NULL auto_increment,
  `ctime` datetime default NULL,
  `money` double NOT NULL,
  `note` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

--
-- Dumping data for table `account`
--


/*!40000 ALTER TABLE `account` DISABLE KEYS */;
LOCK TABLES `account` WRITE;
INSERT INTO `account` VALUES (1,'2014-04-03 11:21:45',579,NULL),(2,'2014-04-03 19:40:26',4,NULL),(5,'2014-04-08 16:08:14',0,NULL),(6,'2014-04-08 16:24:38',0,NULL),(7,'2014-04-08 17:30:22',0,NULL),(8,'2014-04-15 18:57:58',0,NULL),(9,'2014-04-19 11:19:08',0,NULL);
UNLOCK TABLES;
/*!40000 ALTER TABLE `account` ENABLE KEYS */;

--
-- Table structure for table `accountrecord`
--

DROP TABLE IF EXISTS `accountrecord`;
CREATE TABLE `accountrecord` (
  `id` int(11) NOT NULL auto_increment,
  `cmd` varchar(200) NOT NULL,
  `money` double NOT NULL,
  `remark` varchar(255) default NULL,
  `time` datetime default NULL,
  `accountid` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK42E0BE3E409E8B` (`accountid`),
  CONSTRAINT `FK42E0BE3E409E8B` FOREIGN KEY (`accountid`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

--
-- Dumping data for table `accountrecord`
--


/*!40000 ALTER TABLE `accountrecord` DISABLE KEYS */;
LOCK TABLES `accountrecord` WRITE;
INSERT INTO `accountrecord` VALUES (1,'存入',40,'取消订单1404022215631001存入￥40.0','2014-04-14 10:40:45',1),(2,'支付',-10,'为订单1404141933010250支付￥10.0','2014-04-14 19:46:09',1),(3,'存入',22,'支付额超过订单1404031910246027应付额存入￥22.0','2014-04-15 10:33:50',1),(4,'支付',0,'为订单1404182048040317支付￥0.0','2014-04-18 20:48:18',1),(5,'支付',0,'为订单1404182103623424支付￥0.0','2014-04-18 21:03:06',1),(6,'支付',0,'为订单1404182110965727支付￥0.0','2014-04-18 21:10:00',1),(7,'支付',0,'为订单1404182112290654支付￥0.0','2014-04-18 21:12:43',1),(8,'支付',0,'为订单1404190901534016支付￥0.0','2014-04-19 09:01:43',1),(9,'支付',0,'为订单1404232040160263支付￥0.0','2014-04-23 20:40:58',2),(10,'支付',0,'为订单1404232045587240支付￥0.0','2014-04-23 20:45:01',2),(11,'支付',0,'为订单1404232045188699支付￥0.0','2014-04-23 20:45:51',2),(12,'存入',22,'取消订单1404232045188699存入￥22.0','2014-04-26 16:22:31',2),(13,'存入',600,'取消订单1404031909345410存入￥600.0','2014-04-26 16:53:34',1),(14,'支付',0,'为订单1404261659801954支付￥0.0','2014-04-26 16:59:50',1),(15,'支付',-18,'支付订单1404190901534016配送费用￥18.0','2014-04-26 17:57:04',1),(16,'支付',-18,'支付订单1404051906581642配送费用￥18.0','2014-04-26 18:21:47',2),(17,'支付',-90,'为订单1404261828796518支付￥90.0','2014-04-26 18:28:29',1),(18,'存入',35,'支付额超过订单1404261828796518应付额存入￥35.0','2014-04-26 18:32:07',1);
UNLOCK TABLES;
/*!40000 ALTER TABLE `accountrecord` ENABLE KEYS */;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `bookid` int(11) NOT NULL auto_increment,
  `auther` varchar(100) NOT NULL,
  `bookname` varchar(100) NOT NULL,
  `delflag` bit(1) NOT NULL,
  `normalprice` double default NULL,
  `note` longtext,
  `number` int(11) NOT NULL,
  `picurl` varchar(100) NOT NULL,
  `price` double NOT NULL,
  `pubtime` varchar(15) NOT NULL,
  `remark` longtext,
  `saleinfo` longtext,
  `typeid` int(11) default NULL,
  `pressid` int(11) default NULL,
  `hot` bit(1) default NULL,
  `recom` bit(1) default NULL,
  `downtime` datetime default NULL,
  `uptime` datetime default NULL,
  `salecount` int(11) default NULL,
  PRIMARY KEY  (`bookid`),
  KEY `FK2E3AE9F2EE8F48` (`pressid`),
  KEY `FK2E3AE95FC32BD1` (`typeid`),
  CONSTRAINT `FK2E3AE95FC32BD1` FOREIGN KEY (`typeid`) REFERENCES `booktype` (`typeid`),
  CONSTRAINT `FK2E3AE9F2EE8F48` FOREIGN KEY (`pressid`) REFERENCES `press` (`pressid`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

--
-- Dumping data for table `book`
--


/*!40000 ALTER TABLE `book` DISABLE KEYS */;
LOCK TABLES `book` WRITE;
INSERT INTO `book` VALUES (1,'(美)Charlie Hunt,  Binu John','Java性能优化权威指南','\0',109,'',10,'/resources/upload/books/cover/ncover_2014.jpg',81,'2014-03-11','','《java性能优化权威指南》由曾担任Oracle/Sun的性能优化专家编写。<br>java性能优化的任何问题都可以从本书中找到答案！<br>',43,3,'\0','',NULL,'2014-03-17 17:37:58',4),(3,'(美)Henry S. Warren','算法心得：高效算法的奥秘(第二版)','\0',89,'',11,'/resources/upload/books/cover/ncover_1395058431105.PNG',66,'2014-03-17','','',46,3,'\0','',NULL,'2014-03-17 20:13:51',3),(4,'(巴西)Loiane Groner','精通ExtJS','\0',59,'',0,'/resources/upload/books/cover/ncover_1395058646063.PNG',45,'2014-03-17','','',133,8,'\0','',NULL,'2014-03-17 20:17:26',4),(5,'杨巨龙','大数据技术全解：基础、设计、开发与实践','\0',42,'',23,'/resources/upload/books/cover/ncover_1395058816669.PNG',59,'2014-03-17','','',29,3,'\0','',NULL,'2014-03-17 20:20:16',3),(6,'(日)保坂隆','最强大脑：77招让你成为脑力最好的人','\0',35,'',11,'/resources/upload/books/cover/ncover_1395058983320.PNG',23,'2014-02-12','','',38,6,'\0','',NULL,'2014-03-17 20:23:03',4),(7,'（美）Brian W.Kernighan,Dennis M.Ritchie','C程序设计语言(第2版·新版)','\0',30,'',12,'/resources/upload/books/cover/ncover_1395129087931.PNG',20,'2004-04-14','','',39,3,'\0','',NULL,'2014-03-18 15:51:27',4),(8,'Stuart J. Russell;Peter Norvig','人工智能：一种现代的方法(第3版)','\0',126,'',9,'/resources/upload/books/cover/ncover_1395129192097.PNG',106,'2010-03-03','','',47,1,'\0','',NULL,'2014-03-18 15:53:12',6),(9,'(德)Gerhard Weikum;Gottfried Vossen','事务信息系统：并发控制与恢复的理论、算法与实践','\0',122,'',2,'/resources/upload/books/cover/ncover_1395129358865.PNG',56,'2006-11-01','','',133,3,'\0','\0',NULL,'2014-03-18 15:55:58',5),(10,'李欣苗','决策支持系统','\0',43,'',22,'/resources/upload/books/cover/ncover_1395129444584.PNG',23,'2014-03-18','','',47,1,'\0','\0',NULL,'2014-03-18 15:57:24',5),(11,'罗国勋;罗昕;蒋天颖;丛祝辉','系统建模与仿真','\0',87,'',1,'/resources/upload/books/cover/ncover_1395129651728.PNG',68,'2011-04-12','','',47,5,'\0','\0',NULL,'2014-03-18 16:00:51',7),(12,'闪四清','ERP系统原理与实施','\0',38,'',2,'/resources/upload/books/cover/ncover_1395129868833.PNG',30,'2010-01-01','','',133,1,'\0','\0',NULL,'2014-03-18 16:04:28',7),(13,'（美）Rafael C.Gonzalez,Richard E.Woods,Steven L.Eddins','数字图像处理（MATLAB版）','\0',50,'',11,'/resources/upload/books/cover/ncover_1395130073095.PNG',37,'2009-09-10','','',131,9,'\0','\0',NULL,'2014-03-18 16:07:53',8),(14,'郝维春 等','CAD工程制图--AutoCAD 2006（中文版）软件应用','\0',88,'',12,'/resources/upload/books/cover/ncover_1395130159097.PNG',70,'2006-11-08','','',131,9,'\0','',NULL,'2014-03-18 16:09:19',10),(15,'不详','3DMAX建模与动画(普通高等院校“十二五”规划计算机辅助设计系列教材)','',45,'',5,'/resources/upload/books/cover/ncover_1395130307902.PNG',40,'2012-09-05','','',131,10,'\0','','2014-04-26 10:52:13','2014-04-26 10:52:03',8),(16,'窦连江 林漪','高等数学(经管类专业适用)(第二版)','\0',36,'',23,'/resources/upload/books/cover/ncover_1395130696124.PNG',24,'2011-08-01','','',28,5,'\0','\0',NULL,'2014-03-18 16:18:16',8),(17,'同济大学数学系','高等数学(第六版.上册)','\0',34,'',2,'/resources/upload/books/cover/ncover_1395130774991.PNG',29,'2011-02-02','','',28,5,'\0','',NULL,'2014-03-18 16:19:34',9),(18,'(美)Sheldon M. Ross','随机过程（原书第二版）','\0',79,'',23,'/resources/upload/books/cover/ncover_1395130886550.PNG',55,'2014-07-01','','',28,3,'\0','\0',NULL,'2014-03-18 16:21:26',9),(19,'(美)德格鲁特(DeGroot, M. H.) (美)舍维什(Schervish, M. J.)','概率统计(英文版第4版)','\0',139,'',232,'/resources/upload/books/cover/ncover_1395131001584.PNG',97,'2012-07-01','','概率统计(英文版第4版)<br>经典的概率论与数理统计教材，多年来畅销不衰，被CMU、哈佛等众多名校采用！<br>',28,3,'\0','\0',NULL,'2014-03-18 16:23:21',10),(20,'William J. Stewart','概率论、马尔科夫链、排队和模拟','\0',139,'',11,'/resources/upload/books/cover/ncover_1395131153328.PNG',111,'2013-07-01','','',28,11,'\0','',NULL,'2014-03-18 16:25:53',11),(21,'（美）Richard P.Feynman,Robert B.Leighton,Matthew Sands','费恩曼物理学讲义（第一卷）','\0',85,'',12,'/resources/upload/books/cover/ncover_1395147309068.PNG',72,'2005-06-01','','',31,12,'\0','',NULL,'2014-03-18 20:55:09',16),(22,'（美）Richard P.Feynman,Robert B.Leighton,Matthew Sands','费恩曼物理学讲义（第二卷）','\0',85,'',12,'/resources/upload/books/cover/ncover_1395147426398.PNG',65,'2005-06-01','','',31,12,'\0','',NULL,'2014-03-18 20:57:06',12),(23,'（美）Richard P.Feynman,Robert B.Leighton,Matthew Sands','费恩曼物理学讲义（第三卷）','\0',85,'',12,'/resources/upload/books/cover/ncover_1395147546648.PNG',76,'2005-06-01','','',31,12,'\0','',NULL,'2014-03-18 20:59:06',18),(24,'(俄)л.д.朗道;E.M.栗弗席兹','量子力学：非相对论理论（第三版）','\0',87,'',0,'/resources/upload/books/cover/ncover_1395147658769.PNG',63,'2008-10-27','','',31,5,'\0','',NULL,'2014-03-18 21:00:58',15),(25,'E.M.票弗席兹;Л.П.皮塔耶夫斯基','物理动理学（第二版）','\0',55,'',3,'/resources/upload/books/cover/ncover_1395147762705.PNG',34,'2008-01-01','','',31,5,'\0','',NULL,'2014-03-18 21:02:42',14),(26,'宋天佑 程鹏 王杏乔 徐家宁','无机化学（上册）','\0',37,'',22,'/resources/upload/books/cover/ncover_1395147920124.PNG',32,'2013-02-01','','',32,5,'\0','\0',NULL,'2014-03-18 21:05:20',13),(27,'任丽萍','普通化学','\0',28,'',23,'/resources/upload/books/cover/ncover_1395147983594.PNG',17,'2006-08-01','','',32,5,'\0','\0',NULL,'2014-03-18 21:06:23',14),(28,'呼世斌 翟彤宇','无机及分析化学','\0',65,'',12,'/resources/upload/books/cover/ncover_1395148074734.PNG',47,'2010-06-01','','',32,5,'\0','\0',NULL,'2014-03-18 21:07:54',14),(29,'周昕 罗虹 刘文娟','大学化学实验（第二版）','\0',47,'',2,'/resources/upload/books/cover/ncover_1395148341130.PNG',37,'2012-07-01','','',32,13,'\0','\0',NULL,'2014-03-18 21:12:21',15),(30,'司徒正美','JavaScript框架设计','\0',56,'',12,'/resources/upload/books/cover/ncover_1397871355443.PNG',45,'2014-04-19','','',46,8,'\0','',NULL,'2014-04-19 09:35:55',1),(31,'司徒正','JavaScript框架开发','\0',56,'',2,'/resources/upload/books/cover/ncover_1397871935709.PNG',45,'2014-04-19','','',46,8,'\0','\0',NULL,'2014-04-19 09:45:35',1);
UNLOCK TABLES;
/*!40000 ALTER TABLE `book` ENABLE KEYS */;

--
-- Table structure for table `booktype`
--

DROP TABLE IF EXISTS `booktype`;
CREATE TABLE `booktype` (
  `typeid` int(11) NOT NULL auto_increment,
  `isleaf` bit(1) NOT NULL,
  `note` longtext,
  `typename` varchar(100) NOT NULL,
  `parentid` int(11) default NULL,
  `sortid` int(11) default NULL,
  `deleted` bit(1) default NULL,
  PRIMARY KEY  (`typeid`),
  KEY `FK778B2DA3D8FF0941` (`parentid`),
  CONSTRAINT `FK778B2DA3D8FF0941` FOREIGN KEY (`parentid`) REFERENCES `booktype` (`typeid`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

--
-- Dumping data for table `booktype`
--


/*!40000 ALTER TABLE `booktype` DISABLE KEYS */;
LOCK TABLES `booktype` WRITE;
INSERT INTO `booktype` VALUES (2,'\0','','哲学类',NULL,7,'\0'),(3,'\0','','经济类',NULL,6,'\0'),(4,'\0','','文学艺术类',NULL,10,'\0'),(5,'\0','','医学类',NULL,5,'\0'),(6,'\0','','管理类',NULL,4,'\0'),(7,'\0','','理学类',NULL,2,'\0'),(8,'\0','','工学类',NULL,3,'\0'),(9,'','','海洋科学类',NULL,10,''),(10,'\0','','农学类',NULL,10,'\0'),(11,'\0','','政治法学类',NULL,9,'\0'),(12,'','','西方哲学',2,10,'\0'),(13,'','','古希腊哲学',2,10,'\0'),(14,'','','宏观经济学',3,10,'\0'),(15,'','','微观经济学',3,10,'\0'),(16,'','','西方文学',4,10,'\0'),(17,'','','马克思主义理论类',11,10,'\0'),(18,'','','社会学',11,10,'\0'),(19,'','','政治学',11,10,'\0'),(20,'','','公安学',11,10,'\0'),(21,'\0','','教育学类',NULL,10,'\0'),(22,'\0','','体育教育学',21,10,'\0'),(23,'','','其他教育',21,10,'\0'),(24,'','','田径',22,10,'\0'),(25,'','','中国语言文学',4,10,'\0'),(26,'','','新闻传播学',4,10,'\0'),(27,'\0','','历史学',NULL,10,'\0'),(28,'','','数学类',7,10,'\0'),(29,'','','计算机类',7,10,''),(30,'','','电子科学',7,10,'\0'),(31,'','','物理学类',7,10,'\0'),(32,'','','化学类',7,10,'\0'),(33,'','','临床医学',5,10,'\0'),(34,'','','药学',5,10,'\0'),(35,'\0','','计算机/网络',NULL,1,'\0'),(36,'','','公共课与文化课',NULL,10,'\0'),(37,'\0','','外语类',NULL,8,'\0'),(38,'','','其他',NULL,10,'\0'),(39,'','','计算机基础',35,10,'\0'),(40,'','','计算机硬件设备及其维修',35,10,'\0'),(41,'','','操作系统',35,10,'\0'),(42,'','','数据库语言与编程',35,10,'\0'),(43,'','','计算机网络与安全',35,10,'\0'),(44,'','','图形图像处理',35,10,'\0'),(45,'','','多媒体',35,10,'\0'),(46,'','','软件工程/软件技术',35,10,'\0'),(47,'','','人工智能',35,10,'\0'),(48,'','','逻辑学',2,10,'\0'),(49,'','','宗教学',2,10,'\0'),(50,'','','政治经济学',3,10,'\0'),(51,'','','国际经济学',3,10,'\0'),(52,'','','财政学',3,10,'\0'),(53,'','','金融学',3,10,'\0'),(54,'','','金融理论',3,10,'\0'),(55,'','','证券与投资',3,10,'\0'),(56,'','','基金',3,10,'\0'),(57,'','','诉讼法',11,10,'\0'),(58,'','','民商法',11,10,'\0'),(59,'','','经济法',11,10,'\0'),(60,'','','国际法',11,10,'\0'),(61,'','','宪法/刑法',11,10,'\0'),(62,'','','学前教育',21,10,'\0'),(63,'','','特殊教育',21,10,'\0'),(64,'','','广告学',4,10,'\0'),(65,'','','编辑出版学',4,10,'\0'),(66,'','','舞蹈学',4,10,'\0'),(67,'','','戏剧学',4,10,'\0'),(68,'','','导演',4,10,'\0'),(69,'','','动画',4,10,'\0'),(70,'','','表演',4,10,'\0'),(71,'','','摄影',4,10,'\0'),(72,'','','影视戏剧文学',4,10,'\0'),(73,'','','世界历史',27,10,'\0'),(74,'','','考古学',27,10,'\0'),(75,'','','博物馆学',27,10,'\0'),(76,'','','民族学',27,10,'\0'),(77,'','','数学与应用数学',7,10,'\0'),(78,'','','信息与计算科学',7,10,'\0'),(79,'','','生物科学',7,10,'\0'),(80,'','','天文学',7,10,'\0'),(81,'','','地质学',7,10,'\0'),(82,'','','地理科学',7,10,'\0'),(83,'','','大气科学',7,10,'\0'),(84,'','','海洋科学',7,10,'\0'),(85,'','','力学',7,10,'\0'),(86,'','','材料科学',7,10,'\0'),(87,'','','环境科学',7,10,'\0'),(88,'','','生态学',7,10,'\0'),(89,'','','心理学',7,10,'\0'),(90,'','','统计学',7,10,'\0'),(91,'','','测试',NULL,10,''),(92,'','','地矿类',8,10,'\0'),(93,'','','石油工程',8,10,'\0'),(94,'','','地质工程',8,10,'\0'),(95,'','','机械类',8,10,'\0'),(96,'','','能源动力类',8,10,'\0'),(97,'','','电气信息类',8,10,'\0'),(98,'','','通信工程',8,10,'\0'),(99,'','','建筑学',8,10,'\0'),(100,'','','土木工程',8,10,'\0'),(101,'','','制药工程',8,10,'\0'),(102,'','','航空航天类',8,10,'\0'),(103,'','','工程力学',8,10,'\0'),(104,'','','农学',10,10,'\0'),(105,'','','园艺',10,10,'\0'),(106,'','','森林资源',10,10,'\0'),(107,'','','水产',10,10,'\0'),(108,'','','动物科学',10,10,'\0'),(109,'','','基础医学',5,10,'\0'),(110,'','','预防医学',5,10,'\0'),(111,'','','麻醉学',5,10,'\0'),(112,'','','口腔医学',5,10,'\0'),(113,'','','中医学',5,10,'\0'),(114,'','','护理学',5,10,'\0'),(115,'','','药剂学',5,10,'\0'),(116,'','','针灸推拿学',5,10,'\0'),(117,'','','管理科学与工程',6,10,'\0'),(118,'','','信息管理与信息系统',6,10,'\0'),(119,'','','财务管理',6,10,'\0'),(120,'','','会计学',6,10,'\0'),(121,'','','工商管理',6,10,'\0'),(122,'','','市场营销',6,10,'\0'),(123,'','','人力资源',6,10,'\0'),(124,'','','物流管理',6,10,'\0'),(125,'','','英语',37,10,'\0'),(126,'','','四六级考试',37,10,'\0'),(127,'','','职称英语',37,10,'\0'),(128,'','','日语',37,10,'\0'),(129,'','','法语',37,10,'\0'),(130,'','','实用外语工具',37,10,'\0'),(131,'','','计算机辅助设计',35,11,'\0'),(132,'','','软件与程序设计',7,11,''),(133,'\0','','计算机软件与程序设计',35,11,'\0'),(134,'','','JAVA语言',133,11,'\0'),(135,'','','C语言',133,11,'\0'),(136,'','','C#语言',133,11,'\0'),(137,'','','C++语言',133,11,'\0'),(138,'','','海洋生物研究',9,11,''),(139,'','','海洋植物研究',9,11,''),(140,'','','海洋医学',9,11,''),(141,'\0','测试类型','测试012',NULL,11,''),(142,'','','测试011',141,11,''),(143,'','','12',141,11,'');
UNLOCK TABLES;
/*!40000 ALTER TABLE `booktype` ENABLE KEYS */;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL auto_increment,
  `content` longtext,
  `date` datetime default NULL,
  `delflag` bit(1) NOT NULL,
  `title` varchar(255) default NULL,
  `usefull` int(11) NOT NULL,
  `useless` int(11) NOT NULL,
  `bookid` int(11) default NULL,
  `orderid` varchar(16) default NULL,
  `userid` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK9BDE863F8CAD4566` (`bookid`),
  KEY `FK9BDE863F7DE9BEBE` (`orderid`),
  KEY `FK9BDE863FBB99195B` (`userid`),
  CONSTRAINT `FK9BDE863F7DE9BEBE` FOREIGN KEY (`orderid`) REFERENCES `orders` (`orderid`),
  CONSTRAINT `FK9BDE863F8CAD4566` FOREIGN KEY (`bookid`) REFERENCES `book` (`bookid`),
  CONSTRAINT `FK9BDE863FBB99195B` FOREIGN KEY (`userid`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

--
-- Dumping data for table `comment`
--


/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
LOCK TABLES `comment` WRITE;
INSERT INTO `comment` VALUES (4,'经典中的经典，值得反复琢磨。','2014-04-12 17:40:53','\0','经典永不过时',5,2,24,'1404051906581642',9),(5,'第二次买了。不错的书。值得珍藏！','2014-04-12 18:44:05','\0','不错',0,0,22,'1404051902283800',9),(6,'不错的书','2014-04-12 18:48:47','\0','不错',0,0,23,'1404120922589499',7),(7,'it\'svarygood!','2014-04-18 20:50:26','\0','notbad',1,0,14,'1404132320226019',7),(8,'不错的东西。八成新。','2014-04-26 23:43:02','\0','不错',0,0,13,'1404261828796518',7);
UNLOCK TABLES;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;

--
-- Table structure for table `contactinfo`
--

DROP TABLE IF EXISTS `contactinfo`;
CREATE TABLE `contactinfo` (
  `id` int(11) NOT NULL auto_increment,
  `address` varchar(100) NOT NULL,
  `mobile` varchar(11) default NULL,
  `phone` varchar(20) default NULL,
  `postalcode` varchar(6) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

--
-- Dumping data for table `contactinfo`
--


/*!40000 ALTER TABLE `contactinfo` DISABLE KEYS */;
LOCK TABLES `contactinfo` WRITE;
INSERT INTO `contactinfo` VALUES (1,'广东省广州市暨南大学金陵十三栋','13631395518','0597-3802126','510632'),(2,'暨南大学','13631395518','1234567','12'),(4,'暨南大学金陵十三栋514室','13631395518','0597-3802126','510632'),(5,'111','11111111','','11'),(6,'广东省广州市天河区暨南大学','13631395518','','510632');
UNLOCK TABLES;
/*!40000 ALTER TABLE `contactinfo` ENABLE KEYS */;

--
-- Table structure for table `empdate`
--

DROP TABLE IF EXISTS `empdate`;
CREATE TABLE `empdate` (
  `id` int(11) NOT NULL auto_increment,
  `activetime` varchar(50) default NULL,
  `content` longtext,
  `createtime` datetime default NULL,
  `remark` varchar(255) default NULL,
  `title` varchar(255) default NULL,
  `createemp` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK9F2ED47611185D81` (`createemp`),
  CONSTRAINT `FK9F2ED47611185D81` FOREIGN KEY (`createemp`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

--
-- Dumping data for table `empdate`
--


/*!40000 ALTER TABLE `empdate` DISABLE KEYS */;
LOCK TABLES `empdate` WRITE;
INSERT INTO `empdate` VALUES (6,'2014-04-16 09:45','测试','2014-04-15 19:36:53','可以为空','测试',1);
UNLOCK TABLES;
/*!40000 ALTER TABLE `empdate` ENABLE KEYS */;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
CREATE TABLE `groups` (
  `id` int(11) NOT NULL auto_increment,
  `authority` varchar(255) default NULL,
  `createdate` datetime default NULL,
  `name` varchar(50) default NULL,
  `remark` varchar(255) default NULL,
  `createrid` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKB63DD9D411188DC6` (`createrid`),
  CONSTRAINT `FKB63DD9D411188DC6` FOREIGN KEY (`createrid`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

--
-- Dumping data for table `groups`
--


/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
LOCK TABLES `groups` WRITE;
INSERT INTO `groups` VALUES (1,'111111111111111111111111','2014-04-16 15:36:09','超级管理员','',1),(7,'000011100000000000000000','2014-04-17 00:03:25','用户管理','管理网站用户',1),(9,'101100000000000000000000','2014-04-17 00:04:23','职工管理','',1),(10,'010000000010000000000000','2014-04-17 00:05:30','前台管理','管理前台广告和菜单排序',1),(11,'000000011100000000000000','2014-04-17 00:06:08','采购管理','',1),(12,'000000011101000000000000','2014-04-17 00:06:59','采购管理组长','比采购管理多了对已删除书籍的管理',1),(13,'000000000000011100000000','2014-04-17 00:08:15','订单审核员','审核用户订单',1),(14,'000000000000000111000000','2014-04-17 00:09:20','财务管理员','管理订单账款和确认订单收款',1),(15,'000000000000000101110000','2014-04-17 00:13:41','配货管理员','确认订单并配货',1),(16,'000000000000000100001000','2014-04-17 00:15:16','发货管理员','将已配好的订单打包发送',1),(17,'000000000000000000000100','2014-04-17 00:16:20','收货确认员','确认已发订单的收货处理',1),(18,'000000000000000100000010','2014-04-17 00:17:07','取消订单恢复','恢复已取消订单',1),(19,'100000000000111111111110','2014-04-17 00:18:14','财务组长','具有订单管理子系统的所有权限',1);
UNLOCK TABLES;
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int(11) NOT NULL auto_increment,
  `content` varchar(200) NOT NULL,
  `createtime` datetime default NULL,
  `username` varchar(20) NOT NULL,
  `orderid` varchar(16) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK9C2397E77DE9BEBE` (`orderid`),
  CONSTRAINT `FK9C2397E77DE9BEBE` FOREIGN KEY (`orderid`) REFERENCES `orders` (`orderid`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

--
-- Dumping data for table `message`
--


/*!40000 ALTER TABLE `message` DISABLE KEYS */;
LOCK TABLES `message` WRITE;
INSERT INTO `message` VALUES (1,'明天上午九点到十点向顾客确认订单。','2014-04-06 13:24:06','胡鸿廷','1404051906581642'),(2,'顾客已经确认订单。','2014-04-06 13:28:13','胡鸿廷','1404051900615096'),(3,'顾客已经确认订单。','2014-04-06 13:32:28','胡鸿廷','1404051906581642'),(4,'顾客预计三天后可以在线转账。','2014-04-07 09:53:31','胡鸿廷','1404070952384520'),(5,'未收到顾客账款，此订单将被取消。','2014-04-07 09:54:41','胡鸿廷','1404070952384520'),(6,'顾客拒收而取消订单。','2014-04-07 09:58:50','胡鸿廷','1404051900615096'),(7,'顾客要求恢复订单。','2014-04-07 09:59:33','胡鸿廷','1404070952384520'),(8,'订单已配货，正等待发货。','2014-04-07 10:30:01','胡鸿廷','1404051906581642'),(9,'库存不足，修改订单。','2014-04-10 07:30:17','胡鸿廷','1404022215872300'),(10,'已收部分账款33','2014-04-15 10:26:28','胡鸿廷','1404031910246027'),(11,'取消订单','2014-04-15 10:29:33','胡鸿廷','1404022215631001'),(12,'暂缓三天发货','2014-04-15 10:32:59','胡鸿廷','1404051902283800'),(13,'已收账款400元','2014-04-17 00:55:44','胡鸿廷','1404031909345410');
UNLOCK TABLES;
/*!40000 ALTER TABLE `message` ENABLE KEYS */;

--
-- Table structure for table `node`
--

DROP TABLE IF EXISTS `node`;
CREATE TABLE `node` (
  `id` int(11) NOT NULL auto_increment,
  `leaf` bit(1) NOT NULL,
  `nodeid` varchar(10) default NULL,
  `text` varchar(20) NOT NULL,
  `parentid` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK33AE0225331A00` (`parentid`),
  CONSTRAINT `FK33AE0225331A00` FOREIGN KEY (`parentid`) REFERENCES `node` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

--
-- Dumping data for table `node`
--


/*!40000 ALTER TABLE `node` DISABLE KEYS */;
LOCK TABLES `node` WRITE;
INSERT INTO `node` VALUES (1,'\0',NULL,'订单管理',NULL),(2,'\0',NULL,'书籍管理',NULL),(3,'\0',NULL,'前台管理',NULL),(4,'\0',NULL,'用户管理',NULL),(5,'\0',NULL,'员工管理',NULL),(6,'',NULL,'订单查询',1),(7,'',NULL,'等待审核订单',1),(8,'',NULL,'等待付款订单',1),(9,'',NULL,'等待配货订单',1),(10,'',NULL,'等待发货订单',1),(11,'',NULL,'已发货订单',1),(12,'',NULL,'已收货订单',1),(13,'',NULL,'已取消订单',1),(14,'',NULL,'书籍管理',2),(15,'',NULL,'书籍类别管理',2),(16,'',NULL,'出版社管理',2),(17,'',NULL,'广告管理',3),(18,'',NULL,'用户管理',4),(21,'',NULL,'在职员工管理',5),(24,'',NULL,'菜单排序',2),(25,'',NULL,'已禁用用户管理',4),(26,'',NULL,'离职员工管理',5),(27,'',NULL,'已删除书籍管理',2),(28,'',NULL,'员工查询',5),(29,'\0',NULL,'权限管理',NULL),(30,'',NULL,'权限组管理',29),(31,'',NULL,'评论管理',4);
UNLOCK TABLES;
/*!40000 ALTER TABLE `node` ENABLE KEYS */;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
  `id` int(11) NOT NULL auto_increment,
  `content` longtext,
  `createdate` datetime default NULL,
  `status` varchar(20) default NULL,
  `title` varchar(255) default NULL,
  `creater` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK2D45DD0B2ABC662B` (`creater`),
  CONSTRAINT `FK2D45DD0B2ABC662B` FOREIGN KEY (`creater`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

--
-- Dumping data for table `notification`
--


/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
LOCK TABLES `notification` WRITE;
INSERT INTO `notification` VALUES (4,'<font size=\"3\"><b>欢迎各位加入IShare.com大家庭！</b></font>','2014-04-15 20:13:28','IMPORTANT','IShare.com',11);
UNLOCK TABLES;
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;

--
-- Table structure for table `orderdeliverinfo`
--

DROP TABLE IF EXISTS `orderdeliverinfo`;
CREATE TABLE `orderdeliverinfo` (
  `id` int(11) NOT NULL auto_increment,
  `address` varchar(80) NOT NULL,
  `email` varchar(40) default NULL,
  `mobile` varchar(11) default NULL,
  `postalcode` varchar(6) default NULL,
  `tel` varchar(20) default NULL,
  `uid` int(11) default NULL,
  `name` varchar(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK8BFE2F65ED6FAB85` (`uid`),
  CONSTRAINT `FK8BFE2F65ED6FAB85` FOREIGN KEY (`uid`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

--
-- Dumping data for table `orderdeliverinfo`
--


/*!40000 ALTER TABLE `orderdeliverinfo` DISABLE KEYS */;
LOCK TABLES `orderdeliverinfo` WRITE;
INSERT INTO `orderdeliverinfo` VALUES (1,'广州市暨南大学','ten4and4ten@gmail.com','13631395518','364207','',7,'胡鸿廷'),(2,'福建省龙岩市上杭县','huht@live.cn','13631395518','123456','',7,'胡鸿廷'),(4,'广东省广州市天河区暨南大学金陵十三栋','huhting@qq.com','13631395518','123456','',9,'胡鸿廷'),(5,'福建省龙岩市上杭县','huht@live.cn','13631395518','364207','',1,'胡鸿廷'),(7,'暨南大学','huht@live.cn','13631395518','123456','',9,'胡鸿廷');
UNLOCK TABLES;
/*!40000 ALTER TABLE `orderdeliverinfo` ENABLE KEYS */;

--
-- Table structure for table `orderitem`
--

DROP TABLE IF EXISTS `orderitem`;
CREATE TABLE `orderitem` (
  `itemid` int(11) NOT NULL auto_increment,
  `amount` int(11) NOT NULL,
  `productName` varchar(50) NOT NULL,
  `productPrice` double NOT NULL,
  `productid` int(11) NOT NULL,
  `orderid` varchar(16) default NULL,
  `iscomment` bit(1) default NULL,
  PRIMARY KEY  (`itemid`),
  KEY `FK60163F617DE9BEBE` (`orderid`),
  CONSTRAINT `FK60163F617DE9BEBE` FOREIGN KEY (`orderid`) REFERENCES `orders` (`orderid`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

--
-- Dumping data for table `orderitem`
--


/*!40000 ALTER TABLE `orderitem` DISABLE KEYS */;
LOCK TABLES `orderitem` WRITE;
INSERT INTO `orderitem` VALUES (2,1,'量子力学：非相对论理论（第三版）',63,24,'1404022212257135','\0'),(3,2,'3DMAX建模与动画(普通高等院校“十二五”规划计算机辅助设计系列教材)',40,15,'1404022215631001','\0'),(4,1,'费恩曼物理学讲义（第一卷）',72,21,'1404022215872300','\0'),(5,1,'费恩曼物理学讲义（第二卷）',65,22,'1404022215872300','\0'),(6,1,'费恩曼物理学讲义（第三卷）',76,23,'1404022215872300','\0'),(7,1,'Java性能优化权威指南',81,1,'1404022218297981','\0'),(8,1,'Java性能优化权威指南',81,1,'1404031909345410','\0'),(9,1,'CAD工程制图--AutoCAD 2006（中文版）软件应用',70,14,'1404031909345410','\0'),(10,2,'人工智能：一种现代的方法(第3版)',106,8,'1404031909345410','\0'),(11,1,'算法心得：高效算法的奥秘(第二版)',66,3,'1404031909345410','\0'),(12,2,'精通ExtJS',45,4,'1404031909345410','\0'),(13,1,'量子力学：非相对论理论（第三版）',63,24,'1404031909345410','\0'),(14,1,'大学化学实验（第二版）',37,29,'1404031910246027','\0'),(15,1,'精通ExtJS',45,4,'1404051854616866','\0'),(16,1,'算法心得：高效算法的奥秘(第二版)',66,3,'1404051854616866','\0'),(17,1,'费恩曼物理学讲义（第三卷）',76,23,'1404051900615096','\0'),(18,1,'费恩曼物理学讲义（第二卷）',65,22,'1404051902283800',''),(19,1,'费恩曼物理学讲义（第一卷）',72,21,'1404051905710150','\0'),(20,1,'量子力学：非相对论理论（第三版）',63,24,'1404051906581642',''),(21,1,'概率论、马尔科夫链、排队和模拟',111,20,'1404070952384520','\0'),(22,1,'量子力学：非相对论理论（第三版）',63,24,'1404070952384520','\0'),(23,1,'精通ExtJS',45,4,'1404100727493556','\0'),(24,2,'费恩曼物理学讲义（第一卷）',72,21,'1404051905710150','\0'),(25,2,'费恩曼物理学讲义（第三卷）',76,23,'1404101026697371','\0'),(26,1,'费恩曼物理学讲义（第三卷）',76,23,'1404120922589499',''),(27,1,'CAD工程制图--AutoCAD 2006（中文版）软件应用',70,14,'1404132320226019',''),(28,1,'量子力学：非相对论理论（第三版）',63,24,'1404141933010250','\0'),(29,1,'费恩曼物理学讲义（第三卷）',76,23,'1404182048040317','\0'),(30,2,'Java性能优化权威指南',81,1,'1404182103623424','\0'),(31,1,'Java性能优化权威指南',81,1,'1404182110965727','\0'),(32,1,'物理动理学（第二版）',34,25,'1404182112290654','\0'),(33,2,'人工智能：一种现代的方法(第3版)',106,8,'1404190901534016','\0'),(34,1,'CAD工程制图--AutoCAD 2006（中文版）软件应用',70,14,'1404190901534016','\0'),(35,1,'费恩曼物理学讲义（第三卷）',76,23,'1404232040160263','\0'),(36,1,'JavaScript框架设计',45,30,'1404232045587240','\0'),(37,1,'JavaScript框架开发',45,31,'1404232045188699','\0'),(38,2,'大学化学实验（第二版）',37,29,'1404251536531331','\0'),(39,1,'大学化学实验（第二版）',37,29,'1404251537967452','\0'),(40,1,'大学化学实验（第二版）',37,29,'1404251543515779','\0'),(41,1,'大学化学实验（第二版）',37,29,'1404251544448099','\0'),(42,2,'大学化学实验（第二版）',37,29,'1404251546364177','\0'),(43,1,'大学化学实验（第二版）',37,29,'1404251547926737','\0'),(44,1,'大学化学实验（第二版）',37,29,'1404251548113897','\0'),(45,1,'大学化学实验（第二版）',37,29,'1404251550384443','\0'),(46,3,'大学化学实验（第二版）',37,29,'1404251550158660','\0'),(47,1,'大学化学实验（第二版）',37,29,'1404251556058656','\0'),(48,1,'大学化学实验（第二版）',37,29,'1404251557180281','\0'),(49,1,'大学化学实验（第二版）',37,29,'1404251558046987','\0'),(50,2,'大学化学实验（第二版）',37,29,'1404251559136548','\0'),(51,1,'大学化学实验（第二版）',37,29,'1404251559259359','\0'),(52,1,'最强大脑：77招让你成为脑力最好的人',23,6,'1404261659801954','\0'),(53,1,'系统建模与仿真',68,11,'1404261825724110','\0'),(54,1,'ERP系统原理与实施',30,12,'1404261825575802','\0'),(55,1,'CAD工程制图--AutoCAD 2006（中文版）软件应用',70,14,'1404261826265793','\0'),(56,1,'数字图像处理（MATLAB版）',37,13,'1404261828796518','');
UNLOCK TABLES;
/*!40000 ALTER TABLE `orderitem` ENABLE KEYS */;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `orderid` varchar(16) NOT NULL,
  `createdate` datetime NOT NULL,
  `deliverway` varchar(23) NOT NULL,
  `employee` varchar(20) default NULL,
  `note` varchar(100) default NULL,
  `paymentway` varchar(20) NOT NULL,
  `paymentstate` bit(1) NOT NULL,
  `producttotalprice` float NOT NULL,
  `state` varchar(16) NOT NULL,
  `totalprice` float NOT NULL,
  `userid` int(11) NOT NULL,
  `deliverid` int(11) NOT NULL,
  `epay` float default NULL,
  `fapiao` varchar(255) default NULL,
  `alreadypay` double default NULL,
  `payable` double default NULL,
  PRIMARY KEY  (`orderid`),
  KEY `FKC3DF62E5B59F78AF` (`deliverid`),
  KEY `FKC3DF62E5BB99195B` (`userid`),
  CONSTRAINT `FKC3DF62E5B59F78AF` FOREIGN KEY (`deliverid`) REFERENCES `orderdeliverinfo` (`id`),
  CONSTRAINT `FKC3DF62E5BB99195B` FOREIGN KEY (`userid`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

--
-- Dumping data for table `orders`
--


/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
LOCK TABLES `orders` WRITE;
INSERT INTO `orders` VALUES ('1404022212257135','2014-04-02 22:12:04','ZHAIJISONG',NULL,'','CASH','\0',63,'WAITCONFIRM',81,7,1,0,NULL,0,0),('1404022215631001','2014-04-02 22:15:09','ZHAIJISONG',NULL,'','CASH','\0',80,'CANCEL',98,7,1,0,NULL,0,98),('1404022215872300','2014-04-02 22:15:38','ZHAIJISONG',NULL,'','CASH','\0',213,'WAITDELIVER',231,7,1,0,NULL,0,0),('1404022218297981','2014-04-02 22:18:14','ZHAIJISONG',NULL,'','ONLINE','\0',81,'CANCEL',99,7,1,0,NULL,0,99),('1404031909345410','2014-04-03 19:09:23','ZHAIJISONG',NULL,'','HUIKUAN','\0',582,'CANCEL',600,7,2,0,NULL,0,600),('1404031910246027','2014-04-03 19:10:28','ZHAIJISONG',NULL,'','ZHUANZHANG','\0',37,'CANCEL',55,7,2,0,NULL,55,0),('1404051854616866','2014-04-05 18:54:45','EMS',NULL,'    抬头：个人    内容：图书    附加信息：无','CASH','\0',111,'CANCEL',149,9,4,0,'    抬头：个人    内容：图书    附加信息：无',0,0),('1404051900615096','2014-04-05 19:00:08','ZHAIJISONG',NULL,'','CASH','\0',76,'CANCEL',94,9,4,0,NULL,0,0),('1404051902283800','2014-04-05 19:02:05','ZHAIJISONG',NULL,'','CASH','\0',65,'WAITDELIVER',83,9,4,0,NULL,0,0),('1404051905710150','2014-04-05 19:05:14','ZHAIJISONG',NULL,'','CASH','\0',72,'CANCEL',90,9,4,0,NULL,0,0),('1404051906581642','2014-04-05 19:06:27','ZHAIJISONG',NULL,'','CASH','\0',63,'CANCEL',81,9,4,0,NULL,0,81),('1404070952384520','2014-04-07 09:52:02','TEKUAIZHUANDI',NULL,'','ONLINE','\0',174,'RECEIVED',194,1,5,0,NULL,0,0),('1404100727493556','2014-04-10 07:27:37','PINGYOU',NULL,'','ONLINE','\0',45,'CANCEL',50,7,1,0,NULL,0,50),('1404101015037132','2014-04-10 10:15:01','ZHAIJISONG',NULL,'','CASH','\0',144,'CANCEL',162,9,4,0,NULL,0,0),('1404101024507131','2014-04-10 10:24:29','ZHAIJISONG',NULL,'','CASH','\0',0,'CANCEL',18,9,4,0,NULL,0,0),('1404101026697371','2014-04-10 10:26:39','ZHAIJISONG',NULL,'','CASH','\0',152,'CANCEL',170,7,1,0,NULL,0,0),('1404120922589499','2014-04-12 09:22:33','ZHAIJISONG',NULL,'因库存不足，本订单替代订单号为1404101026697371的订单。','CASH','\0',76,'CANCEL',94,7,1,0,NULL,0,94),('1404132320226019','2014-04-13 23:20:39','ZHAIJISONG',NULL,NULL,'CASH','\0',70,'RECEIVED',88,7,2,0,NULL,88,0),('1404141933010250','2014-04-14 19:33:29','ZHAIJISONG',NULL,NULL,'CASH','\0',63,'WAITCONFIRM',81,7,2,10,NULL,10,71),('1404182048040317','2014-04-18 20:48:17','ZHAIJISONG',NULL,NULL,'CASH','\0',76,'WAITCONFIRM',94,7,2,0,NULL,0,94),('1404182103623424','2014-04-18 21:03:06','ZHAIJISONG',NULL,NULL,'CASH','\0',162,'WAITCONFIRM',180,7,2,0,NULL,0,180),('1404182110965727','2014-04-18 21:10:00','ZHAIJISONG',NULL,NULL,'CASH','\0',81,'WAITCONFIRM',99,7,2,0,NULL,0,99),('1404182112290654','2014-04-18 21:12:42','ZHAIJISONG',NULL,NULL,'CASH','\0',34,'WAITCONFIRM',52,7,2,0,NULL,0,52),('1404190901534016','2014-04-19 09:01:43','ZHAIJISONG',NULL,NULL,'CASH','\0',282,'CANCEL',300,7,2,0,NULL,0,300),('1404232040160263','2014-04-23 20:40:58','ZHAIJISONG',NULL,NULL,'CASH','\0',76,'WAITCONFIRM',94,9,7,0,NULL,0,94),('1404232045188699','2014-04-23 20:45:51','ZHAIJISONG',NULL,NULL,'ZHUANZHANG','\0',45,'CANCEL',63,9,7,0,'    抬头：个人    内容：图书    附加信息：无',0,63),('1404232045587240','2014-04-23 20:45:00','ZHAIJISONG',NULL,NULL,'CASH','\0',45,'WAITCONFIRM',63,9,7,0,NULL,0,63),('1404251536531331','2014-04-25 15:36:44','ZHAIJISONG',NULL,'因库存不足，本订单替代订单号为1404031910246027的订单。','ZHUANZHANG','\0',74,'CANCEL',92,7,2,0,NULL,0,92),('1404251537967452','2014-04-25 15:37:58','ZHAIJISONG',NULL,'因库存不足，本订单替代订单号为1404251536531331的订单。','ZHUANZHANG','\0',37,'CANCEL',55,7,2,0,NULL,0,55),('1404251543515779','2014-04-25 15:43:20','ZHAIJISONG',NULL,'因库存不足，本订单替代订单号为1404251537967452的订单。','ZHUANZHANG','\0',37,'CANCEL',55,7,2,0,NULL,0,55),('1404251544448099','2014-04-25 15:44:50','ZHAIJISONG',NULL,'因库存不足，本订单替代订单号为1404251543515779的订单。','ZHUANZHANG','\0',37,'CANCEL',55,7,2,0,NULL,0,55),('1404251546364177','2014-04-25 16:06:23','ZHAIJISONG',NULL,'因库存不足，本订单替代订单号为1404251544448099的订单。','ZHUANZHANG','\0',74,'WAITDELIVER',92,7,2,0,NULL,92,92),('1404251547926737','2014-04-25 15:47:45','ZHAIJISONG',NULL,'因库存不足，本订单替代订单号为1404251546364177的订单。','ZHUANZHANG','\0',37,'CANCEL',55,7,2,0,NULL,0,55),('1404251548113897','2014-04-25 15:48:58','ZHAIJISONG',NULL,'因库存不足，本订单替代订单号为1404251547926737的订单。','ZHUANZHANG','\0',37,'CANCEL',55,7,2,0,NULL,0,55),('1404251550158660','2014-04-25 15:50:55','ZHAIJISONG',NULL,'因库存不足，本订单替代订单号为1404251548113897的订单。','ZHUANZHANG','\0',111,'CANCEL',129,7,2,0,NULL,0,129),('1404251550384443','2014-04-25 15:50:49','ZHAIJISONG',NULL,'因库存不足，本订单替代订单号为1404251548113897的订单。','ZHUANZHANG','\0',37,'CANCEL',55,7,2,0,NULL,0,55),('1404251556058656','2014-04-25 15:56:14','ZHAIJISONG',NULL,'因库存不足，本订单替代订单号为1404251550384443的订单。','ZHUANZHANG','\0',37,'CANCEL',55,7,2,0,NULL,0,55),('1404251557180281','2014-04-25 15:57:28','ZHAIJISONG',NULL,'因库存不足，本订单替代订单号为1404251556058656的订单。','ZHUANZHANG','\0',37,'CANCEL',55,7,2,0,NULL,0,55),('1404251558046987','2014-04-25 15:58:38','ZHAIJISONG',NULL,'因库存不足，本订单替代订单号为1404251557180281的订单。','ZHUANZHANG','\0',37,'CANCEL',55,7,2,0,NULL,0,55),('1404251559136548','2014-04-25 15:59:17','ZHAIJISONG',NULL,'因库存不足，本订单替代订单号为1404251558046987的订单。','ZHUANZHANG','\0',74,'CANCEL',92,7,2,0,NULL,0,92),('1404251559259359','2014-04-25 15:59:34','ZHAIJISONG',NULL,'因库存不足，本订单替代订单号为1404251559136548的订单。','ZHUANZHANG','\0',37,'CANCEL',55,7,2,0,NULL,0,55),('1404251559827466','2014-04-25 15:59:43','ZHAIJISONG',NULL,'因库存不足，本订单替代订单号为1404251559259359的订单。','ZHUANZHANG','\0',0,'CANCEL',18,7,2,0,NULL,0,18),('1404261659801954','2014-04-26 16:59:50','ZHAIJISONG',NULL,NULL,'CASH','\0',23,'CANCEL',41,7,2,0,NULL,0,41),('1404261825575802','2014-04-26 18:25:39','ZHAIJISONG',NULL,NULL,'CASH','\0',30,'WAITCONFIRM',48,7,2,0,NULL,0,48),('1404261825724110','2014-04-26 18:25:12','ZHAIJISONG',NULL,NULL,'CASH','\0',68,'WAITCONFIRM',86,7,2,0,NULL,0,86),('1404261826265793','2014-04-26 18:26:18','ZHAIJISONG',NULL,NULL,'CASH','\0',70,'WAITCONFIRM',88,7,2,0,NULL,0,88),('1404261828796518','2014-04-26 18:28:29','ZHAIJISONG',NULL,NULL,'CASH','\0',37,'RECEIVED',55,7,2,90,NULL,55,-35);
UNLOCK TABLES;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;

--
-- Table structure for table `picture`
--

DROP TABLE IF EXISTS `picture`;
CREATE TABLE `picture` (
  `id` int(11) NOT NULL auto_increment,
  `isshow` bit(1) NOT NULL,
  `name` varchar(50) default NULL,
  `note` varchar(255) default NULL,
  `uptime` datetime default NULL,
  `url` varchar(100) NOT NULL,
  `action` varchar(255) default NULL,
  `path` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

--
-- Dumping data for table `picture`
--


/*!40000 ALTER TABLE `picture` DISABLE KEYS */;
LOCK TABLES `picture` WRITE;
INSERT INTO `picture` VALUES (1,'','图片1','','2014-04-07 09:50:07','/resources/images/show/show_1396835407629.jpg','javascript:void(0);','D:\\绿色软件\\tomcat6.0.36\\webapps\\ROOT\\WEB-INF\\resources\\images\\show\\show_1396835407629.jpg'),(2,'','图片2','','2014-04-07 09:50:18','/resources/images/show/show_1396835418162.jpg','javascript:void(0);','D:\\绿色软件\\tomcat6.0.36\\webapps\\ROOT\\WEB-INF\\resources\\images\\show\\show_1396835418162.jpg'),(3,'','图片3','','2014-04-07 09:50:29','/resources/images/show/show_1396835429721.jpg','javascript:void(0);','D:\\绿色软件\\tomcat6.0.36\\webapps\\ROOT\\WEB-INF\\resources\\images\\show\\show_1396835429721.jpg'),(4,'','图片4','','2014-04-09 22:17:38','/resources/images/show/show_1397053058134.jpg','/view/book/show?id=1','D:\\绿色软件\\tomcat6.0.36\\webapps\\ROOT\\WEB-INF\\resources\\images\\show\\show_1397053058134.jpg');
UNLOCK TABLES;
/*!40000 ALTER TABLE `picture` ENABLE KEYS */;

--
-- Table structure for table `press`
--

DROP TABLE IF EXISTS `press`;
CREATE TABLE `press` (
  `pressid` int(11) NOT NULL auto_increment,
  `deleted` bit(1) default NULL,
  `pressname` varchar(100) NOT NULL,
  `remark` longtext,
  PRIMARY KEY  (`pressid`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

--
-- Dumping data for table `press`
--


/*!40000 ALTER TABLE `press` DISABLE KEYS */;
LOCK TABLES `press` WRITE;
INSERT INTO `press` VALUES (1,'\0','清华大学出版社',''),(2,'\0','北京大学出版社',''),(3,'\0','机械工业出版社',''),(4,'\0','邮电大学出版社',''),(5,'\0','高等教育出版社',''),(6,'\0','人民大学出版',''),(7,'\0','暨南大学出版社',''),(8,'\0','人民邮电出版社',''),(9,'\0','电子工业出版社',''),(10,'\0','华中科技大学出版社',''),(11,'\0','世界图书出版社',''),(12,'\0','上海科学技术出版社',''),(13,'\0','科学出版社',''),(14,'\0','复旦大学出版社','');
UNLOCK TABLES;
/*!40000 ALTER TABLE `press` ENABLE KEYS */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL auto_increment,
  `email` varchar(50) default NULL,
  `name` varchar(50) NOT NULL,
  `note` longtext,
  `password` varchar(100) NOT NULL,
  `regtime` datetime NOT NULL,
  `usertype` int(11) NOT NULL,
  `contactid` int(11) default NULL,
  `accountid` int(11) default NULL,
  `lastlogin` datetime default NULL,
  `deleted` bit(1) default NULL,
  `groupid` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK36EBCB9E2ECF7F` (`contactid`),
  KEY `FK36EBCB3E409E8B` (`accountid`),
  KEY `FK36EBCB2E1D8873` (`groupid`),
  CONSTRAINT `FK36EBCB2E1D8873` FOREIGN KEY (`groupid`) REFERENCES `groups` (`id`),
  CONSTRAINT `FK36EBCB3E409E8B` FOREIGN KEY (`accountid`) REFERENCES `account` (`id`),
  CONSTRAINT `FK36EBCB9E2ECF7F` FOREIGN KEY (`contactid`) REFERENCES `contactinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

--
-- Dumping data for table `user`
--


/*!40000 ALTER TABLE `user` DISABLE KEYS */;
LOCK TABLES `user` WRITE;
INSERT INTO `user` VALUES (1,'huht@live.cn','胡鸿廷','超级管理员','0B8FFE1239AEC898A33D9D65E131BE25','2014-03-07 15:55:13',2,4,NULL,NULL,'\0',1),(7,'ten4and4ten@gmail.com','huht','','D365CB2CD80884A9660E5DBDF0AA76D9','2014-03-24 14:58:52',0,1,1,'2014-04-27 12:58:53','\0',NULL),(9,'huhting@qq.com','huhongting','没有说明^_^','C3705EF47007126343D49E7157E3BFA8','2014-04-03 19:40:26',0,2,2,'2014-04-26 18:20:43','\0',NULL),(10,'2318231678@qq.com','测试01',NULL,'6775C0F33E6FB1541097436D77E48301','2014-04-08 16:08:14',1,5,5,NULL,'\0',10),(11,'sh.huht@gmail.com','admin.ishare.com','默认系统管理员','962FBD67E05FFF4AD971AE09F339A921','2014-04-15 18:57:58',2,6,8,NULL,'\0',1),(12,'h@h.com','hh',NULL,'B604366D3EBD3C408019DCF51F37F5C9','2014-04-19 11:19:08',0,NULL,9,'2014-04-19 11:19:19','',NULL);
UNLOCK TABLES;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

