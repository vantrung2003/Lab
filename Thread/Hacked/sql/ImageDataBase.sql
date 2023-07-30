CREATE DATABASE IF NOT EXISTS `ImageDataBase` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `ImageDataBase`;

CREATE TABLE IF NOT EXISTS `ImageTable` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `title` TINYTEXT NOT NULL,
    `image` LONGTEXT NOT NULL,
    `copyright` VARCHAR(255),
    `date` DATE,
    `explanation` TEXT,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
