-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               11.3.2-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERbookO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SELECT * FROM account

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `account` (`id`, `account_non_locked`, `email`, `failed_attempt`, `is_enable`, `lock_time`, `name`, `password`, `pincode`, `profile_image`, `reset_token`, `role`) VALUES
	(1, b'1', 'manhtri@gmail.com', 0, b'1', NULL, 'tri', '$2a$10$pLyNMqI6jh8O4xdDo0zEzep4E8O/lNNd2yGtWve.yftzZabNwAPqy', '1111', 'default.jpg', '0e21dda8-22d5-4422-b82c-a71e807e39b7', 'ROLE_ADMIN'),
	(2, b'1', 'men@gmail.com', 0, b'1', NULL, 'men', '$2a$10$5YGsb98Swg48911TZMy1.O7nbg4wkwI7Je54x6XmKX5eWG7uwDS0G', '123', 'default.jpg', 'a1420be1-5679-4d08-a19b-f139be21c795', 'ROLE_USER');

CREATE TABLE IF NOT EXISTS `cart` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `quantity` int(11) DEFAULT NULL,
  `book_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3d704slv66tw6x5hmbm6p2x3u` (`book_id`),
  KEY `FKkv2m9ommx9qx1dy233gr8egxs` (`user_id`),
  CONSTRAINT `FK3d704slv66tw6x5hmbm6p2x3u` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
  CONSTRAINT `FKkv2m9ommx9qx1dy233gr8egxs` FOREIGN KEY (`user_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



CREATE TABLE IF NOT EXISTS `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `image_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `category` (`id`, `image_name`, `name`) VALUES
	(1, 'hoctap1.png', 'HocTap'),
	(3, 'giaitri1.png', 'GiaiTri'),
	(4, 'kynang1.png', 'KyNang');

-- Dumping structure for table sport.order_address
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;




CREATE TABLE IF NOT EXISTS `book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `author` varchar(5000) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `title` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


INSERT INTO `book` (`id`, `category`, `author`, `image`, `price`, `title`) 
VALUES 
(1, 'HocTap', 'John Nguyen', 'hoctap1.jpg', 1000, 'Người thông minh học tập'),
(2, 'HocTap', 'John Nguyen', 'hoctap2.jpg', 1500, 'Học Tập suốt đời'),
(3, 'HocTap', 'David Le', 'hoctap3.jpg', 1000, 'Tôi tự học'),
(4, 'GiaiTri', 'David Le', 'giaitri1.png', 1234, 'Hoàng tử bé'),
(5, 'GiaiTri', 'Brian Dang', 'giaitri2.png', 1231, 'Chuyện nhỏ trong thế giới lớn'),
(6, 'KyNang', 'Brian Dang', 'kynang1.png', 5121, 'Quản lý thời gian'),
(7, 'KyNang', 'Kevin Hoang', 'kynang2.png', 5121, 'Muốn thành công nói không trì hoãn');



CREATE TABLE IF NOT EXISTS `book_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_date` datetime(6) DEFAULT NULL,
  `order_id` varchar(255) DEFAULT NULL,
  `payment_type` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `order_address_id` int(11) DEFAULT NULL,
  `book_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKqcdbxaeuc7c5gahwh0dutg04r` (`order_address_id`),
  KEY `FKh73acsd9s5wp6l0e55td6jr1m` (`book_id`),
  KEY `FKky74ym4tihx2a5v65ncsm1jfj` (`user_id`),
  CONSTRAINT `FK8frxalwc79tpxo7hgqp3hsjck` FOREIGN KEY (`order_address_id`) REFERENCES `order_address` (`id`),
  CONSTRAINT `FKh73acsd9s5wp6l0e55td6jr1m` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
  CONSTRAINT `FKky74ym4tihx2a5v65ncsm1jfj` FOREIGN KEY (`user_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
