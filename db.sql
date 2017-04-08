-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Apr 08, 2017 at 06:38 PM
-- Server version: 5.5.25
-- PHP Version: 5.3.13

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `bioimageapp`
--

-- --------------------------------------------------------

--
-- Table structure for table `processinginputvalues`
--

CREATE TABLE IF NOT EXISTS `processinginputvalues` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `histogram` int(255) NOT NULL,
  `red` int(255) NOT NULL,
  `green` int(255) NOT NULL,
  `blue` int(245) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=113 ;

CREATE TABLE IF NOT EXISTS `processingoutputvalues` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `input_id` int(11) NOT NULL,
  `img_name` varchar(255) NOT NULL,
  `distance` double NOT NULL,
  `frag` double NOT NULL,
  `lowthreshlevel` int(11) NOT NULL,
  `human_evaluate` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=118 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
