CREATE DATABASE  IF NOT EXISTS `we_pc` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `we_pc`;
-- MySQL dump 10.13  Distrib 8.0.34, for macos13 (arm64)
--
-- Host: 127.0.0.1    Database: we_pc
-- ------------------------------------------------------
-- Server version	8.0.35

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
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointment` (
  `appointment_id` int NOT NULL AUTO_INCREMENT,
  `appointment_datetime` datetime NOT NULL,
  `appointment_description` varchar(255) DEFAULT NULL,
  `patient_id` int NOT NULL,
  `clinic_id` int NOT NULL,
  `show_up` tinyint(1) NOT NULL DEFAULT '0',
  `vet_id` char(10) NOT NULL,
  `room_id` int DEFAULT NULL,
  PRIMARY KEY (`appointment_id`),
  UNIQUE KEY `appointment_datetime` (`appointment_datetime`,`patient_id`,`clinic_id`),
  KEY `patient_id` (`patient_id`),
  KEY `clinic_id` (`clinic_id`),
  KEY `vet_id` (`vet_id`),
  KEY `room_id` (`room_id`),
  CONSTRAINT `appointment_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `pet` (`patient_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `appointment_ibfk_2` FOREIGN KEY (`clinic_id`) REFERENCES `pet_clinic` (`clinic_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `appointment_ibfk_3` FOREIGN KEY (`vet_id`) REFERENCES `veterinarian` (`vet_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `appointment_ibfk_4` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
INSERT INTO `appointment` VALUES (1,'2023-11-03 09:00:00','Annual checkup',3,1,1,'01emp00008',1),(2,'2023-11-30 14:30:00','Vaccination',6,2,1,'02emp00009',9),(3,'2023-12-01 11:00:00','Checkup',7,3,1,'03emp00006',15),(4,'2023-12-02 10:30:00','Surgery consultation',1,4,1,'04emp00001',22),(5,'2023-12-05 16:30:00','Emergency treatment',1,5,1,'05emp00010',28),(6,'2023-12-05 13:00:00','Checkup',5,6,1,'06emp00005',36),(7,'2023-12-05 08:00:00','Checkup',8,2,0,'02emp00003',NULL),(8,'2023-12-20 09:00:00','Checkup',8,2,0,'02emp00003',NULL),(9,'2011-12-23 12:00:00','wound',3,5,1,'05emp00010',29),(10,'2022-11-10 12:00:00','eye issue',7,5,1,'05emp00010',31),(11,'2022-12-20 11:30:00','leg wound',5,5,1,'05emp00010',30);
/*!40000 ALTER TABLE `appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointment_medication`
--

DROP TABLE IF EXISTS `appointment_medication`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointment_medication` (
  `appointment_id` int NOT NULL,
  `medication_name` varchar(255) NOT NULL,
  `quantity` int NOT NULL,
  `time_interval_days` int DEFAULT '1',
  PRIMARY KEY (`appointment_id`,`medication_name`),
  KEY `medication_name` (`medication_name`),
  CONSTRAINT `appointment_medication_ibfk_1` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`appointment_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `appointment_medication_ibfk_2` FOREIGN KEY (`medication_name`) REFERENCES `medication` (`medication_name`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment_medication`
--

LOCK TABLES `appointment_medication` WRITE;
/*!40000 ALTER TABLE `appointment_medication` DISABLE KEYS */;
INSERT INTO `appointment_medication` VALUES (1,'amoxicillin',1,1),(1,'lufenuron',2,1),(1,'methimazole',2,1),(1,'tramadol',10,12),(2,'methocarbamol',6,1),(2,'pyrantel',2,1),(3,'hydroxyzine',2,1),(3,'tramadol',1,1),(5,'sucralfate',1,1),(6,'dexamethasone',1,1),(6,'hydroxyzine',2,1),(6,'oxibendazole',1,1);
/*!40000 ALTER TABLE `appointment_medication` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `available_treatment_in_clinic`
--

DROP TABLE IF EXISTS `available_treatment_in_clinic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `available_treatment_in_clinic` (
  `service_id` int NOT NULL,
  `clinic_id` int NOT NULL,
  PRIMARY KEY (`service_id`,`clinic_id`),
  KEY `clinic_id` (`clinic_id`),
  CONSTRAINT `available_treatment_in_clinic_ibfk_1` FOREIGN KEY (`clinic_id`) REFERENCES `pet_clinic` (`clinic_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `available_treatment_in_clinic_ibfk_2` FOREIGN KEY (`service_id`) REFERENCES `treatment_service` (`service_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `available_treatment_in_clinic`
--

LOCK TABLES `available_treatment_in_clinic` WRITE;
/*!40000 ALTER TABLE `available_treatment_in_clinic` DISABLE KEYS */;
INSERT INTO `available_treatment_in_clinic` VALUES (1,1),(4,1),(5,1),(6,1),(7,1),(8,1),(9,1),(10,1),(11,1),(14,1),(15,1),(16,1),(18,1),(19,1),(20,1),(21,1),(1,2),(4,2),(6,2),(7,2),(8,2),(9,2),(11,2),(14,2),(16,2),(18,2),(19,2),(20,2),(1,3),(2,3),(3,3),(6,3),(7,3),(8,3),(10,3),(11,3),(14,3),(16,3),(19,3),(20,3),(21,3),(5,4),(6,4),(7,4),(8,4),(9,4),(10,4),(11,4),(14,4),(15,4),(16,4),(18,4),(19,4),(20,4),(4,5),(5,5),(6,5),(7,5),(8,5),(9,5),(14,5),(15,5),(16,5),(18,5),(19,5),(21,5),(1,6),(2,6),(3,6),(4,6),(5,6),(6,6),(14,6),(15,6),(16,6),(18,6),(19,6),(20,6),(21,6);
/*!40000 ALTER TABLE `available_treatment_in_clinic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `emp_id` char(10) NOT NULL,
  `id_password` varchar(12) NOT NULL,
  `phone` varchar(13) DEFAULT NULL,
  `emp_last_name` varchar(20) NOT NULL,
  `emp_first_name` varchar(40) NOT NULL,
  `street_number` int NOT NULL,
  `street_name` varchar(64) NOT NULL,
  `town` varchar(64) NOT NULL,
  `state_abbrev` char(2) NOT NULL,
  `zip` char(5) NOT NULL,
  `emp_type` varchar(50) NOT NULL,
  `clinic_id` int DEFAULT NULL,
  PRIMARY KEY (`emp_id`),
  UNIQUE KEY `street_number` (`street_number`,`street_name`,`town`,`state_abbrev`,`zip`),
  UNIQUE KEY `phone` (`phone`),
  KEY `clinic_id` (`clinic_id`),
  CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`clinic_id`) REFERENCES `pet_clinic` (`clinic_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES ('01emp00008','kitty0220me','(337)555-9441','Hardy','Thomas',1877,'Ete Ct','Frogtown','LA','70563','veterinarian',1),('01emp00011','jaych08','(425)255-2578','Johnason','Jessica',200,'N Spring St','EggTown','LA','70063','administrative',1),('02emp00002','crazy1002','(351)555-1219','Nixon','Liz',722,'DaVinci Blvd.','Concord','MA','01742','medical director',2),('02emp00003','newmoon95','(617)111-3322','Wilson','Fran',95,'Park Driv.','Boston','MA','02215','veterinarian',2),('02emp00009','daydream02','(607)323-5587','Phillips','Rene',18,'Haviland St.','Boston','MA','02115','veterinarian',2),('02emp00012','nobody3','(585)635-7357','Cheng','Li',333,'Washington Street','Brookline','MA','02445','administrative',2),('03emp00006','007nextday','(206)555-4112','Nagy','Karl',305,'14th Ave. S. Suite 3B','Seattle','WA','98128','veterinarian',3),('03emp00007','avatar1pet','(509)555-7969','Steel','John',12,'Orchestra Terrace','Walla Walla','WA','99362','administrative',3),('04emp00001','maroon5','(765)555-7878','Lee','Chris',345,'Winchell Pl','Anderson','IN','46014','veterinarian',4),('04emp00013','rere2023','(715)755-1368','Swift','Taylor',111,'W. Washington St.','Indy','IN','46014','administrative',4),('05emp00010','dogdd34','(469)555-8828','Chelan','Donna',2299,'E Baylor Dr','Dallas','TX','75224','veterinarian',5),('05emp00014','coolpet','(722)145-9467','Khoury','Joey',123,'PO Box 12874','Austin','TX','72224','administrative',5),('06emp00004','flightless01','(415)555-5938','Yorres','Jaime',87,'Polk St. Suite 5','San Francisco','CA','94117','administrative',6),('06emp00005','quantum09','(412)555-9441','Summer','Martin',717,'E Michigan Ave','San Francisco','CA','90611','veterinarian',6);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice` (
  `appointment_id` int NOT NULL,
  `owner_id` varchar(20) NOT NULL,
  `issued_date` date NOT NULL,
  `total_amount_per_visit` decimal(12,2) DEFAULT '0.00',
  `pmt_received_amt` decimal(12,2) DEFAULT '0.00',
  `pmt_received_date` date DEFAULT NULL,
  PRIMARY KEY (`appointment_id`),
  KEY `owner_id` (`owner_id`),
  KEY `appointment_id` (`appointment_id`),
  CONSTRAINT `invoice_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `pet_owner` (`owner_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `invoice_ibfk_2` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`appointment_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
INSERT INTO `invoice` VALUES (1,'cuscatlady','2023-11-03',638.00,0.00,NULL),(2,'cususer5','2023-11-30',603.00,0.00,NULL),(3,'cususer1','2023-12-01',48.95,0.00,NULL),(4,'cususer2','2023-12-02',850.00,0.00,NULL),(5,'cusbuddad','2023-12-05',2659.99,0.00,NULL),(6,'cususer4','2023-12-05',960.50,0.00,NULL),(9,'cuscatlady','2023-12-07',0.00,0.00,NULL),(10,'cususer1','2023-12-08',0.00,0.00,NULL),(11,'cususer4','2023-12-08',0.00,0.00,NULL);
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medication`
--

DROP TABLE IF EXISTS `medication`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medication` (
  `medication_name` varchar(255) NOT NULL,
  `medication_description` varchar(255) DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`medication_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medication`
--

LOCK TABLES `medication` WRITE;
/*!40000 ALTER TABLE `medication` DISABLE KEYS */;
INSERT INTO `medication` VALUES ('amoxicillin','antibacterial',15.50),('dexamethasone','anti-inflammatory steroid',20.85),('hydroxyzine','antihistamine drug used primarily for treatment of allergies',45.50),('lufenuron','insecticide used for flea control',10.50),('methimazole','used in treatment of hyperthyroidism',8.25),('methocarbamol','muscle relaxant used to reduce muscle spasms associated with inflammation, \n	injury, intervertebral disc disease, and certain toxicities',35.50),('oxibendazole','anthelmintic',28.65),('pyrantel','effective against ascarids, hookworms and stomach worms',10.00),('sucralfate','treats gastric ulcers',9.99),('tramadol','analgesic',8.50);
/*!40000 ALTER TABLE `medication` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pet`
--

DROP TABLE IF EXISTS `pet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pet` (
  `patient_id` int NOT NULL AUTO_INCREMENT,
  `pet_name` varchar(64) NOT NULL,
  `date_of_birth` date NOT NULL,
  `height_in_cm` decimal(3,1) NOT NULL,
  `weight_in_lb` decimal(3,1) NOT NULL,
  `pet_status` varchar(255) DEFAULT NULL,
  `category_id` int NOT NULL,
  `owner_id` varchar(20) NOT NULL,
  PRIMARY KEY (`patient_id`),
  KEY `owner_id` (`owner_id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `pet_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `pet_owner` (`owner_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `pet_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `pet_category` (`category_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pet`
--

LOCK TABLES `pet` WRITE;
/*!40000 ALTER TABLE `pet` DISABLE KEYS */;
INSERT INTO `pet` VALUES (1,'Buddy','2020-05-15',35.5,22.0,'Healthy',1,'cusbuddad'),(2,'Whiskers','2019-08-10',25.3,9.5,'Vaccinated',2,'cuscatlady'),(3,'Pom','2000-03-10',20.2,18.5,'Vaccinated',3,'cuscatlady'),(4,'Hopper','2021-03-25',15.8,4.3,'Healthy',4,'cususer2'),(5,'Rex','2020-11-02',40.0,4.2,'Under Treatment',5,'cususer4'),(6,'Luna','2018-09-20',10.0,5.0,'Healthy',6,'cususer5'),(7,'Charlie','2019-04-12',32.5,18.8,'Vaccinated',1,'cususer1'),(8,'Snowball','2022-01-07',20.2,25.0,'Healthy',7,'cususer3'),(9,'Max','2020-07-15',42.5,78.3,'Under Treatment',9,'cusdoglvr');
/*!40000 ALTER TABLE `pet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pet_category`
--

DROP TABLE IF EXISTS `pet_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pet_category` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `species_treated` enum('Dog','Cat','Rabbit','Ferret','Others') NOT NULL,
  `breed_name` varchar(64) NOT NULL,
  `breed_description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `species_treated` (`species_treated`,`breed_name`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pet_category`
--

LOCK TABLES `pet_category` WRITE;
/*!40000 ALTER TABLE `pet_category` DISABLE KEYS */;
INSERT INTO `pet_category` VALUES (1,'Dog','Golden Retriever','Friendly and intelligent breed.'),(2,'Cat','Siamese','Elegant and vocal breed.'),(3,'Cat','Maine Coon','Very playful and friendly breed.'),(4,'Rabbit','Holland Lop','Small and docile rabbit breed.'),(5,'Ferret','Standard Ferret','Playful and curious domesticated ferret.'),(6,'Others','Parrot','Colorful and talkative bird.'),(7,'Dog','Poodle','Hypoallergenic and intelligent dog breed.'),(8,'Dog','Labrador Retriever','Friendly and outgoing breed.'),(9,'Dog','German Shepherd','Smart and loyal breed.'),(10,'Dog','Bulldog','Sturdy and affectionate breed.'),(11,'Dog','Boxer','Energetic and playful breed.'),(12,'Dog','Dachshund','Curious and playful breed.');
/*!40000 ALTER TABLE `pet_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pet_clinic`
--

DROP TABLE IF EXISTS `pet_clinic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pet_clinic` (
  `clinic_id` int NOT NULL AUTO_INCREMENT,
  `clinic_phone` char(13) NOT NULL,
  `clinic_street_number` int NOT NULL,
  `clinic_street_name` varchar(64) NOT NULL,
  `clinic_town` varchar(64) NOT NULL,
  `clinic_state_abbrev` char(2) NOT NULL,
  `clinic_zip` char(5) NOT NULL,
  `clinic_type` enum('Clinic','Urgent Care','Emergency') NOT NULL,
  PRIMARY KEY (`clinic_id`),
  UNIQUE KEY `clinic_phone` (`clinic_phone`),
  UNIQUE KEY `clinic_street_number` (`clinic_street_number`,`clinic_street_name`,`clinic_town`,`clinic_state_abbrev`,`clinic_zip`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pet_clinic`
--

LOCK TABLES `pet_clinic` WRITE;
/*!40000 ALTER TABLE `pet_clinic` DISABLE KEYS */;
INSERT INTO `pet_clinic` VALUES (1,'(337)555-5678',1877,'Animal Way','Frogtown','LA','70563','Clinic'),(2,'(351)555-7890',456,'Hospital St','Boston','MA','02215','Urgent Care'),(3,'(206)555-4321',305,'Veterinary Ave','Seattle','WA','98128','Urgent Care'),(4,'(765)123-4567',123,'Clinic Ave','Anderson','IN','46014','Clinic'),(5,'(469)555-6789',2299,'Pet Blvd','Dallas','TX','75224','Emergency'),(6,'(408)555-7890',555,'Pet Haven Dr','San Jose','CA','95117','Clinic');
/*!40000 ALTER TABLE `pet_clinic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pet_owner`
--

DROP TABLE IF EXISTS `pet_owner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pet_owner` (
  `owner_id` varchar(20) NOT NULL,
  `id_password` varchar(12) NOT NULL,
  `card_number` char(12) NOT NULL,
  `owner_firstname` varchar(255) NOT NULL,
  `owner_lastname` varchar(255) NOT NULL,
  `card_type` enum('Debit','VISA','MasterCard','American Express','Discover') NOT NULL,
  `billing_street_number` int NOT NULL,
  `billing_street_name` varchar(64) NOT NULL,
  `billing_town` varchar(64) NOT NULL,
  `billing_state_abbrev` char(2) NOT NULL,
  `billing_zip` char(5) NOT NULL,
  PRIMARY KEY (`owner_id`),
  UNIQUE KEY `card_number` (`card_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pet_owner`
--

LOCK TABLES `pet_owner` WRITE;
/*!40000 ALTER TABLE `pet_owner` DISABLE KEYS */;
INSERT INTO `pet_owner` VALUES ('CUSbuddad','securepass','987654321098','Jane','Smith','MasterCard',456,'Lone Star Blvd','Huston','TX','77001'),('CUScatlady','mypassword2','456789123456','Jenny','Johnson','Debit',789,'Bayou Dr','Baton Rouge','LA','70801'),('CUSdoglvr','secretcode','789123456789','Sarah','Williams','Discover',101,'Oak St','Portland','ME','04101'),('CUSuser1','password123','123456789012','John','Doe','VISA',123,'Evergreen Ave','Seattle','WA','98101'),('CUSuser2','mypassword','444455556666','David','Smith','MasterCard',234,'Hoosier Ln','Indianapolis','IN','46201'),('CUSuser3','securepass1','111122223333','Emily','Johnson','VISA',123,'Oak St','Boston','MA','02101'),('CUSuser4','secretcode3','777788889999','Olivia','Williams','Debit',123,'Sunset Blvd','Los Angeles','CA','90001'),('CUSuser5','strongpass4','999900001111','William','Davis','American Express',101,'Cedar St','Portland','ME','04101');
/*!40000 ALTER TABLE `pet_owner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `room_id` int NOT NULL AUTO_INCREMENT,
  `clinic_id` int NOT NULL,
  `room_number` int NOT NULL,
  `state_of_use` enum('AVAILABLE','IN USE') DEFAULT 'AVAILABLE',
  PRIMARY KEY (`room_id`),
  UNIQUE KEY `clinic_id` (`clinic_id`,`room_number`),
  CONSTRAINT `room_ibfk_1` FOREIGN KEY (`clinic_id`) REFERENCES `pet_clinic` (`clinic_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,1,1,'AVAILABLE'),(2,1,2,'AVAILABLE'),(3,1,3,'AVAILABLE'),(4,1,4,'AVAILABLE'),(5,1,5,'AVAILABLE'),(6,1,6,'AVAILABLE'),(7,1,7,'AVAILABLE'),(8,1,8,'AVAILABLE'),(9,2,2,'AVAILABLE'),(10,2,1,'AVAILABLE'),(11,2,3,'AVAILABLE'),(12,2,4,'AVAILABLE'),(13,2,6,'AVAILABLE'),(14,2,5,'AVAILABLE'),(15,3,1,'AVAILABLE'),(16,3,7,'AVAILABLE'),(17,3,6,'AVAILABLE'),(18,3,5,'AVAILABLE'),(19,3,2,'AVAILABLE'),(20,3,3,'AVAILABLE'),(21,3,4,'AVAILABLE'),(22,3,8,'AVAILABLE'),(23,4,1,'AVAILABLE'),(24,4,5,'AVAILABLE'),(25,4,2,'AVAILABLE'),(26,4,3,'AVAILABLE'),(27,4,4,'AVAILABLE'),(28,4,6,'AVAILABLE'),(29,5,1,'IN USE'),(30,5,2,'IN USE'),(31,5,8,'IN USE'),(32,5,4,'AVAILABLE'),(33,5,6,'AVAILABLE'),(34,5,5,'AVAILABLE'),(35,5,7,'AVAILABLE'),(36,5,3,'AVAILABLE'),(37,6,1,'AVAILABLE'),(38,6,7,'AVAILABLE'),(39,6,2,'AVAILABLE'),(40,6,3,'AVAILABLE'),(41,6,4,'AVAILABLE'),(42,6,5,'AVAILABLE'),(43,6,6,'AVAILABLE');
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `treatment`
--

DROP TABLE IF EXISTS `treatment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `treatment` (
  `appoint_id` int NOT NULL,
  `service_id` int NOT NULL,
  `charge` decimal(10,2) NOT NULL,
  PRIMARY KEY (`appoint_id`,`service_id`),
  KEY `service_id` (`service_id`),
  CONSTRAINT `treatment_ibfk_1` FOREIGN KEY (`service_id`) REFERENCES `treatment_service` (`service_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `treatment_ibfk_2` FOREIGN KEY (`appoint_id`) REFERENCES `appointment` (`appointment_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `treatment`
--

LOCK TABLES `treatment` WRITE;
/*!40000 ALTER TABLE `treatment` DISABLE KEYS */;
INSERT INTO `treatment` VALUES (1,1,250.00),(1,5,250.00),(2,7,150.00),(2,16,220.00),(3,2,220.00),(3,17,170.00),(4,6,550.00),(4,15,300.00),(5,14,550.00),(5,21,2100.00),(6,4,220.00),(6,18,600.00);
/*!40000 ALTER TABLE `treatment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `treatment_service`
--

DROP TABLE IF EXISTS `treatment_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `treatment_service` (
  `service_id` int NOT NULL AUTO_INCREMENT,
  `treatment_type` enum('preventative care','sick care','surgery','additionals') NOT NULL,
  `treatment_name` varchar(64) NOT NULL,
  `treatment_description` varchar(255) DEFAULT NULL,
  `regular_price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`service_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `treatment_service`
--

LOCK TABLES `treatment_service` WRITE;
/*!40000 ALTER TABLE `treatment_service` DISABLE KEYS */;
INSERT INTO `treatment_service` VALUES (1,'additionals','travel certificates',NULL,200.00),(2,'additionals','allergies',NULL,200.00),(3,'additionals','nutrition',NULL,200.00),(4,'additionals','grooming',NULL,200.00),(5,'preventative care','dentals','Dental cleaning and polish,including inspection for any cavities, fractures and other dental issues',200.00),(6,'preventative care','wellness exam','a 360-degree exam looks at everything from nose to tail, including oral (teeth and gums), ears, \nophthalmologic (eyes), cardiothoracic (heart and lungs), as well as abdominal, \nand musculoskeletal examinations',500.00),(7,'preventative care','vaccination-Rabies','core',100.00),(8,'preventative care','vaccination-DHPPi','core',100.00),(9,'preventative care','vaccination-Leptospirosis','core',100.00),(10,'preventative care','vaccination-FVRCP','core',100.00),(11,'preventative care','vaccination-Feline','core',100.00),(12,'preventative care','vaccination-Lyme','non-core',50.00),(13,'preventative care','vaccination-Bordetella','non-core',50.00),(14,'sick care','diagnostic testing','bloodwork, fecal testing,cytology,radiology',500.00),(15,'sick care','urgent care','contact 24/7 for immediate injuries',200.00),(16,'surgery','Spaying & Neutering','cats',200.00),(17,'surgery','Spaying & Neutering','dogs',150.00),(18,'surgery','Specialty Surgery','Hernia Repairs',500.00),(19,'surgery','Specialty Surgery','dental',1000.00),(20,'surgery','Specialty Surgery','wound repair',800.00),(21,'surgery','Specialty Surgery','Mass removal',2000.00);
/*!40000 ALTER TABLE `treatment_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `veterinarian`
--

DROP TABLE IF EXISTS `veterinarian`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `veterinarian` (
  `vet_id` char(10) NOT NULL,
  `vet_license` varchar(20) NOT NULL,
  `vet_certificate` varchar(255) DEFAULT NULL,
  `work_years` int DEFAULT '0',
  `specialization` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`vet_id`),
  CONSTRAINT `veterinarian_ibfk_1` FOREIGN KEY (`vet_id`) REFERENCES `employee` (`emp_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `veterinarian`
--

LOCK TABLES `veterinarian` WRITE;
/*!40000 ALTER TABLE `veterinarian` DISABLE KEYS */;
INSERT INTO `veterinarian` VALUES ('01emp00008','LAPVD001','Veterinary degree from Iowa State University',10,'emergency care and medicine'),('02emp00003','MAPVD134','Masters degree in Animal Welfare Law and Ethics from The University of Glasgow',2,'emergency medicine, surgery, and neurology'),('02emp00009','MAPVD523','Veterinary degree from Boston University',5,'emergency medicine, surgery, and neurology'),('03emp00006','WAPVD2234','Bachelor’s degree and veterinary degree from Cornell University',1,'emergency medicine'),('04emp00001','INPVD123','Veterinary degree from Indiana State University',1,'emergency medicine, surgery, and neurology'),('05emp00010','TXPVD778','Royal Veterinary College in London UK',2,'emergency care and medicine'),('06emp00005','CAPVD002','Veterinary degree from UCLA',6,'emergency medicine, surgery, and neurology');
/*!40000 ALTER TABLE `veterinarian` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'we_pc'
--

--
-- Dumping routines for database 'we_pc'
--
/*!50003 DROP FUNCTION IF EXISTS `is_book_full` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `is_book_full`(time_match DATETIME, employee_id CHAR(10)) RETURNS int
    DETERMINISTIC
BEGIN
	DECLARE is_full INT DEFAULT 0;
    DECLARE max_book INT DEFAULT 5;
    DECLARE book_num INT DEFAULT 0;
	DECLARE clinic_var INT;
    
	SELECT clinic_id INTO clinic_var FROM employee WHERE emp_id = employee_id;
    
    -- find the number of vet in the clinic
    SELECT COUNT(*) INTO max_book 
    FROM veterinarian JOIN employee on vet_id = emp_id
    WHERE clinic_id = clinic_var;
    
	SELECT COUNT(*) INTO book_num FROM appointment where clinic_id = clinic_var and appointment_datetime = time_match;
    IF book_num = max_book THEN 
		SET is_full = 1;
	END IF;
    RETURN is_full;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `veterinarian_match` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `veterinarian_match`(c_id INT, time_match DATETIME) RETURNS char(10) CHARSET utf8mb4
    DETERMINISTIC
BEGIN
	DECLARE vet_var CHAR(10);
    DECLARE clinic_var int;

    SELECT vet_id INTO vet_var
    FROM veterinarian JOIN employee ON vet_id = emp_id
    WHERE vet_id NOT IN (SELECT vet_id FROM appointment WHERE appointment_datetime = time_match)
		AND clinic_id = c_id
	LIMIT 1;
    RETURN vet_var;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `add_medication` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_medication`(appoint_p INT, 
	medication_p VARCHAR(255), quantity_p INT, interval_p INT)
BEGIN
	DECLARE price_var DECIMAL(10, 2);
    
    IF NOT EXISTS (SELECT * FROM invoice WHERE appointment_id = appoint_p) THEN
		SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invoice not exists, re-enter is required.';
	END IF;
    
    IF quantity_p < 1 OR interval_p < 1 THEN
		SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid inputs, try again.';
	END IF;
    
    -- Check if medication exists, then insert a row in the treatment table
    SELECT price INTO price_var FROM medication
		WHERE medication_name = medication_p;

	-- If medication not found, signal an error
    IF price_var IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid medication.';
	ELSE
		INSERT INTO appointment_medication (appointment_id, medication_name, quantity, time_interval_days)
			VALUES (appoint_p, medication_p, quantity_p, interval_p);
	END IF;

	-- If no invoice exists, then this visit has not been checked in yet, raise error
    -- If invoice exists, update the invoice with the old amount + quantity * price amount
	UPDATE invoice
	SET total_amount_per_visit = total_amount_per_visit + (price_var * quantity_p)
	WHERE appointment_id = appoint_p;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `add_treatment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_treatment`(appoint_p INT, 
	service_p INT, actual_charge DECIMAL(10, 2))
BEGIN
    
    -- Insert a row in the treatment table
	INSERT INTO treatment (appoint_id, service_id, charge)
        VALUES (appoint_p, service_p, actual_charge);
	
	-- If no invoice exists, then this visit has not been checked in yet, raise error
	IF NOT EXISTS (SELECT * FROM invoice WHERE appointment_id = appoint_p) THEN
		SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invoice not exists, re-enter is required.';
	END IF;
	
	-- If invoice exists, update the invoice with the old amount + charge amount
	UPDATE invoice
	SET total_amount_per_visit = total_amount_per_visit + actual_charge
	WHERE appointment_id = appoint_p;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `appointment_treatments` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `appointment_treatments`(IN appoint_p INT)
BEGIN
    DECLARE appoint_count INT;
    SELECT COUNT(DISTINCT service_id) INTO appoint_count 
		FROM treatment
        WHERE appoint_id = appoint_p;

    IF appoint_count = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid appointment id/No treatment has done for this appointment.';
    ELSE
        -- Display treatment services for the appointment
        SELECT ts.service_id, ts.treatment_name, ts.treatment_description, t.charge
        FROM treatment t JOIN treatment_service ts USING (service_id)
        WHERE t.appoint_id = appoint_p;
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `appoint_show_up` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `appoint_show_up`(IN appoint_p INT)
BEGIN
	DECLARE clinic_var INT;
    DECLARE room_var INT;
    DECLARE owner_var VARCHAR(20);

    -- Get the clinic_id and owner_id for the appointment
    SELECT clinic_id, owner_id INTO clinic_var, owner_var
    FROM appointment
    JOIN pet USING (patient_id)
    WHERE appointment_id = appoint_p;
    
	-- Find an available room in the same clinic
    SELECT room_id INTO room_var FROM room
    WHERE clinic_id = clinic_var AND state_of_use = 'AVAILABLE'
    ORDER BY room_id ASC LIMIT 1;
    
	-- If no available room, signal an error
    IF room_var IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Please wait and sign in the pet owner later, 
			no available room in clinic.';
	END IF;
    
	IF EXISTS (SELECT * FROM invoice WHERE appointment_id = appoint_p) THEN
		SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invoice exists, has checked in already.';
	END IF;
    
    
    -- Update the appointment to assign the room and update the room state of use to in use
    UPDATE appointment SET room_id = room_var WHERE appointment_id = appoint_p;
     UPDATE appointment SET show_up = 1 WHERE appointment_id = appoint_p;
    UPDATE room SET state_of_use = 'IN USE' WHERE room_id = room_var;
	SELECT CONCAT('Please use Room Number: ', (SELECT room_number FROM room WHERE room_id = room_var), 
		' for this treatment.') AS message;
	-- insert a invoice for this appointment
    INSERT INTO invoice (appointment_id, owner_id, issued_date, total_amount_per_visit) 
		VALUES (appoint_p, owner_var, CURDATE(), DEFAULT);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `cancel_appointment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `cancel_appointment`(employee_id CHAR(10), appoint_time DATETIME, petID int)
BEGIN
	DECLARE clinic_var INT;
    DECLARE appoint_var INT;
    DECLARE show_var tinyint;
    
	SELECT clinic_id INTO clinic_var FROM employee WHERE emp_id = employee_id;
	SELECT appointment_id, show_up INTO appoint_var,show_var FROM appointment
    WHERE patient_id = petID and clinic_id = clinic_var and appointment_datetime = appoint_time;
    
   
	IF show_var = 1 THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = "this appointment is processed. Delete Denied";
	ELSEIf appoint_var > 0 THEN
		DELETE FROM appointment where appointment_id = appoint_var;    
	ELSE 
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'appointment is NOT found in your clinic';
	END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `clinic_has_treatments` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `clinic_has_treatments`(vet_p CHAR(10), appoint_p INT)
BEGIN
    DECLARE clinic_p INT;
	SELECT clinic_id INTO clinic_p FROM employee WHERE emp_id = vet_p;
    
    IF clinic_p IS NOT NULL THEN
		SELECT service_id, treatment_name, treatment_description, regular_price FROM treatment_service ts
			JOIN available_treatment_in_clinic atc USING (service_id)
		WHERE atc.clinic_id = clinic_p AND ts.service_id NOT IN (
            SELECT service_id FROM treatment WHERE appoint_id = appoint_p);
	ELSE
		SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = "Not valid vet info.";
	END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `complete_appoint` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `complete_appoint`(appoint_p INT)
BEGIN
	DECLARE room_var INT;
    DECLARE state VARCHAR(10);
	IF NOT EXISTS (SELECT * FROM invoice WHERE appointment_id = appoint_p) THEN
		SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invoice not exists, re-enter is required.';
	END IF;
    
    SELECT room_id INTO room_var FROM appointment WHERE appointment_id = appoint_p;
    
	-- If room not assigned, signal an error
    IF room_var IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No room has been assigned, no show, cannot complete.';
	END IF;
    
    SELECT state_of_use INTO state FROM room WHERE room_id = room_var;
    IF state = 'AVAILABLE' THEN 
		SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Appointment has been completed or appointment is a no-show.';
	END IF;   
    -- Update assigned room to available as appointment completed
	UPDATE room SET state_of_use = 'AVAILABLE' WHERE room_id = room_var;
    
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `display_appointment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `display_appointment`(employee_id CHAR(10))
BEGIN
	DECLARE clinic_var INT;
        
    SELECT clinic_id INTO clinic_var FROM employee WHERE emp_id = employee_id;
	If clinic_var IS NULL THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT= 'Employee NOT found';
	ELSE
		WITH apt_table (appointment, meeting_time, pet, family, basic_description, veterinarian) AS
		(SELECT appointment_id, appointment_datetime,pet_name, 
			CONCAT(owner_lastname,', ',owner_firstname),appointment_description, CONCAT(emp_last_name,',',emp_first_name)
			FROM employee e JOIN appointment a ON vet_id = emp_id
				JOIN pet p ON p.patient_id = a.patient_id
					JOIN pet_owner pe ON p.owner_id = pe.owner_id
		WHERE a.clinic_id  = clinic_var)
		SELECT * FROM apt_table
        ORDER BY meeting_time;
	END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `display_appointments_for_owner` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `display_appointments_for_owner`(IN p_owner_id VARCHAR(20))
BEGIN
    SELECT
        a.appointment_id,
        a.appointment_datetime,
        a.appointment_description,
        a.patient_id,
        p.pet_name,
        a.clinic_id,
        CASE
            WHEN a.show_up = FALSE THEN 'No'
            ELSE 'Yes'
        END AS show_up_status,
        a.vet_id,
        CONCAT(e.emp_last_name, ', ', e.emp_first_name) AS vet_name,
        a.room_id
    FROM
        appointment a
    INNER JOIN pet p ON a.patient_id = p.patient_id
    INNER JOIN veterinarian v ON a.vet_id = v.vet_id
    INNER JOIN employee e ON v.vet_id = e.emp_id
    LEFT JOIN room r ON a.room_id = r.room_id
    WHERE
        p.owner_id = p_owner_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `display_appointment_detail` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `display_appointment_detail`(employee_id CHAR(10), apt_id INT)
BEGIN
	DECLARE clinic_var INT;
    DECLARE clinic_var1 INT;
            
    SELECT clinic_id INTO clinic_var FROM employee WHERE emp_id = employee_id;
    SELECT clinic_id INTO clinic_var1 FROM appointment a WHERE appointment_id = apt_id;
	If (clinic_var1 IS NULL) THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT= 'Appointment ID is invalid';
	ELSEIF clinic_var != clinic_var1 THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT= 'You are NOT authorized to see this appointment';
	ELSE
		WITH apt_table (appointment_id, meeting_time, pet, pet_owner, basic_description, veterinarian, room, visit_expenditure, confirmed_amt, received_date) AS
		(SELECT a.appointment_id, appointment_datetime,pet_name,
			CONCAT(owner_lastname,', ',owner_firstname), appointment_description, CONCAT(emp_last_name,',',emp_first_name), room_id, total_amount_per_visit,pmt_received_amt,pmt_received_date
			FROM appointment a JOIN pet p ON p.patient_id = a.patient_id
				JOIN employee ON vet_id = emp_id
					JOIN pet_owner pe ON p.owner_id = pe.owner_id
						LEFT JOIN invoice i ON a.appointment_id = i.appointment_id
		WHERE a.clinic_id  = clinic_var AND a.appointment_id = apt_id)
		SELECT * FROM apt_table
        ORDER BY received_date;
	END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `display_appointment_for_employee` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `display_appointment_for_employee`(employee_id CHAR(10))
BEGIN
	DECLARE clinic_var INT;
        
    SELECT clinic_id INTO clinic_var FROM employee WHERE emp_id = employee_id;
	If clinic_var IS NULL THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT= 'Employee NOT found';
	ELSE
		WITH apt_table (appointment, meeting_time, petID, pet, family, showup) AS
		(SELECT appointment_id, appointment_datetime, a.patient_id, pet_name, 
			CONCAT(owner_lastname,', ',owner_firstname), a.show_up
			FROM appointment a JOIN pet p ON p.patient_id = a.patient_id
					JOIN pet_owner pe ON p.owner_id = pe.owner_id
		WHERE a.clinic_id  = clinic_var)
		SELECT * FROM apt_table
        ORDER BY meeting_time;
	END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `display_appointment_for_vet` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `display_appointment_for_vet`(employee_id CHAR(10))
BEGIN
	DECLARE clinic_var INT;
        
    SELECT clinic_id INTO clinic_var FROM employee WHERE emp_id = employee_id;
	If clinic_var IS NULL THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT= 'Employee NOT found';
	ELSE
		WITH apt_table (vd,veterinarian, meeting_time, pet, basic_description, treatment,medication, quantity, days) AS
		(SELECT vet_id, CONCAT(emp_last_name,',',emp_first_name), appointment_datetime,pet_name, 
			appointment_description, treatment_name, medication_name,quantity, time_interval_days
			FROM appointment a
				JOIN pet p ON p.patient_id = a.patient_id
					JOIN treatment t ON t.appoint_id = a.appointment_id
						JOIN treatment_service ts ON ts.service_id = t.service_id
							JOIN appointment_medication am ON a.appointment_id = am.appointment_id
								JOIN employee ON vet_id = emp_id)
		SELECT veterinarian, meeting_time, pet, basic_description, treatment,medication, quantity, days
        FROM apt_table 
        WHERE vd = employee_id 
        ORDER BY meeting_time;
	END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `display_invoices_for_appointment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `display_invoices_for_appointment`(IN p_appointment_id INT)
BEGIN
    SELECT
        issued_date,
        total_amount_per_visit,
        pmt_received_amt,
        pmt_received_date
    FROM
        invoice
    WHERE
        appointment_id = p_appointment_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `display_medication` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `display_medication`(IN appoint_p CHAR(10))
BEGIN
	SELECT medication_name 
	FROM medication 
	WHERE medication_name NOT IN ( SELECT medication_name 
		FROM appointment_medication WHERE appointment_id = appoint_p);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `display_pet_info` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `display_pet_info`(IN p_owner_id VARCHAR(20))
BEGIN
    SELECT 
        p.patient_id,
        p.pet_name,
        pc.breed_name,
        p.height_in_cm,
        p.weight_in_lb,
        p.pet_status
    FROM 
        pet p
    INNER JOIN 
        pet_category pc ON p.category_id = pc.category_id
    WHERE 
        p.owner_id = p_owner_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `display_treatments_and_medications_for_appointment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `display_treatments_and_medications_for_appointment`(IN p_appointment_id INT)
BEGIN
    SELECT
        ts.treatment_name AS name,
        ts.treatment_description AS description,
        t.charge AS price,
        'Treatment' AS type
    FROM
        treatment t
    INNER JOIN treatment_service ts ON t.service_id = ts.service_id
    WHERE
        t.appoint_id = p_appointment_id
    UNION ALL
    SELECT
        m.medication_name AS name,
        m.medication_description AS description,
        m.price AS price,
        'Medication' AS type
    FROM
        appointment_medication am
    INNER JOIN medication m ON am.medication_name = m.medication_name
    WHERE
        am.appointment_id = p_appointment_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `make_appointment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `make_appointment`(employee_id CHAR(10), appoint_time DATETIME, petID INT, description_of_request VARCHAR(255))
BEGIN
	DECLARE pet_var VARCHAR(64);
    DECLARE clinic_var INT;
    DECLARE check_record INT DEFAULT 0;
    DECLARE vet_var CHAR(10);
    DECLARE name_l_var VARCHAR(20);
    DECLARE name_f_var VARCHAR(40);
    
    --  find the clinic id
    SELECT clinic_id INTO clinic_var FROM employee WHERE emp_id = employee_id;
    
    SELECT pet_name INTO pet_var FROM pet WHERE patient_id = petID;
    
    -- match with a vacant vet who works in the clinic
    SET vet_var = veterinarian_match(clinic_var, appoint_time);
    SELECT emp_last_name, emp_first_name INTO name_l_var, name_f_var FROM employee WHERE emp_id = vet_var;
    
    -- check if appointment already exist
    SELECT appointment_id INTO check_record FROM appointment 
    WHERE clinic_id =  clinic_var AND appointment_datetime = appoint_time AND patient_id = petID;
    
    -- If required vet is not available
    IF check_record > 0 THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT='appointment already exists';
	-- If petID is invalid
	ELSEIF (petID < 0 OR petID > (SELECT COUNT(*) FROM pet)) THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT= "this pet ID doesn't exist";
	ELSE 
		INSERT INTO appointment 
        VALUES (DEFAULT, appoint_time, description_of_request, petID, clinic_var, 0, vet_var, NULL);
        SELECT appoint_time AS appointment_time, description_of_request AS description, pet_var AS pet, (CONCAT(name_l_var,', ',name_f_var)) AS vet; 
	END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `remove_treatment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `remove_treatment`(appoint_p INT, service_p INT)
BEGIN

    DECLARE charge_var DECIMAL(10, 2);
    
	-- Find the charge for the treatment
    SELECT charge INTO charge_var
    FROM treatment 
    WHERE appoint_id = appoint_p AND service_id = service_p;
    
	IF charge_var IS NULL THEN
		SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = "No selected service added for this appointment.";
	ELSE
		DELETE FROM treatment
        WHERE appoint_id = appoint_p AND service_id = service_p;
	END IF;
	
    -- invoice must be created if treatment added for appointment
	UPDATE invoice
	SET total_amount_per_visit = total_amount_per_visit - charge_var
	WHERE appointment_id = appoint_p;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `signup_pet_owner` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `signup_pet_owner`(
    IN p_username VARCHAR(20),
    IN p_password VARCHAR(12),
    IN p_card_number CHAR(12),
    IN p_first_name VARCHAR(255),
    IN p_last_name VARCHAR(255),
    IN p_card_type ENUM('Debit', 'VISA', 'MasterCard', 'American Express', 'Discover'),
    IN p_billing_street_number INT,
    IN p_billing_street_name VARCHAR(64),
    IN p_billing_town VARCHAR(64),
    IN p_billing_state_abbrev CHAR(2),
    IN p_billing_zip CHAR(5)
)
BEGIN
    DECLARE existingUserCount INT;
    SET p_username = CONCAT('CUS', p_username);
    SELECT COUNT(*) INTO existingUserCount
    FROM pet_owner
    WHERE owner_id = p_username OR card_number = p_card_number;
    IF existingUserCount = 0 THEN
        INSERT INTO pet_owner(
            owner_id,
            id_password,
            card_number,
            owner_firstname,
            owner_lastname,
            card_type,
            billing_street_number,
            billing_street_name,
            billing_town,
            billing_state_abbrev,
            billing_zip
        ) VALUES (
            p_username,
            p_password,
            p_card_number,
            p_first_name,
            p_last_name,
            p_card_type,
            p_billing_street_number,
            p_billing_street_name,
            p_billing_town,
            p_billing_state_abbrev,
            p_billing_zip
        );
    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'A user with the given owner_id or card number already exists.';
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `update_owner_password` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_owner_password`(
    IN p_owner_id VARCHAR(20),
    IN p_new_password VARCHAR(12)
)
BEGIN
    UPDATE pet_owner
    SET
        id_password = p_new_password
    WHERE
        owner_id = p_owner_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `update_payment_received` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_payment_received`(
    IN p_appointment_id INT,
    IN p_additional_amount DECIMAL(12, 2)
)
BEGIN
    DECLARE v_total_amount DECIMAL(12, 2);
    SELECT total_amount_per_visit INTO v_total_amount
    FROM invoice
    WHERE appointment_id = p_appointment_id;
    UPDATE invoice
    SET
        pmt_received_amt = GREATEST(LEAST(pmt_received_amt + p_additional_amount, v_total_amount), pmt_received_amt),
        pmt_received_date = CURRENT_DATE()
    WHERE
        appointment_id = p_appointment_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `validate_user_credentials` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `validate_user_credentials`(IN p_username VARCHAR(20), IN p_password VARCHAR(12))
BEGIN
    DECLARE user_type VARCHAR(20);

    IF EXISTS(SELECT 1 FROM pet_owner WHERE owner_id = p_username AND id_password = p_password) THEN
        SET user_type = 'user';
    ELSEIF EXISTS(SELECT 1 FROM employee WHERE emp_id = p_username AND id_password = p_password) THEN
        SELECT emp_type INTO user_type FROM employee WHERE emp_id = p_username;
        IF user_type <> 'veterinarian' THEN
            SET user_type = 'employee';
        END IF;
    ELSE
        SET user_type = NULL;
    END IF;
    SELECT 
        (user_type IS NOT NULL) AS success, 
        user_type;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-08  4:02:17
