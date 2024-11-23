-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               11.5.2-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for book
CREATE DATABASE IF NOT EXISTS `book` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci */;
USE `book`;

-- Dumping structure for table book.account
CREATE TABLE IF NOT EXISTS `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_non_locked` bit(1) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `failed_attempt` int(11) DEFAULT NULL,
  `is_enable` bit(1) DEFAULT NULL,
  `lock_time` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `pincode` varchar(255) DEFAULT NULL,
  `profile_image` varchar(255) DEFAULT NULL,
  `reset_token` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKq0uja26qgu1atulenwup9rxyr` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Dumping data for table book.account: ~2 rows (approximately)
INSERT INTO `account` (`id`, `account_non_locked`, `email`, `failed_attempt`, `is_enable`, `lock_time`, `name`, `password`, `pincode`, `profile_image`, `reset_token`, `role`) VALUES
	(1, b'1', 'manhtri@gmail.com', 0, b'1', NULL, 'tri', '$2a$10$pLyNMqI6jh8O4xdDo0zEzep4E8O/lNNd2yGtWve.yftzZabNwAPqy', '1111', 'default.jpg', '0e21dda8-22d5-4422-b82c-a71e807e39b7', 'ROLE_ADMIN'),
	(2, b'1', 'men@gmail.com', 0, b'1', NULL, 'men', '$2a$10$5YGsb98Swg48911TZMy1.O7nbg4wkwI7Je54x6XmKX5eWG7uwDS0G', '123', 'default.jpg', 'a1420be1-5679-4d08-a19b-f139be21c795', 'ROLE_USER');

-- Dumping structure for table book.book
CREATE TABLE IF NOT EXISTS `book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `author` varchar(500) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `title` varchar(500) DEFAULT NULL,
  `stock` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Dumping data for table book.book: ~7 rows (approximately)
INSERT INTO `book` (`id`, `author`, `category`, `image`, `price`, `title`, `stock`) VALUES
	(1, 'John Nguyen', 'HocTap', 'hoctap1.jpg', 1000, 'Người thông minh học tập', 5),
	(2, 'John Nguyen', 'HocTap', 'hoctap2.jpg', 1500, 'Học Tập suốt đời', 10),
	(3, 'David Le', 'HocTap', 'hoctap3.jpg', 1000, 'Tôi tự học', 15),
	(4, 'David Le', 'GiaiTri', 'giaitri1.png', 1234, 'Hoàng tử bé', 20),
	(5, 'Brian Dang', 'GiaiTri', 'giaitri2.png', 1231, 'Chuyện nhỏ trong thế giới lớn', 25),
	(6, 'Brian Dang', 'KyNang', 'kynang1.png', 5121, 'Quản lý thời gian', 30),
	(7, 'Kevin Hoang', 'KyNang', 'kynang2.png', 5121, 'Muốn thành công nói không trì hoãn', 35);

-- Dumping structure for table book.book_order
CREATE TABLE IF NOT EXISTS `book_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_date` datetime(6) DEFAULT NULL,
  `order_id` varchar(255) DEFAULT NULL,
  `payment_type` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `book_id` int(11) DEFAULT NULL,
  `order_address_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKp7dv7vbk8fhpdogu50o2y6w2s` (`order_address_id`),
  KEY `FKqkpd2wfya302b4kxa2av7lgsw` (`book_id`),
  KEY `FKe9my1kcpc7a33h0u4msu3fbc6` (`user_id`),
  CONSTRAINT `FKe9my1kcpc7a33h0u4msu3fbc6` FOREIGN KEY (`user_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FKoutcqh21jkfhrjmqoq0bjfhfp` FOREIGN KEY (`order_address_id`) REFERENCES `order_address` (`id`),
  CONSTRAINT `FKqkpd2wfya302b4kxa2av7lgsw` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Dumping data for table book.book_order: ~0 rows (approximately)

-- Dumping structure for table book.cart
CREATE TABLE IF NOT EXISTS `cart` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `quantity` int(11) DEFAULT NULL,
  `book_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5n0sq8ulj6ykdnrh4dnk0vfmw` (`book_id`),
  KEY `FKi82jd7qudsyjsnq2tcct3q11u` (`user_id`),
  KEY `FKo1lbdxogc1klywc9kk1rum80t` (`product_id`),
  CONSTRAINT `FK5n0sq8ulj6ykdnrh4dnk0vfmw` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
  CONSTRAINT `FKi82jd7qudsyjsnq2tcct3q11u` FOREIGN KEY (`user_id`) REFERENCES `book` (`id`),
  CONSTRAINT `FKkv2m9ommx9qx1dy233gr8egxs` FOREIGN KEY (`user_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FKo1lbdxogc1klywc9kk1rum80t` FOREIGN KEY (`product_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Dumping data for table book.cart: ~2 rows (approximately)
INSERT INTO `cart` (`id`, `quantity`, `book_id`, `user_id`, `product_id`) VALUES
	(1, 8, 3, 2, NULL),
	(2, 5, 4, 2, NULL);

-- Dumping structure for table book.category
CREATE TABLE IF NOT EXISTS `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `image_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Dumping data for table book.category: ~3 rows (approximately)
INSERT INTO `category` (`id`, `image_name`, `name`) VALUES
	(1, 'caulong.png', 'HocTap'),
	(3, 'Bongda.png', 'GiaiTri'),
	(4, 'Bongro.png', 'KyNang');

-- Dumping structure for table book.order_address
CREATE TABLE IF NOT EXISTS `order_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `mobile_no` varchar(255) DEFAULT NULL,
  `pincode` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Dumping data for table book.order_address: ~0 rows (approximately)

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
