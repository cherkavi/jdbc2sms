-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 03, 2015 at 08:58 AM
-- Server version: 5.5.40-0ubuntu0.14.04.1
-- PHP Version: 5.5.9-1ubuntu4.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `sms_center`
--

-- --------------------------------------------------------

--
-- Table structure for table `sms_center`
--

CREATE TABLE IF NOT EXISTS `sms_center` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text` varchar(800) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `recipient` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `sendtime` timestamp NULL DEFAULT NULL,
  `sendresult` tinyint(1) DEFAULT NULL,
  `senderrorcode` int(11) DEFAULT NULL,
  `deliverytime` timestamp NULL DEFAULT NULL,
  `deliveryresult` tinyint(1) DEFAULT NULL,
  `deliverystatus` int(11) DEFAULT NULL,
  `createdat` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Dumping data for table `sms_center`
--

INSERT INTO `sms_center` (`id`, `text`, `recipient`, `sendtime`, `sendresult`, `senderrorcode`, `deliverytime`, `deliveryresult`, `deliverystatus`, `createdat`) VALUES
(2, 'working', '+380954670000', '2015-02-03 06:19:25', 1, NULL, '2015-02-03 06:24:25', 1, NULL, '2015-02-03 06:19:23'),
(3, 'working2', '+380954670000', '2015-02-03 06:38:06', 0, NULL, NULL, NULL, NULL, '2015-02-03 06:33:26'),
(4, 'working3', '+380954670000', '2015-02-03 06:49:00', 0, 2, NULL, NULL, NULL, '2015-02-03 06:48:55'),
(5, 'working4', '+380954670000', '2015-02-03 06:53:06', 1, NULL, '2015-02-03 06:55:06', 1, NULL, '2015-02-03 06:53:01'),
(6, 'working5', '+380954670000', '2015-02-03 06:58:07', 1, NULL, '2015-02-03 06:58:16', 1, NULL, '2015-02-03 06:58:05');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
