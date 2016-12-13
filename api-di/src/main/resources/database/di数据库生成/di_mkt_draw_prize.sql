-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: dev-yunsoo-di.cquafg9deiuc.rds.cn-north-1.amazonaws.com.cn    Database: di
-- ------------------------------------------------------
-- Server version	5.6.27-log

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
-- Table structure for table `mkt_draw_prize`
--

DROP TABLE IF EXISTS `mkt_draw_prize`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mkt_draw_prize` (
  `draw_record_id` char(19) NOT NULL,
  `product_key` varchar(22) DEFAULT NULL,
  `scan_record_id` char(19) NOT NULL,
  `marketing_id` char(19) NOT NULL,
  `draw_rule_id` char(19) NOT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `prize_type_code` varchar(20) DEFAULT NULL,
  `status_code` varchar(20) DEFAULT NULL,
  `created_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `paid_datetime` datetime DEFAULT NULL,
  `account_type` varchar(20) DEFAULT NULL,
  `prize_account` varchar(50) DEFAULT NULL,
  `prize_account_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `prize_content` varchar(50) DEFAULT NULL,
  `prize_contact_id` char(19) DEFAULT NULL,
  `comments` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`draw_record_id`),
  KEY `idx_scan_record` (`scan_record_id`),
  KEY `idx_mkt_rule` (`marketing_id`,`draw_rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-12-13 13:56:43
