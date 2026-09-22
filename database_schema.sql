-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)਍ഀ
--਍ഀ
-- Host: localhost    Database: fashion_store਍ഀ
-- ------------------------------------------------------਍ഀ
-- Server version	8.0.46਍ഀ
਍ഀ
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;਍ഀ
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;਍ഀ
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;਍ഀ
/*!50503 SET NAMES utf8mb4 */;਍ഀ
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;਍ഀ
/*!40103 SET TIME_ZONE='+00:00' */;਍ഀ
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;਍ഀ
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;਍ഀ
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;਍ഀ
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;਍ഀ
਍ഀ
--਍ഀ
-- Table structure for table `addresses`਍ഀ
--਍ഀ
਍ഀ
DROP TABLE IF EXISTS `addresses`;਍ഀ
/*!40101 SET @saved_cs_client     = @@character_set_client */;਍ഀ
/*!50503 SET character_set_client = utf8mb4 */;਍ഀ
CREATE TABLE `addresses` (਍ഀ
  `address_id` int NOT NULL AUTO_INCREMENT,਍ഀ
  `user_id` int NOT NULL,਍ഀ
  `full_name` varchar(100) NOT NULL,਍ഀ
  `phone` varchar(20) NOT NULL,਍ഀ
  `address_line1` varchar(255) NOT NULL,਍ഀ
  `address_line2` varchar(255) DEFAULT NULL,਍ഀ
  `city` varchar(100) NOT NULL,਍ഀ
  `state` varchar(100) NOT NULL,਍ഀ
  `postal_code` varchar(20) NOT NULL,਍ഀ
  `country` varchar(100) NOT NULL,਍ഀ
  `is_default` tinyint(1) DEFAULT '0',਍ഀ
  PRIMARY KEY (`address_id`),਍ഀ
  KEY `user_id` (`user_id`),਍ഀ
  CONSTRAINT `addresses_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE਍ഀ
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;਍ഀ
/*!40101 SET character_set_client = @saved_cs_client */;਍ഀ
਍ഀ
--਍ഀ
-- Dumping data for table `addresses`਍ഀ
--਍ഀ
਍ഀ
LOCK TABLES `addresses` WRITE;਍ഀ
/*!40000 ALTER TABLE `addresses` DISABLE KEYS */;਍ഀ
INSERT INTO `addresses` VALUES (1,2,'Mohammed Faisal. D','8660402506','Prashanth Nagar 1st Main 4th cross',NULL,'Harihar','Karnataka','577601','India',0);਍ഀ
/*!40000 ALTER TABLE `addresses` ENABLE KEYS */;਍ഀ
UNLOCK TABLES;਍ഀ
਍ഀ
--਍ഀ
-- Table structure for table `cart`਍ഀ
--਍ഀ
਍ഀ
DROP TABLE IF EXISTS `cart`;਍ഀ
/*!40101 SET @saved_cs_client     = @@character_set_client */;਍ഀ
/*!50503 SET character_set_client = utf8mb4 */;਍ഀ
CREATE TABLE `cart` (਍ഀ
  `cart_id` int NOT NULL AUTO_INCREMENT,਍ഀ
  `user_id` int NOT NULL,਍ഀ
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,਍ഀ
  PRIMARY KEY (`cart_id`),਍ഀ
  UNIQUE KEY `user_id` (`user_id`),਍ഀ
  CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE਍ഀ
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;਍ഀ
/*!40101 SET character_set_client = @saved_cs_client */;਍ഀ
਍ഀ
--਍ഀ
-- Dumping data for table `cart`਍ഀ
--਍ഀ
਍ഀ
LOCK TABLES `cart` WRITE;਍ഀ
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;਍ഀ
INSERT INTO `cart` VALUES (1,1,'2026-05-07 02:05:18');਍ഀ
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;਍ഀ
UNLOCK TABLES;਍ഀ
਍ഀ
--਍ഀ
-- Table structure for table `cart_items`਍ഀ
--਍ഀ
਍ഀ
DROP TABLE IF EXISTS `cart_items`;਍ഀ
/*!40101 SET @saved_cs_client     = @@character_set_client */;਍ഀ
/*!50503 SET character_set_client = utf8mb4 */;਍ഀ
CREATE TABLE `cart_items` (਍ഀ
  `cart_item_id` int NOT NULL AUTO_INCREMENT,਍ഀ
  `cart_id` int NOT NULL,਍ഀ
  `variant_id` int NOT NULL,਍ഀ
  `quantity` int NOT NULL,਍ഀ
  PRIMARY KEY (`cart_item_id`),਍ഀ
  UNIQUE KEY `cart_id` (`cart_id`,`variant_id`),਍ഀ
  KEY `variant_id` (`variant_id`),਍ഀ
  CONSTRAINT `cart_items_ibfk_1` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`cart_id`) ON DELETE CASCADE,਍ഀ
  CONSTRAINT `cart_items_ibfk_2` FOREIGN KEY (`variant_id`) REFERENCES `product_variants` (`variant_id`),਍ഀ
  CONSTRAINT `cart_items_chk_1` CHECK ((`quantity` > 0))਍ഀ
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;਍ഀ
/*!40101 SET character_set_client = @saved_cs_client */;਍ഀ
਍ഀ
--਍ഀ
-- Dumping data for table `cart_items`਍ഀ
--਍ഀ
਍ഀ
LOCK TABLES `cart_items` WRITE;਍ഀ
/*!40000 ALTER TABLE `cart_items` DISABLE KEYS */;਍ഀ
/*!40000 ALTER TABLE `cart_items` ENABLE KEYS */;਍ഀ
UNLOCK TABLES;਍ഀ
਍ഀ
--਍ഀ
-- Table structure for table `categories`਍ഀ
--਍ഀ
਍ഀ
DROP TABLE IF EXISTS `categories`;਍ഀ
/*!40101 SET @saved_cs_client     = @@character_set_client */;਍ഀ
/*!50503 SET character_set_client = utf8mb4 */;਍ഀ
CREATE TABLE `categories` (਍ഀ
  `category_id` int NOT NULL AUTO_INCREMENT,਍ഀ
  `name` varchar(100) NOT NULL,਍ഀ
  PRIMARY KEY (`category_id`),਍ഀ
  UNIQUE KEY `name` (`name`)਍ഀ
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;਍ഀ
/*!40101 SET character_set_client = @saved_cs_client */;਍ഀ
਍ഀ
--਍ഀ
-- Dumping data for table `categories`਍ഀ
--਍ഀ
਍ഀ
LOCK TABLES `categories` WRITE;਍ഀ
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;਍ഀ
INSERT INTO `categories` VALUES (10,'Accessories'),(12,'Ethnic Wear'),(11,'Footwear'),(9,'Kids'),(1,'Men'),(2,'Women');਍ഀ
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;਍ഀ
UNLOCK TABLES;਍ഀ
਍ഀ
--਍ഀ
-- Table structure for table `order_items`਍ഀ
--਍ഀ
਍ഀ
DROP TABLE IF EXISTS `order_items`;਍ഀ
/*!40101 SET @saved_cs_client     = @@character_set_client */;਍ഀ
/*!50503 SET character_set_client = utf8mb4 */;਍ഀ
CREATE TABLE `order_items` (਍ഀ
  `order_item_id` int NOT NULL AUTO_INCREMENT,਍ഀ
  `order_id` int NOT NULL,਍ഀ
  `variant_id` int NOT NULL,਍ഀ
  `quantity` int NOT NULL,਍ഀ
  `price` decimal(10,2) NOT NULL,਍ഀ
  PRIMARY KEY (`order_item_id`),਍ഀ
  KEY `order_id` (`order_id`),਍ഀ
  KEY `variant_id` (`variant_id`),਍ഀ
  CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE,਍ഀ
  CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`variant_id`) REFERENCES `product_variants` (`variant_id`),਍ഀ
  CONSTRAINT `order_items_chk_1` CHECK ((`quantity` > 0))਍ഀ
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;਍ഀ
/*!40101 SET character_set_client = @saved_cs_client */;਍ഀ
਍ഀ
--਍ഀ
-- Dumping data for table `order_items`਍ഀ
--਍ഀ
਍ഀ
LOCK TABLES `order_items` WRITE;਍ഀ
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;਍ഀ
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;਍ഀ
UNLOCK TABLES;਍ഀ
਍ഀ
--਍ഀ
-- Table structure for table `orders`਍ഀ
--਍ഀ
਍ഀ
DROP TABLE IF EXISTS `orders`;਍ഀ
/*!40101 SET @saved_cs_client     = @@character_set_client */;਍ഀ
/*!50503 SET character_set_client = utf8mb4 */;਍ഀ
CREATE TABLE `orders` (਍ഀ
  `order_id` int NOT NULL AUTO_INCREMENT,਍ഀ
  `user_id` int NOT NULL,਍ഀ
  `address_id` int NOT NULL,਍ഀ
  `total_amount` decimal(10,2) NOT NULL,਍ഀ
  `status` varchar(50) DEFAULT 'PLACED',਍ഀ
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,਍ഀ
  PRIMARY KEY (`order_id`),਍ഀ
  KEY `user_id` (`user_id`),਍ഀ
  KEY `address_id` (`address_id`),਍ഀ
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),਍ഀ
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`address_id`) REFERENCES `addresses` (`address_id`)਍ഀ
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;਍ഀ
/*!40101 SET character_set_client = @saved_cs_client */;਍ഀ
਍ഀ
--਍ഀ
-- Dumping data for table `orders`਍ഀ
--਍ഀ
਍ഀ
LOCK TABLES `orders` WRITE;਍ഀ
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;਍ഀ
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;਍ഀ
UNLOCK TABLES;਍ഀ
਍ഀ
--਍ഀ
-- Table structure for table `product_variants`਍ഀ
--਍ഀ
਍ഀ
DROP TABLE IF EXISTS `product_variants`;਍ഀ
/*!40101 SET @saved_cs_client     = @@character_set_client */;਍ഀ
/*!50503 SET character_set_client = utf8mb4 */;਍ഀ
CREATE TABLE `product_variants` (਍ഀ
  `variant_id` int NOT NULL AUTO_INCREMENT,਍ഀ
  `product_id` int NOT NULL,਍ഀ
  `size` varchar(10) NOT NULL,਍ഀ
  `color` varchar(50) NOT NULL,਍ഀ
  `stock` int NOT NULL DEFAULT '0',਍ഀ
  PRIMARY KEY (`variant_id`),਍ഀ
  UNIQUE KEY `product_id` (`product_id`,`size`,`color`),਍ഀ
  CONSTRAINT `product_variants_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE਍ഀ
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;਍ഀ
/*!40101 SET character_set_client = @saved_cs_client */;਍ഀ
਍ഀ
--਍ഀ
-- Dumping data for table `product_variants`਍ഀ
--਍ഀ
਍ഀ
LOCK TABLES `product_variants` WRITE;਍ഀ
/*!40000 ALTER TABLE `product_variants` DISABLE KEYS */;਍ഀ
INSERT INTO `product_variants` VALUES (1,1,'M','Black',25),(2,1,'L','Black',30),(3,2,'30','Blue',20),(4,2,'32','Blue',25),(5,3,'M','Grey',15),(6,3,'L','Grey',20),(7,4,'M','White',18),(8,4,'L','White',22),(9,5,'S','Floral',12),(10,5,'M','Floral',15),(11,6,'S','White',20),(12,6,'M','Black',20),(13,7,'28','Blue',15),(14,7,'30','Blue',18),(15,8,'S','Pink',10),(16,8,'M','Pink',15),(17,9,'S','Printed',25),(18,9,'M','Printed',25),(19,10,'S','Denim',20),(20,10,'M','Denim',20),(21,11,'Standard','Brown',50),(22,11,'Standard','Black',50),(23,12,'Standard','Tan',30),(24,12,'Standard','Black',30),(25,13,'M','Black',20),(26,13,'L','Black',25),(27,14,'S','Striped',15),(28,14,'M','Striped',15),(29,15,'Standard','Black',35),(30,15,'Standard','Brown',25),(31,16,'M','White',40),(32,16,'L','Black',40),(35,17,'Free Size','Emerald Green',20),(36,18,'UK 8','Tan Brown',15),(37,18,'UK 9','Tan Brown',15),(38,18,'UK 10','Tan Brown',10),(39,19,'M','Cream',10),(40,19,'L','Cream',15),(41,19,'XL','Cream',10),(42,20,'UK 6','Magenta',12),(43,20,'UK 7','Magenta',12),(44,21,'UK 7','Dark Brown',15),(45,21,'UK 8','Dark Brown',20),(46,21,'UK 9','Dark Brown',15),(47,22,'Kids 1','Emerald Green',10),(48,22,'Kids 2','Emerald Green',15),(49,22,'Kids 3','Emerald Green',12),(50,23,'UK 8','Dark Grey',20),(51,23,'UK 9','Dark Grey',25),(52,23,'UK 10','Dark Grey',15),(53,24,'UK 5','Grey Rose',15),(54,24,'UK 6','Grey Rose',20),(55,24,'UK 7','Grey Rose',15);਍ഀ
/*!40000 ALTER TABLE `product_variants` ENABLE KEYS */;਍ഀ
UNLOCK TABLES;਍ഀ
਍ഀ
--਍ഀ
-- Table structure for table `products`਍ഀ
--਍ഀ
਍ഀ
DROP TABLE IF EXISTS `products`;਍ഀ
/*!40101 SET @saved_cs_client     = @@character_set_client */;਍ഀ
/*!50503 SET character_set_client = utf8mb4 */;਍ഀ
CREATE TABLE `products` (਍ഀ
  `product_id` int NOT NULL AUTO_INCREMENT,਍ഀ
  `name` varchar(150) NOT NULL,਍ഀ
  `brand` varchar(100) DEFAULT 'Aura',਍ഀ
  `description` text,਍ഀ
  `price` decimal(10,2) NOT NULL,਍ഀ
  `category_id` int NOT NULL,਍ഀ
  `is_featured` tinyint(1) DEFAULT '1',਍ഀ
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,਍ഀ
  `image_url` varchar(500) DEFAULT NULL,਍ഀ
  PRIMARY KEY (`product_id`),਍ഀ
  KEY `category_id` (`category_id`),਍ഀ
  CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`)਍ഀ
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;਍ഀ
/*!40101 SET character_set_client = @saved_cs_client */;਍ഀ
਍ഀ
--਍ഀ
-- Dumping data for table `products`਍ഀ
--਍ഀ
਍ഀ
LOCK TABLES `products` WRITE;਍ഀ
/*!40000 ALTER TABLE `products` DISABLE KEYS */;਍ഀ
INSERT INTO `products` (`product_id`, `name`, `description`, `price`, `category_id`, `created_at`, `image_url`) VALUES (1,'Black Oversized Tee','Premium heavy-cotton black oversized t-shirt with modern streetwear fit.',1500.00,1,'2026-09-20 05:00:33','assets/images/products/1.png'),(2,'501 Original Fit Men\'s Jeans','Classic straight leg original fit denim jeans with button fly.',3500.00,1,'2026-09-20 05:00:33','assets/images/products/2.png'),(3,'Grey Unisex Hoodie','Cozy fleece unisex hoodie in heather grey with kangaroo pocket.',2800.00,1,'2026-09-20 05:00:33','assets/images/products/3.png'),(4,'White Oxford Shirt Men','Classic crisp white oxford button-down shirt for formal and casual wear.',2100.00,1,'2026-09-20 05:00:33','assets/images/products/4.png'),(5,'A Floral Print Cami Dress','Elegant summer cami dress featuring a delicate floral print pattern.',3400.00,2,'2026-09-20 05:00:33','assets/images/products/5.png'),(6,'Women Tank Top','Ribbed cotton stretch tank top in a modern comfortable fit.',1200.00,2,'2026-09-20 05:00:33','assets/images/products/6.png'),(7,'511 Slim Fit Women\'s Jeans','Slim fit stretch denim jeans designed for modern everyday style.',3200.00,2,'2026-09-20 05:00:33','assets/images/products/7.png'),(8,'Pink Women\'s Hoodie','Soft pastel pink cropped hoodie with premium inner fleece lining.',2900.00,2,'2026-09-20 05:00:33','assets/images/products/8.png'),(9,'Printed Kids T-Shirt','Fun graphic printed cotton t-shirt for kids.',900.00,9,'2026-09-20 05:00:33','assets/images/products/9.png'),(10,'Kids Denim Shorts','Durable and comfortable denim shorts with elastic waistband for kids.',1400.00,9,'2026-09-20 05:00:33','assets/images/products/10.png'),(11,'Men\'s Wallet','Genuine bi-fold leather wallet with multiple card slots and bill compartment.',1600.00,10,'2026-09-20 05:00:33','assets/images/products/11.png'),(12,'A Flap-Over Backpack','Stylish flap-over canvas and leather backpack for daily travel.',3800.00,10,'2026-09-20 05:00:33','assets/images/products/12.png'),(13,'Black Men\'s Hoodie','Classic black fleece pullover hoodie with drawstrings.',3100.00,1,'2026-09-20 05:00:33','assets/images/products/13.png'),(14,'A Striped Wrap Dress','Sophisticated vertical striped wrap dress with tie waist.',3300.00,2,'2026-09-20 05:00:33','assets/images/products/14.png'),(15,'Faux Leather Backpack','Sleek water-resistant faux leather urban laptop backpack.',4200.00,10,'2026-09-20 05:00:33','assets/images/products/15.png'),(16,'Men Basic Tee','Essential solid crewneck cotton basic t-shirt for everyday layering.',1100.00,1,'2026-09-20 05:00:33','assets/images/products/16.png'),(17,'Women\'s Emerald Silk Saree','Exquisite emerald green Banarasi silk saree with gold zari woven border.',4999.00,12,'2026-09-20 06:34:42','assets/images/products/17.png'),(18,'Men\'s Heritage Leather Loafers','Handcrafted tan brown leather loafers featuring textured vamp and horsebit hardware.',3499.00,11,'2026-09-20 06:34:42','assets/images/products/18.png'),(19,'Men\'s Jaipur Silk Sherwani','Royal cream hand-embroidered silk sherwani ensemble with rich maroon stole.',8999.00,12,'2026-09-20 06:34:42','assets/images/products/19.png'),(20,'Royal Zardozi Velvet Mules','Luxury magenta velvet block-heel mules embellished with silver Zardozi embroidery and pearls.',3999.00,11,'2026-09-20 06:34:42','assets/images/products/20.png'),(21,'Unisex Heritage Leather Sandals','Comfortable dark brown suede leather sandals with brass buckle straps and ergonomic cork footbed.',2499.00,11,'2026-09-20 06:38:07','assets/images/products/21.png'),(22,'Kids Quilted Velvet Loafers','Ultra-soft emerald green quilted velvet loafers with plush lining and slip-resistant rubber sole.',1999.00,9,'2026-09-20 06:38:07','assets/images/products/22.png'),(23,'Men\'s Pro Cushion Running Shoes','Breathable dark grey and royal blue performance running sneakers with high-rebound foam soles.',3999.00,11,'2026-09-20 06:38:07','assets/images/products/23.png'),(24,'Women\'s Air Cushion Running Shoes','Lightweight grey and rose-gold mesh running shoes with responsive air cushion heel support.',3799.00,11,'2026-09-20 06:38:07','assets/images/products/24.png');਍ഀ
/*!40000 ALTER TABLE `products` ENABLE KEYS */;਍ഀ
UNLOCK TABLES;਍ഀ
਍ഀ
--਍ഀ
-- Table structure for table `reviews`਍ഀ
--਍ഀ
਍ഀ
DROP TABLE IF EXISTS `reviews`;਍ഀ
/*!40101 SET @saved_cs_client     = @@character_set_client */;਍ഀ
/*!50503 SET character_set_client = utf8mb4 */;਍ഀ
CREATE TABLE `reviews` (਍ഀ
  `id` int NOT NULL AUTO_INCREMENT,਍ഀ
  `user_id` int NOT NULL,਍ഀ
  `product_id` int NOT NULL,਍ഀ
  `rating` int NOT NULL,਍ഀ
  `review_text` text NOT NULL,਍ഀ
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,਍ഀ
  PRIMARY KEY (`id`),਍ഀ
  KEY `user_id` (`user_id`),਍ഀ
  KEY `product_id` (`product_id`),਍ഀ
  CONSTRAINT `reviews_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),਍ഀ
  CONSTRAINT `reviews_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)਍ഀ
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;਍ഀ
/*!40101 SET character_set_client = @saved_cs_client */;਍ഀ
਍ഀ
--਍ഀ
-- Dumping data for table `reviews`਍ഀ
--਍ഀ
਍ഀ
LOCK TABLES `reviews` WRITE;਍ഀ
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;਍ഀ
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;਍ഀ
UNLOCK TABLES;਍ഀ
਍ഀ
--਍ഀ
-- Table structure for table `users`਍ഀ
--਍ഀ
਍ഀ
DROP TABLE IF EXISTS `users`;਍ഀ
/*!40101 SET @saved_cs_client     = @@character_set_client */;਍ഀ
/*!50503 SET character_set_client = utf8mb4 */;਍ഀ
CREATE TABLE `users` (਍ഀ
  `user_id` int NOT NULL AUTO_INCREMENT,਍ഀ
  `name` varchar(100) NOT NULL,਍ഀ
  `email` varchar(150) NOT NULL,਍ഀ
  `password_hash` varchar(255) NOT NULL,਍ഀ
  `phone` varchar(20) DEFAULT NULL,਍ഀ
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,਍ഀ
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,਍ഀ
  PRIMARY KEY (`user_id`),਍ഀ
  UNIQUE KEY `email` (`email`)਍ഀ
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;਍ഀ
/*!40101 SET character_set_client = @saved_cs_client */;਍ഀ
਍ഀ
--਍ഀ
-- Dumping data for table `users`਍ഀ
--਍ഀ
਍ഀ
LOCK TABLES `users` WRITE;਍ഀ
/*!40000 ALTER TABLE `users` DISABLE KEYS */;਍ഀ
INSERT INTO `users` VALUES (1,'mohammed faisal d','mohammedfaisald2003@gmail.com','Faisal@2003','8660402506','2026-05-07 01:39:39','2026-05-07 01:39:39'),(2,'Mohammed Faisal. D','faisal@gmail.com','123456','8660402506','2026-09-20 02:56:58','2026-09-20 02:56:58');਍ഀ
/*!40000 ALTER TABLE `users` ENABLE KEYS */;਍ഀ
UNLOCK TABLES;਍ഀ
਍ഀ
--਍ഀ
-- Table structure for table `wishlist`਍ഀ
--਍ഀ
਍ഀ
DROP TABLE IF EXISTS `wishlist`;਍ഀ
/*!40101 SET @saved_cs_client     = @@character_set_client */;਍ഀ
/*!50503 SET character_set_client = utf8mb4 */;਍ഀ
CREATE TABLE `wishlist` (਍ഀ
  `id` int NOT NULL AUTO_INCREMENT,਍ഀ
  `user_id` int NOT NULL,਍ഀ
  `product_id` int NOT NULL,਍ഀ
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,਍ഀ
  PRIMARY KEY (`id`),਍ഀ
  KEY `user_id` (`user_id`),਍ഀ
  KEY `product_id` (`product_id`),਍ഀ
  CONSTRAINT `wishlist_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),਍ഀ
  CONSTRAINT `wishlist_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)਍ഀ
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;਍ഀ
/*!40101 SET character_set_client = @saved_cs_client */;਍ഀ
਍ഀ
--਍ഀ
-- Dumping data for table `wishlist`਍ഀ
--਍ഀ
਍ഀ
LOCK TABLES `wishlist` WRITE;਍ഀ
/*!40000 ALTER TABLE `wishlist` DISABLE KEYS */;਍ഀ
/*!40000 ALTER TABLE `wishlist` ENABLE KEYS */;਍ഀ
UNLOCK TABLES;਍ഀ
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;਍ഀ
਍ഀ
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;਍ഀ
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;਍ഀ
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;਍ഀ
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;਍ഀ
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;਍ഀ
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;਍ഀ
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;਍ഀ
਍ഀ
-- Dump completed on 2026-09-20 12:09:15਍ഀ
