-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: localhost    Database: cms
-- ------------------------------------------------------
-- Server version	8.0.23

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `cID` int(5) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `cName` varchar(45) NOT NULL,
  `cNIC` varchar(12) NOT NULL,
  `cAddress` varchar(100) DEFAULT NULL,
  `cPhone` varchar(12) NOT NULL,
  PRIMARY KEY (`cID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (00001,'U. Liyanage','960042247V','123, Malabe Sri Lanka','0779211001'),(00003,'I. S Fernando','995641236V','123, Malabe Sri Lanka','0771122365');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `designation`
--

DROP TABLE IF EXISTS `designation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `designation` (
  `dID` int(4) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `dName` varchar(45) DEFAULT NULL,
  `dDescription` varchar(100) DEFAULT NULL,
  `dStatus` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`dID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `designation`
--

LOCK TABLES `designation` WRITE;
/*!40000 ALTER TABLE `designation` DISABLE KEYS */;
INSERT INTO `designation` VALUES (0002,'Designer','Fassion Designer','Active'),(0003,'Cashier','Sales Cashier','Active'),(0004,'Pattern Checker','Pattern Checker','Active');
/*!40000 ALTER TABLE `designation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `eID` int(3) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `eName` varchar(45) DEFAULT NULL,
  `eNIC` varchar(12) DEFAULT NULL,
  `eContactNo` varchar(10) DEFAULT NULL,
  `eDesignation` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`eID`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (001,'Uthpala Liyanage','960042247V','779211001','Cashier'),(003,'kasun madawa','658577897v','713457897','Designer'),(004,'Srimal jayapala','432776809v','708764763','Pattern Checker'),(007,'Kusuma perera','625677870v','786678662','Pattern Checker'),(010,'Ranjith','642233145V','332244557','Designer'),(011,'Siripala Perera','942356789V','442356897','Pattern Checker');
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderitem`
--

DROP TABLE IF EXISTS `orderitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orderitem` (
  `oIID` int NOT NULL AUTO_INCREMENT,
  `oIOID` int(5) unsigned zerofill NOT NULL,
  `oIPID` int(5) unsigned zerofill NOT NULL,
  `oIPQty` int DEFAULT NULL,
  `oIUPrice` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`oIID`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderitem`
--

LOCK TABLES `orderitem` WRITE;
/*!40000 ALTER TABLE `orderitem` DISABLE KEYS */;
INSERT INTO `orderitem` VALUES (1,00001,00001,1,'20.0'),(2,00001,00001,3,'20.0'),(3,00002,00001,2,'20.0'),(4,00003,00001,1,'20.0'),(5,00004,00001,2,'20.0'),(6,00005,00001,1,'20.0'),(7,00006,00001,1,'20.0'),(8,00006,00001,2,'20.0'),(9,00007,00001,2,'20.0'),(10,00008,00001,1,'20.0');
/*!40000 ALTER TABLE `orderitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderlist`
--

DROP TABLE IF EXISTS `orderlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orderlist` (
  `oID` int(5) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `ocID` int(5) unsigned zerofill DEFAULT NULL,
  `oNotes` varchar(400) NOT NULL DEFAULT '"Not Specified"',
  `oDate` varchar(45) DEFAULT NULL,
  `oStatus` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`oID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderlist`
--

LOCK TABLES `orderlist` WRITE;
/*!40000 ALTER TABLE `orderlist` DISABLE KEYS */;
INSERT INTO `orderlist` VALUES (00001,00001,'Not Specified','2021-10-08','Processing'),(00002,00001,'Not Specified','2021-10-07','Pending'),(00003,00001,'Not Specified','2021-10-07','Pending'),(00004,00001,'Not Specified','2021-10-07','Pending'),(00005,00001,'Not Specified','2021-10-08','Pending'),(00006,00001,'Not Specified','2021-10-08','Pending'),(00007,00001,'Not Specified','2021-10-08','Pending'),(00008,00001,'Not Specified','2021-10-08','Pending');
/*!40000 ALTER TABLE `orderlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `pID` int(5) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `pName` varchar(45) DEFAULT NULL,
  `pInfo` varchar(100) DEFAULT NULL,
  `pQuantity` int DEFAULT NULL,
  `pPrice` double DEFAULT NULL,
  `pAvailability` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`pID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (00001,'Collar buttons (Color : Black)','Black t shirt button with nilon design (Stock price *1)',2,20,'Available');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier` (
  `sID` int(5) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `sName` varchar(45) DEFAULT NULL,
  `sNIC` varchar(12) DEFAULT NULL,
  `sEmail` varchar(45) DEFAULT NULL,
  `sAddress` varchar(100) DEFAULT NULL,
  `sPhoneNumber` varchar(12) DEFAULT NULL,
  `sType` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`sID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier`
--

LOCK TABLES `supplier` WRITE;
/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
INSERT INTO `supplier` VALUES (00001,'U. D Liyanage','954212354V','ud@gmail.com','123, Gampaha, sri Lanka','0112233564','Ready Made'),(00002,'D. P Liyanage','965642136V','dp@gmail.com','1234, Gampaha, Sri Lanka','0334455698','Ready Made'),(00003,'I. H Liyanaga','990045896V','IH@gmail.com','123, Gampaha, Sri Lanka','0112233654','Raw & Ready Made'),(00006,'ABC Distributers','984212354V','gp@gmail.com','123, Biyagama Sri Lanka','0112233565','Raw Material');
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `uEmail` varchar(45) NOT NULL,
  `uFName` varchar(45) DEFAULT NULL,
  `uLName` varchar(45) DEFAULT NULL,
  `uPassword` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`uEmail`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('chamindu@gmail.com','chamindu','matheesha','202cb962ac59075b964b07152d234b70'),('pathum@gmail.com','pathum','Nirmal','827ccb0eea8a706c4c34a16891f84e7b'),('ud@yahoo.com','Dulshana','Liyanage','202cb962ac59075b964b07152d234b70');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-10-08 23:08:44
