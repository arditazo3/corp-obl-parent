-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 14, 2018 at 09:26 PM
-- Server version: 10.1.26-MariaDB
-- PHP Version: 7.1.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `corporate_obligations`
--

-- --------------------------------------------------------

--
-- Table structure for table `co_company`
--

CREATE TABLE `co_company` (
  `id` int(11) NOT NULL COMMENT 'The PK of the TABLE ',
  `description` varchar(100) NOT NULL COMMENT 'The description',
  `enabled` tinyint(1) NOT NULL COMMENT 'Indicate the STATUS of company:Active OR NOT Active',
  `creationdate` datetime NOT NULL COMMENT 'DATE of creation',
  `createdby` varchar(100) NOT NULL COMMENT 'The CORPOBLIG_USER that has inserted the record',
  `modificationdate` datetime NOT NULL,
  `modifiedby` varchar(100) NOT NULL COMMENT 'The CORPOBLIG_USER that made the LAST modification of the record'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='The TABLE company';

--
-- Dumping data for table `co_company`
--

INSERT INTO `co_company` (`id`, `description`, `enabled`, `creationdate`, `createdby`, `modificationdate`, `modifiedby`) VALUES
(30, 'ABC Foreign Company', 1, '2018-10-14 18:09:44', 'FOREIGN', '2018-10-14 18:09:44', 'FOREIGN'),
(31, 'BBC Inland Company', 1, '2018-10-14 18:28:54', 'INLAND', '2018-10-14 18:28:54', 'INLAND');

-- --------------------------------------------------------

--
-- Table structure for table `co_companyconsultant`
--

CREATE TABLE `co_companyconsultant` (
  `id` int(11) NOT NULL COMMENT 'The PK of the TABLE ',
  `company_id` int(11) NOT NULL COMMENT 'FK of the TABLE Company',
  `name` varchar(255) NOT NULL COMMENT 'Company NAME / NAME ',
  `email` varchar(255) NOT NULL COMMENT 'The email',
  `phone1` varchar(30) DEFAULT NULL COMMENT 'Phone NO .1',
  `phone2` varchar(30) DEFAULT NULL COMMENT 'Phone NO .2',
  `enabled` tinyint(1) NOT NULL COMMENT 'The STATUS :Active OR NOT active',
  `creationdate` datetime NOT NULL COMMENT 'The DATE of creation of the record',
  `createdby` varchar(100) NOT NULL COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  `modificationdate` datetime NOT NULL COMMENT 'The DATE of the creation of the record',
  `modifiedby` varchar(100) NOT NULL COMMENT 'That CORPOBLIG_USER that has made the LAST modification'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='The TABLE Company Consultant';

--
-- Dumping data for table `co_companyconsultant`
--

INSERT INTO `co_companyconsultant` (`id`, `company_id`, `name`, `email`, `phone1`, `phone2`, `enabled`, `creationdate`, `createdby`, `modificationdate`, `modifiedby`) VALUES
(29, 30, 'ABC Consultant 1', 'consultant1@constultant.com', '123456789', '987654321', 1, '2018-10-14 18:17:08', 'FOREIGN', '2018-10-14 18:17:08', 'FOREIGN'),
(30, 30, 'ABC Consultant 2', 'consultant2@constultant.com', '1111112', '2222221', 1, '2018-10-14 18:17:35', 'FOREIGN', '2018-10-14 18:17:35', 'FOREIGN'),
(31, 31, 'BBC Consultant 1', 'consultant1@bbc.com', '1111113', '2222222', 1, '2018-10-14 18:32:21', 'INLAND', '2018-10-14 18:32:21', 'INLAND'),
(32, 31, 'BBC Consultant 2', 'consultant2@bbc.com', '123123', '321321', 1, '2018-10-14 18:32:49', 'INLAND', '2018-10-14 18:32:49', 'INLAND');

-- --------------------------------------------------------

--
-- Table structure for table `co_companytopic`
--

CREATE TABLE `co_companytopic` (
  `id` int(11) NOT NULL COMMENT 'The Pk of the TABLE ',
  `topic_id` int(11) NOT NULL COMMENT 'FK of the TABLE Topic',
  `company_id` int(11) NOT NULL COMMENT 'FK of the TABLE Company',
  `enabled` tinyint(1) NOT NULL COMMENT 'The STATUS :Active OR NOT active',
  `creationdate` datetime NOT NULL COMMENT 'The DATE of creation of the record',
  `createdby` varchar(100) NOT NULL COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  `modificationdate` datetime NOT NULL COMMENT 'The DATE of the creation of the record',
  `modifiedby` varchar(100) NOT NULL COMMENT 'That CORPOBLIG_USER that has made the LAST modification'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='The TABLE of association BETWEEN Office AND Topic';

--
-- Dumping data for table `co_companytopic`
--

INSERT INTO `co_companytopic` (`id`, `topic_id`, `company_id`, `enabled`, `creationdate`, `createdby`, `modificationdate`, `modifiedby`) VALUES
(11, 9, 30, 1, '2018-10-14 18:15:37', 'FOREIGN', '2018-10-14 18:15:37', 'FOREIGN'),
(12, 10, 30, 1, '2018-10-14 18:16:17', 'FOREIGN', '2018-10-14 18:16:17', 'FOREIGN'),
(13, 11, 31, 1, '2018-10-14 18:31:14', 'INLAND', '2018-10-14 18:31:14', 'INLAND'),
(14, 12, 31, 1, '2018-10-14 18:31:30', 'INLAND', '2018-10-14 18:31:30', 'INLAND');

-- --------------------------------------------------------

--
-- Table structure for table `co_companyuser`
--

CREATE TABLE `co_companyuser` (
  `id` int(11) NOT NULL COMMENT 'The PK of the TABLE ',
  `username` varchar(100) NOT NULL COMMENT 'The FK of the TABLE CORPOBLIG_USER ',
  `company_id` int(11) NOT NULL COMMENT 'The FK of the TABLE Company',
  `companyadmin` tinyint(1) NOT NULL COMMENT 'Indicates whether it IS a company administrator OR NOT ',
  `enabled` tinyint(1) NOT NULL COMMENT 'The STATUS :Active OR NOT active',
  `creationdate` datetime NOT NULL COMMENT 'The creaation DATE of the record',
  `createdby` varchar(100) NOT NULL COMMENT 'The CORPOBLIG_USER that has created the record',
  `modificationdate` datetime NOT NULL COMMENT 'The DATE of creation',
  `modifiedby` varchar(100) NOT NULL COMMENT 'The CORPOBLIG_USER that has made the LAST modification'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='The junction TABLE of the CORPOBLIG_USER AND Company';

--
-- Dumping data for table `co_companyuser`
--

INSERT INTO `co_companyuser` (`id`, `username`, `company_id`, `companyadmin`, `enabled`, `creationdate`, `createdby`, `modificationdate`, `modifiedby`) VALUES
(121, 'FOREIGN', 30, 1, 1, '2018-10-14 18:09:44', 'FOREIGN', '2018-10-14 18:09:44', 'FOREIGN'),
(122, 'USER3', 30, 1, 1, '2018-10-14 18:09:44', 'FOREIGN', '2018-10-14 18:09:44', 'FOREIGN'),
(123, 'USER2', 30, 1, 1, '2018-10-14 18:09:44', 'FOREIGN', '2018-10-14 18:09:44', 'FOREIGN'),
(124, 'INLAND', 31, 1, 1, '2018-10-14 18:28:54', 'INLAND', '2018-10-14 18:28:54', 'INLAND'),
(125, 'USER4', 31, 1, 1, '2018-10-14 18:28:54', 'INLAND', '2018-10-14 18:28:54', 'INLAND'),
(126, 'USER5', 31, 0, 1, '2018-10-14 18:28:54', 'INLAND', '2018-10-14 18:28:54', 'INLAND');

-- --------------------------------------------------------

--
-- Table structure for table `co_expiration`
--

CREATE TABLE `co_expiration` (
  `id` int(11) NOT NULL,
  `tasktemplate_id` int(11) NOT NULL COMMENT 'FK of the TABLE Task Template',
  `task_id` int(11) NOT NULL COMMENT 'FK of the TABLE Task',
  `office_id` int(11) NOT NULL COMMENT 'FK of the TABLE Office',
  `expirationclosableby` int(1) NOT NULL COMMENT '1:Anyone can CLOSE the task, 2:EACH CORPOBLIG_USER has his task',
  `username` varchar(100) NOT NULL COMMENT 'The CORPOBLIG_USER ',
  `expirationdate` datetime NOT NULL COMMENT 'The expired TIME ',
  `completed` datetime NOT NULL COMMENT 'DATE of WHEN it was declared executed ( BY the controlled)',
  `approved` datetime NOT NULL COMMENT 'DATE WHEN it was declared approved BY the controller',
  `registered` datetime NOT NULL COMMENT 'DATE WHEN it was archived BY the controller',
  `enabled` tinyint(1) NOT NULL COMMENT 'The STATUS :Active OR NOT active',
  `creationdate` datetime NOT NULL COMMENT 'The DATE of creation of the record',
  `createdby` varchar(100) NOT NULL COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  `modificationdate` datetime NOT NULL COMMENT 'The DATE of the creation of the record',
  `modifiedby` varchar(100) NOT NULL COMMENT 'That CORPOBLIG_USER that has made the LAST modification'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='The TABLE Expiration';

-- --------------------------------------------------------

--
-- Table structure for table `co_expirationactivity`
--

CREATE TABLE `co_expirationactivity` (
  `id` int(11) NOT NULL,
  `expiration_id` int(11) NOT NULL COMMENT 'FK of the TABLE Expiration',
  `body` longtext NOT NULL,
  `deleted` tinyint(1) NOT NULL,
  `creationdate` datetime NOT NULL COMMENT 'The DATE of creation of the record',
  `createdby` varchar(100) NOT NULL COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  `modificationdate` datetime NOT NULL COMMENT 'The DATE of the creation of the record',
  `modifiedby` varchar(100) NOT NULL COMMENT 'That CORPOBLIG_USER that has made the LAST modification'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='The TABLE Exiration Activity';

-- --------------------------------------------------------

--
-- Table structure for table `co_expirationactivityattachment`
--

CREATE TABLE `co_expirationactivityattachment` (
  `id` int(11) NOT NULL COMMENT 'The PK of the TABLE ',
  `expirationactivity_id` int(11) NOT NULL COMMENT 'FK of the TABLE ',
  `filename` varchar(255) NOT NULL COMMENT 'The NAME of the FILE ',
  `filetype` varchar(255) NOT NULL COMMENT 'The type of the FILE ',
  `filepath` varchar(255) NOT NULL COMMENT 'The path of the FILE ',
  `filesizze` int(11) NOT NULL COMMENT 'The size of the File',
  `createdby` varchar(100) NOT NULL COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  `modificationdate` datetime NOT NULL COMMENT 'The DATE of the creation of the record',
  `modifiedby` varchar(100) NOT NULL COMMENT 'That CORPOBLIG_USER that has made the LAST modification',
  `creationdate` datetime NOT NULL COMMENT 'The DATE of creation of the record'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='The TABLE Exiration Activity Attachment';

-- --------------------------------------------------------

--
-- Table structure for table `co_office`
--

CREATE TABLE `co_office` (
  `id` int(11) NOT NULL COMMENT 'The PK of the TABLE ',
  `company_id` int(11) NOT NULL COMMENT 'The FK of the TABLE Company',
  `description` varchar(100) NOT NULL COMMENT 'The description',
  `enabled` tinyint(1) NOT NULL COMMENT 'The STATUS :Active OR NOT active',
  `creationdate` datetime NOT NULL COMMENT 'The DATE of creation of the record',
  `createdby` varchar(100) NOT NULL COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  `modificationdate` datetime NOT NULL COMMENT 'The DATE of the creation of the record',
  `modifiedby` varchar(100) NOT NULL COMMENT 'That CORPOBLIG_USER that has made the LAST modification'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='The TABLE Office';

--
-- Dumping data for table `co_office`
--

INSERT INTO `co_office` (`id`, `company_id`, `description`, `enabled`, `creationdate`, `createdby`, `modificationdate`, `modifiedby`) VALUES
(8, 30, 'ABC Office 1', 1, '2018-10-14 18:10:07', 'FOREIGN', '2018-10-14 18:10:07', 'FOREIGN'),
(9, 30, 'ABC Office 2', 1, '2018-10-14 18:21:00', 'FOREIGN', '2018-10-14 18:21:00', 'FOREIGN'),
(10, 31, 'BBC Office 1', 1, '2018-10-14 18:29:08', 'INLAND', '2018-10-14 18:29:08', 'INLAND'),
(11, 31, 'BBC Office 2', 1, '2018-10-14 18:29:18', 'INLAND', '2018-10-14 18:29:18', 'INLAND');

-- --------------------------------------------------------

--
-- Table structure for table `co_task`
--

CREATE TABLE `co_task` (
  `id` int(11) NOT NULL COMMENT 'The PK of the TABLE ',
  `tasktemplate_id` int(11) NOT NULL COMMENT 'The FK of the TABLE Task Template',
  `recurrence` varchar(20) NOT NULL COMMENT 'Periodicity:weekly,\r\n  n  monthly,\r\n  n  yearly',
  `expirationtype` varchar(20) NOT NULL COMMENT 'Expiry type:fix_day,\r\n  n  month_start,\r\n  n  month_end',
  `day` int(11) DEFAULT NULL COMMENT 'Expiry DAY (filled IN only IN the "fix_day" CASE )',
  `daysofnotice` int(11) NOT NULL COMMENT 'Frequency of the alert ( EVERY how many days IS sent)',
  `frequenceofnotice` int(11) NOT NULL COMMENT 'Frequency of the alert (every how many days is sent)',
  `enabled` tinyint(1) NOT NULL COMMENT 'The STATUS :Active OR NOT active',
  `daysbeforeshowexpiration` int(11) NOT NULL COMMENT 'How many days BEFORE TO SHOW the deadline',
  `createdby` varchar(100) NOT NULL COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  `modificationdate` datetime NOT NULL COMMENT 'The DATE of the creation of the record',
  `modifiedby` varchar(100) NOT NULL COMMENT 'That CORPOBLIG_USER that has made the LAST modification',
  `creationdate` datetime NOT NULL COMMENT 'The DATE of creation of the record'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='The TABLE of the Task';

--
-- Dumping data for table `co_task`
--

INSERT INTO `co_task` (`id`, `tasktemplate_id`, `recurrence`, `expirationtype`, `day`, `daysofnotice`, `frequenceofnotice`, `enabled`, `daysbeforeshowexpiration`, `createdby`, `modificationdate`, `modifiedby`, `creationdate`) VALUES
(123, 64, 'monthly', 'fix_day', 2, 5, 5, 1, 10, 'FOREIGN', '2018-10-14 18:19:34', 'FOREIGN', '2018-10-14 18:19:34'),
(124, 65, 'monthly', 'month_end', 0, 5, 5, 1, 2, 'FOREIGN', '2018-10-14 18:22:13', 'FOREIGN', '2018-10-14 18:20:49'),
(125, 66, 'weekly', 'month_start', 0, 3, 3, 1, 5, 'INLAND', '2018-10-14 20:01:35', 'INLAND', '2018-10-14 19:52:53'),
(126, 67, 'monthly', 'fix_day', 4, 5, 5, 1, 8, 'INLAND', '2018-10-14 19:56:11', 'INLAND', '2018-10-14 19:56:11'),
(127, 68, 'monthly', 'month_start', 0, 5, 5, 1, 1, 'INLAND', '2018-10-14 20:03:35', 'INLAND', '2018-10-14 20:03:00');

-- --------------------------------------------------------

--
-- Table structure for table `co_taskoffice`
--

CREATE TABLE `co_taskoffice` (
  `id` int(11) NOT NULL,
  `tasktemplate_id` int(11) NOT NULL COMMENT 'The FK of the TABLE Task Template',
  `task_id` int(11) NOT NULL COMMENT 'The FK of the TABLE Task',
  `office_id` int(11) NOT NULL COMMENT 'The FK of the TABLE Office',
  `startdate` datetime NOT NULL,
  `enddate` datetime NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `createdby` varchar(100) NOT NULL COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  `modificationdate` datetime NOT NULL COMMENT 'The DATE of the creation of the record',
  `modifiedby` varchar(100) NOT NULL COMMENT 'That CORPOBLIG_USER that has made the LAST modification',
  `creationdate` datetime NOT NULL COMMENT 'The DATE of creation of the record'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='The TABLE Task Office, associated TO the Office';

--
-- Dumping data for table `co_taskoffice`
--

INSERT INTO `co_taskoffice` (`id`, `tasktemplate_id`, `task_id`, `office_id`, `startdate`, `enddate`, `enabled`, `createdby`, `modificationdate`, `modifiedby`, `creationdate`) VALUES
(161, 64, 123, 8, '2018-10-14 18:19:34', '2999-12-31 23:59:59', 1, 'FOREIGN', '2018-10-14 18:19:34', 'FOREIGN', '2018-10-14 18:19:34'),
(162, 65, 124, 9, '2018-10-14 18:22:13', '2999-12-31 23:59:59', 1, 'FOREIGN', '2018-10-14 18:22:13', 'FOREIGN', '2018-10-14 18:22:13'),
(163, 66, 125, 10, '2018-10-14 20:01:35', '2999-12-31 23:59:59', 1, 'INLAND', '2018-10-14 20:01:35', 'INLAND', '2018-10-14 20:01:35'),
(164, 67, 126, 11, '2018-10-14 19:56:11', '2999-12-31 23:59:59', 1, 'INLAND', '2018-10-14 19:56:11', 'INLAND', '2018-10-14 19:56:11'),
(165, 68, 127, 10, '2018-10-14 20:03:26', '2999-12-31 23:59:59', 1, 'INLAND', '2018-10-14 20:03:26', 'INLAND', '2018-10-14 20:03:26'),
(166, 68, 127, 11, '2018-10-14 20:03:12', '2018-10-14 20:03:19', 0, 'INLAND', '2018-10-14 20:03:12', 'INLAND', '2018-10-14 20:03:12'),
(167, 68, 127, 11, '2018-10-14 20:03:26', '2018-10-14 20:03:35', 0, 'INLAND', '2018-10-14 20:03:26', 'INLAND', '2018-10-14 20:03:26');

-- --------------------------------------------------------

--
-- Table structure for table `co_taskofficerelations`
--

CREATE TABLE `co_taskofficerelations` (
  `id` int(11) NOT NULL COMMENT 'The PK of the TABLE ',
  `taskoffice_id` int(11) NOT NULL COMMENT 'FK of the TABLE Task Office',
  `username` varchar(100) NOT NULL COMMENT 'The CORPOBLIG_USER ',
  `relationtype` int(11) NOT NULL COMMENT '1:Controller, 2:Controlled',
  `enabled` tinyint(1) NOT NULL COMMENT 'The STATUS :Active OR NOT active',
  `creationdate` datetime NOT NULL COMMENT 'The DATE of creation of the record',
  `createdby` varchar(100) NOT NULL COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  `modificationdate` datetime NOT NULL COMMENT 'The DATE of the creation of the record',
  `modifiedby` varchar(100) NOT NULL COMMENT 'That CORPOBLIG_USER that has made the LAST modification'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='The TABLE Task Office Relations, association BETWEEN Users AND the task of the Office';

--
-- Dumping data for table `co_taskofficerelations`
--

INSERT INTO `co_taskofficerelations` (`id`, `taskoffice_id`, `username`, `relationtype`, `enabled`, `creationdate`, `createdby`, `modificationdate`, `modifiedby`) VALUES
(1, 161, 'USER', 1, 1, '2018-10-14 18:19:34', 'FOREIGN', '2018-10-14 18:19:34', 'FOREIGN'),
(2, 161, 'USER2', 2, 1, '2018-10-14 18:19:34', 'FOREIGN', '2018-10-14 18:19:34', 'FOREIGN'),
(3, 161, 'USER3', 2, 1, '2018-10-14 18:19:34', 'FOREIGN', '2018-10-14 18:19:34', 'FOREIGN'),
(13, 163, 'USER4', 2, 1, '2018-10-14 20:01:35', 'INLAND', '2018-10-14 20:01:35', 'INLAND'),
(14, 163, 'USER5', 1, 1, '2018-10-14 20:01:35', 'INLAND', '2018-10-14 20:01:35', 'INLAND');

-- --------------------------------------------------------

--
-- Table structure for table `co_tasktemplate`
--

CREATE TABLE `co_tasktemplate` (
  `id` int(11) NOT NULL COMMENT 'The PK of the TABLE ',
  `topic_id` int(11) NOT NULL COMMENT 'The FK of the TABLE Topic',
  `description` varchar(100) NOT NULL COMMENT 'The description',
  `recurrence` varchar(20) NOT NULL COMMENT 'Periodicity:weekly,\r\n  n  monthly,\r\n  n  yearly',
  `expirationtype` varchar(20) NOT NULL COMMENT 'Expiry type:fix_day,\r\n  n  month_start,\r\n  n  month_end',
  `day` int(11) DEFAULT NULL COMMENT 'Expiry DAY (filled IN only IN the "fix_day" CASE )',
  `daysofnotice` int(11) NOT NULL COMMENT 'Frequency of the alert ( EVERY how many days IS sent)',
  `frequenceofnotice` int(11) NOT NULL COMMENT 'Frequency of the alert (every how many days is sent)',
  `daysbeforeshowexpiration` int(11) NOT NULL COMMENT 'How many days BEFORE TO SHOW the deadline',
  `enabled` tinyint(1) NOT NULL COMMENT 'The STATUS :Active OR NOT active',
  `expirationclosableby` int(1) NOT NULL COMMENT '1:Anyone can CLOSE the task, 2:EACH CORPOBLIG_USER has his task',
  `createdby` varchar(100) NOT NULL COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  `modificationdate` datetime NOT NULL COMMENT 'The DATE of the creation of the record',
  `modifiedby` varchar(100) NOT NULL COMMENT 'That CORPOBLIG_USER that has made the LAST modification',
  `creationdate` datetime NOT NULL COMMENT 'The DATE of creation of the record'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='The TABLE Task Template';

--
-- Dumping data for table `co_tasktemplate`
--

INSERT INTO `co_tasktemplate` (`id`, `topic_id`, `description`, `recurrence`, `expirationtype`, `day`, `daysofnotice`, `frequenceofnotice`, `daysbeforeshowexpiration`, `enabled`, `expirationclosableby`, `createdby`, `modificationdate`, `modifiedby`, `creationdate`) VALUES
(64, 9, 'ABC Task Template 1', 'monthly', 'fix_day', 5, 5, 10, 15, 1, 1, 'FOREIGN', '2018-10-14 18:18:49', 'FOREIGN', '2018-10-14 18:18:49'),
(65, 10, 'ABC Task Template 2', 'yearly', 'month_start', 0, 5, 5, 10, 1, 1, 'FOREIGN', '2018-10-14 18:20:31', 'FOREIGN', '2018-10-14 18:20:31'),
(66, 11, 'BBC Task Template 1', 'weekly', 'month_start', 0, 3, 3, 5, 1, 2, 'INLAND', '2018-10-14 20:01:35', 'INLAND', '2018-10-14 19:52:53'),
(67, 11, 'BBC Task Template 2', 'monthly', 'fix_day', 4, 5, 5, 8, 1, 1, 'INLAND', '2018-10-14 19:55:56', 'INLAND', '2018-10-14 19:55:56'),
(68, 11, 'Task Template 3', 'monthly', 'month_start', 0, 5, 5, 1, 1, 2, 'INLAND', '2018-10-14 20:03:35', 'INLAND', '2018-10-14 20:03:00');

-- --------------------------------------------------------

--
-- Table structure for table `co_tasktemplateattachment`
--

CREATE TABLE `co_tasktemplateattachment` (
  `id` int(11) NOT NULL COMMENT 'The PK of the TABLE ',
  `tasktemplate_id` int(11) NOT NULL COMMENT 'The FK of the TABLE Task template',
  `filename` varchar(255) NOT NULL COMMENT 'The NAME of the FILE ',
  `filetype` varchar(255) NOT NULL COMMENT 'The type of the FILE ',
  `filepath` varchar(255) NOT NULL COMMENT 'The path of the FILE ',
  `filesize` int(11) NOT NULL,
  `createdby` varchar(100) NOT NULL COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  `modificationdate` datetime NOT NULL COMMENT 'The DATE of the creation of the record',
  `modifiedby` varchar(100) NOT NULL COMMENT 'That CORPOBLIG_USER that has made the LAST modification',
  `creationdate` datetime NOT NULL COMMENT 'The DATE of creation of the record'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='The TABLE of Task template attached ';

--
-- Dumping data for table `co_tasktemplateattachment`
--

INSERT INTO `co_tasktemplateattachment` (`id`, `tasktemplate_id`, `filename`, `filetype`, `filepath`, `filesize`, `createdby`, `modificationdate`, `modifiedby`, `creationdate`) VALUES
(3, 64, 'Gestione per uffici.jpg', 'application/octet-stream', 'C:\\template_files\\2018\\9\\14\\64_Gestione per uffici.jpg', 179300, 'FOREIGN', '2018-10-14 18:18:49', 'FOREIGN', '2018-10-14 18:18:49'),
(4, 64, 'nvm-setup.zip', 'application/octet-stream', 'C:\\template_files\\2018\\9\\14\\64_nvm-setup.zip', 2079418, 'FOREIGN', '2018-10-14 18:18:49', 'FOREIGN', '2018-10-14 18:18:49'),
(5, 64, 'README.txt', 'application/octet-stream', 'C:\\template_files\\2018\\9\\14\\64_README.txt', 2544, 'FOREIGN', '2018-10-14 18:18:50', 'FOREIGN', '2018-10-14 18:18:50'),
(6, 64, 'Requirements-Estimation.xls', 'application/octet-stream', 'C:\\template_files\\2018\\9\\14\\64_Requirements-Estimation.xls', 57856, 'FOREIGN', '2018-10-14 18:18:50', 'FOREIGN', '2018-10-14 18:18:50'),
(7, 65, 'Analisi adempimenti societari .pdf', 'application/octet-stream', 'C:\\template_files\\2018\\9\\14\\65_Analisi adempimenti societari .pdf', 965692, 'FOREIGN', '2018-10-14 18:20:31', 'FOREIGN', '2018-10-14 18:20:31'),
(8, 65, 'The Walking Dead_9x01_HDTV.AVS-SVA.en.zip', 'application/octet-stream', 'C:\\template_files\\2018\\9\\14\\65_The Walking Dead_9x01_HDTV.AVS-SVA.en.zip', 17428, 'FOREIGN', '2018-10-14 18:20:31', 'FOREIGN', '2018-10-14 18:20:31'),
(9, 66, 'README (1).txt', 'application/octet-stream', 'C:\\template_files\\2018\\9\\14\\66_README (1).txt', 2544, 'INLAND', '2018-10-14 19:52:53', 'INLAND', '2018-10-14 19:52:53'),
(10, 66, 'README (2).txt', 'application/octet-stream', 'C:\\template_files\\2018\\9\\14\\66_README (2).txt', 2544, 'INLAND', '2018-10-14 19:52:53', 'INLAND', '2018-10-14 19:52:53'),
(11, 66, 'README (3).txt', 'application/octet-stream', 'C:\\template_files\\2018\\9\\14\\66_README (3).txt', 2544, 'INLAND', '2018-10-14 19:52:53', 'INLAND', '2018-10-14 19:52:53'),
(13, 66, 'README (5).txt', 'application/octet-stream', 'C:\\template_files\\2018\\9\\14\\66_README (5).txt', 2544, 'INLAND', '2018-10-14 19:52:53', 'INLAND', '2018-10-14 19:52:53'),
(14, 67, 'Requirements-Estimation.xls', 'application/octet-stream', 'C:\\template_files\\2018\\9\\14\\67_Requirements-Estimation.xls', 57856, 'INLAND', '2018-10-14 19:56:11', 'INLAND', '2018-10-14 19:56:11');

-- --------------------------------------------------------

--
-- Table structure for table `co_topic`
--

CREATE TABLE `co_topic` (
  `id` int(11) NOT NULL COMMENT 'The PK of the TABLE ',
  `description` varchar(100) NOT NULL COMMENT 'The description',
  `enabled` tinyint(1) NOT NULL COMMENT 'The STATUS :Active OR NOT active',
  `creationdate` datetime NOT NULL COMMENT 'The DATE of creation of the record',
  `createdby` varchar(100) NOT NULL COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  `modificationdate` datetime NOT NULL COMMENT 'The DATE of the creation of the record',
  `modifiedby` varchar(100) NOT NULL COMMENT 'That CORPOBLIG_USER that has made the LAST modification'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='The TABLE Topic';

--
-- Dumping data for table `co_topic`
--

INSERT INTO `co_topic` (`id`, `description`, `enabled`, `creationdate`, `createdby`, `modificationdate`, `modifiedby`) VALUES
(9, 'ABC Topic EN', 1, '2018-10-14 18:15:37', 'FOREIGN', '2018-10-14 18:15:37', 'FOREIGN'),
(10, 'ABC Topic 2 EN', 1, '2018-10-14 18:16:17', 'FOREIGN', '2018-10-14 18:16:17', 'FOREIGN'),
(11, 'BBC Topic 1 IT', 1, '2018-10-14 18:31:14', 'INLAND', '2018-10-14 18:31:14', 'INLAND'),
(12, 'BBC Topic 2 EN', 1, '2018-10-14 18:31:30', 'INLAND', '2018-10-14 18:31:30', 'INLAND');

-- --------------------------------------------------------

--
-- Table structure for table `co_topicconsultant`
--

CREATE TABLE `co_topicconsultant` (
  `id` int(11) NOT NULL COMMENT 'The PK of the TABLE ',
  `topic_id` int(11) NOT NULL COMMENT 'The FK of the TABLE Topic',
  `consultantcompany_id` int(11) NOT NULL COMMENT 'The FK of the TABLE Consultant',
  `enabled` tinyint(1) NOT NULL COMMENT 'The STATUS :Active OR NOT active',
  `creationdate` datetime NOT NULL COMMENT 'The DATE of creation of the record',
  `createdby` varchar(100) NOT NULL COMMENT 'The CORPOBLIG_USER that has made the LAST modification',
  `modificationdate` datetime NOT NULL COMMENT 'The DATE of the creation of the record',
  `modifiedby` varchar(100) NOT NULL COMMENT 'That CORPOBLIG_USER that has made the LAST modification'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='The TABLE Topic Consultant';

--
-- Dumping data for table `co_topicconsultant`
--

INSERT INTO `co_topicconsultant` (`id`, `topic_id`, `consultantcompany_id`, `enabled`, `creationdate`, `createdby`, `modificationdate`, `modifiedby`) VALUES
(16, 9, 29, 1, '2018-10-14 18:17:17', 'FOREIGN', '2018-10-14 18:17:17', 'FOREIGN'),
(17, 10, 30, 1, '2018-10-14 18:17:40', 'FOREIGN', '2018-10-14 18:17:40', 'FOREIGN'),
(18, 11, 31, 1, '2018-10-14 18:32:54', 'INLAND', '2018-10-14 18:32:54', 'INLAND'),
(19, 12, 32, 1, '2018-10-14 18:32:57', 'INLAND', '2018-10-14 18:32:57', 'INLAND'),
(20, 12, 31, 1, '2018-10-14 18:32:58', 'INLAND', '2018-10-14 18:32:58', 'INLAND');

-- --------------------------------------------------------

--
-- Table structure for table `co_translations`
--

CREATE TABLE `co_translations` (
  `id` int(11) NOT NULL COMMENT 'The PK of the TABLE ',
  `entity_id` int(11) NOT NULL COMMENT 'The ID of entity TO translate the desciption',
  `tablename` varchar(100) NOT NULL COMMENT 'The TABLE NAME serves AS reference IN ORDER TO translate the TEXT ',
  `lang` varchar(2) NOT NULL COMMENT 'The LANGUAGE of the translation TEXT ',
  `description` varchar(255) NOT NULL COMMENT 'The TEXT translated'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='The TABLE that CONTAINS the TEXT of different languanges';

--
-- Dumping data for table `co_translations`
--

INSERT INTO `co_translations` (`id`, `entity_id`, `tablename`, `lang`, `description`) VALUES
(3, 2, 'co_topic', 'EN', 'Test English'),
(4, 2, 'co_topic', 'IT', 'Test Italian'),
(5, 3, 'co_topic', 'EN', 'Test English'),
(6, 3, 'co_topic', 'IT', 'Test Italian'),
(7, 4, 'co_topic', 'EN', 'EN'),
(8, 4, 'co_topic', 'IT', 'IT'),
(9, 5, 'co_topic', 'EN', 'aaaa'),
(10, 5, 'co_topic', 'IT', 'aaaa'),
(11, 6, 'co_topic', 'EN', 'a11'),
(12, 7, 'co_topic', 'EN', 'a11a11'),
(31, 1, 'tasktemplate#periodicity#weekly', 'EN', 'Weekly'),
(32, 1, 'tasktemplate#periodicity#weekly', 'IT', 'Settimanale'),
(33, 2, 'tasktemplate#periodicity#monthly', 'EN', 'Monthly'),
(34, 2, 'tasktemplate#periodicity#monthly', 'IT', 'Mensile'),
(35, 3, 'tasktemplate#periodicity#yearly', 'EN', 'Yearly'),
(36, 3, 'tasktemplate#periodicity#yearly', 'IT', 'Annuale'),
(37, 1, 'tasktemplate#expirationtype#fix_day', 'EN', 'Fixed day'),
(38, 1, 'tasktemplate#expirationtype#fix_day', 'IT', 'Giorno fisso'),
(39, 2, 'tasktemplate#expirationtype#month_start', 'EN', 'Beginning of the month'),
(40, 2, 'tasktemplate#expirationtype#month_start', 'IT', 'Inizio mese'),
(41, 2, 'tasktemplate#expirationtype#month_end', 'EN', 'End of the month'),
(42, 2, 'tasktemplate#expirationtype#month_end', 'IT', 'Fine mese'),
(53, 1, 'tasktemplate#configurationinterval', 'EN', 'Configuration: '),
(54, 1, 'tasktemplate#configurationinterval', 'IT', 'Configurazione: '),
(55, 8, 'co_topic', 'EN', 'Foreign Topic'),
(56, 9, 'co_topic', 'EN', 'ABC Topic EN'),
(57, 9, 'co_topic', 'IT', 'ABC Topic IT'),
(58, 10, 'co_topic', 'EN', 'ABC Topic 2 EN'),
(59, 11, 'co_topic', 'EN', 'BBC Topic 1 EN'),
(60, 11, 'co_topic', 'IT', 'BBC Topic 1 IT'),
(61, 12, 'co_topic', 'EN', 'BBC Topic 2 EN');

-- --------------------------------------------------------

--
-- Table structure for table `co_user`
--

CREATE TABLE `co_user` (
  `username` varchar(100) NOT NULL COMMENT 'The username of the CORPOBLIG_USER has ben used during whole SESSION ',
  `fullname` varchar(255) NOT NULL COMMENT 'The NAME AND LAST NAME of the CORPOBLIG_USER ',
  `email` varchar(255) NOT NULL COMMENT 'The email of the CORPOBLIG_USER ',
  `lang` varchar(2) NOT NULL COMMENT 'The LANGUAGE of the CORPOBLIG_USER used TO display the CORPOBLIG_USER LANGUAGE ON the application',
  `enabled` tinyint(1) NOT NULL COMMENT 'The STATUS of the CORPOBLIG_USER :Active OR NOT Active'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='The TABLE of the Users';

--
-- Dumping data for table `co_user`
--

INSERT INTO `co_user` (`username`, `fullname`, `email`, `lang`, `enabled`) VALUES
('ADMIN', 'Administrator', 'admin@admin.com', 'EN', 1),
('FOREIGN', 'Foreign', 'foreign@foreign.com', 'EN', 1),
('INLAND', 'Inland', 'inland@inland.com', 'EN', 1),
('TEST', 'Test', 'test@test.com', 'EN', 0),
('USER', 'User', 'user@user.com', 'IT', 1),
('USER2', 'User 2', 'user2@user.com', 'EN', 1),
('USER3', 'User 3', 'user3@user.com', 'EN', 1),
('USER4', 'User 4', 'user4@user.com', 'EN', 1),
('USER5', 'User 5', 'user5@user.com', 'EN', 1);

-- --------------------------------------------------------

--
-- Table structure for table `co_userrole`
--

CREATE TABLE `co_userrole` (
  `id` int(11) NOT NULL COMMENT 'The PK of the TABLE ',
  `username` varchar(100) NOT NULL COMMENT 'The FK of the TABLE CORPOBLIG_USER,n  n  defined the role of the CORPOBLIG_USER ',
  `roleuid` varchar(100) NOT NULL COMMENT 'The FK of the TABLE role,n  n  defines the role FOR the CORPOBLIG_USER '
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='The junction TABLE of CORPOBLIG_USER AND role';

--
-- Dumping data for table `co_userrole`
--

INSERT INTO `co_userrole` (`id`, `username`, `roleuid`) VALUES
(1, 'ADMIN', 'CORPOBLIG_ADMIN'),
(2, 'USER', 'CORPOBLIG_USER'),
(3, 'FOREIGN', 'CORPOBLIG_BACKOFFICE_FOREIGN'),
(4, 'INLAND', 'CORPOBLIG_BACKOFFICE_INLAND'),
(5, 'USER2', 'CORPOBLIG_USER'),
(6, 'USER3', 'CORPOBLIG_USER'),
(7, 'USER4', 'CORPOBLIG_USER'),
(8, 'USER5', 'CORPOBLIG_USER');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `co_company`
--
ALTER TABLE `co_company`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `co_companyconsultant`
--
ALTER TABLE `co_companyconsultant`
  ADD PRIMARY KEY (`id`),
  ADD KEY `co_companyconsultant_co_company_id_fk` (`company_id`);

--
-- Indexes for table `co_companytopic`
--
ALTER TABLE `co_companytopic`
  ADD PRIMARY KEY (`id`),
  ADD KEY `co_companytopic_co_company_id_fk` (`company_id`),
  ADD KEY `co_companytopic_co_topic_id_fk` (`topic_id`);

--
-- Indexes for table `co_companyuser`
--
ALTER TABLE `co_companyuser`
  ADD PRIMARY KEY (`id`),
  ADD KEY `co_companyuser_co_company_id_fk` (`company_id`),
  ADD KEY `co_companyuser_co_user_username_fk` (`username`);

--
-- Indexes for table `co_expiration`
--
ALTER TABLE `co_expiration`
  ADD PRIMARY KEY (`id`),
  ADD KEY `co_expiration_co_office_id_fk` (`office_id`),
  ADD KEY `co_expiration_co_tasktemplate_id_fk` (`tasktemplate_id`),
  ADD KEY `co_expiration_co_task_id_fk` (`task_id`);

--
-- Indexes for table `co_expirationactivity`
--
ALTER TABLE `co_expirationactivity`
  ADD PRIMARY KEY (`id`),
  ADD KEY `co_expirationactivity_co_expiration_id_fk` (`expiration_id`);

--
-- Indexes for table `co_expirationactivityattachment`
--
ALTER TABLE `co_expirationactivityattachment`
  ADD PRIMARY KEY (`id`),
  ADD KEY `co_expirationactivityattachment_co_expirationactivity_id_fk` (`expirationactivity_id`);

--
-- Indexes for table `co_office`
--
ALTER TABLE `co_office`
  ADD PRIMARY KEY (`id`),
  ADD KEY `co_office_co_company_id_fk` (`company_id`);

--
-- Indexes for table `co_task`
--
ALTER TABLE `co_task`
  ADD PRIMARY KEY (`id`),
  ADD KEY `co_task_co_tasktemplate_id_fk` (`tasktemplate_id`);

--
-- Indexes for table `co_taskoffice`
--
ALTER TABLE `co_taskoffice`
  ADD PRIMARY KEY (`id`),
  ADD KEY `co_taskoffice_co_office_id_fk` (`office_id`),
  ADD KEY `co_taskoffice_co_task_id_fk` (`task_id`),
  ADD KEY `co_taskoffice_co_tasktemplate_id_fk` (`tasktemplate_id`);

--
-- Indexes for table `co_taskofficerelations`
--
ALTER TABLE `co_taskofficerelations`
  ADD PRIMARY KEY (`id`),
  ADD KEY `co_taskofficerelations_co_task_id_fk` (`taskoffice_id`);

--
-- Indexes for table `co_tasktemplate`
--
ALTER TABLE `co_tasktemplate`
  ADD PRIMARY KEY (`id`),
  ADD KEY `co_tasktemplate_co_topic_id_fk` (`topic_id`);

--
-- Indexes for table `co_tasktemplateattachment`
--
ALTER TABLE `co_tasktemplateattachment`
  ADD PRIMARY KEY (`id`),
  ADD KEY `co_tasktemplateattachment_co_tasktemplate_id_fk` (`tasktemplate_id`);

--
-- Indexes for table `co_topic`
--
ALTER TABLE `co_topic`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `co_topicconsultant`
--
ALTER TABLE `co_topicconsultant`
  ADD PRIMARY KEY (`id`),
  ADD KEY `co_topicconsultant_co_topic_id_fk` (`topic_id`),
  ADD KEY `co_topicconsultant_co_companyconsultant_id_fk` (`consultantcompany_id`);

--
-- Indexes for table `co_translations`
--
ALTER TABLE `co_translations`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `co_user`
--
ALTER TABLE `co_user`
  ADD PRIMARY KEY (`username`);

--
-- Indexes for table `co_userrole`
--
ALTER TABLE `co_userrole`
  ADD PRIMARY KEY (`id`),
  ADD KEY `co_userrole_co_role_role_fk` (`roleuid`),
  ADD KEY `co_userrole_co_user_username_fk` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `co_company`
--
ALTER TABLE `co_company`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'The PK of the TABLE ', AUTO_INCREMENT=32;

--
-- AUTO_INCREMENT for table `co_companyconsultant`
--
ALTER TABLE `co_companyconsultant`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'The PK of the TABLE ', AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT for table `co_companytopic`
--
ALTER TABLE `co_companytopic`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'The Pk of the TABLE ', AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `co_companyuser`
--
ALTER TABLE `co_companyuser`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'The PK of the TABLE ', AUTO_INCREMENT=127;

--
-- AUTO_INCREMENT for table `co_expiration`
--
ALTER TABLE `co_expiration`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `co_expirationactivity`
--
ALTER TABLE `co_expirationactivity`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `co_expirationactivityattachment`
--
ALTER TABLE `co_expirationactivityattachment`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'The PK of the TABLE ';

--
-- AUTO_INCREMENT for table `co_office`
--
ALTER TABLE `co_office`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'The PK of the TABLE ', AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `co_task`
--
ALTER TABLE `co_task`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'The PK of the TABLE ', AUTO_INCREMENT=128;

--
-- AUTO_INCREMENT for table `co_taskoffice`
--
ALTER TABLE `co_taskoffice`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=168;

--
-- AUTO_INCREMENT for table `co_taskofficerelations`
--
ALTER TABLE `co_taskofficerelations`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'The PK of the TABLE ', AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `co_tasktemplate`
--
ALTER TABLE `co_tasktemplate`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'The PK of the TABLE ', AUTO_INCREMENT=69;

--
-- AUTO_INCREMENT for table `co_tasktemplateattachment`
--
ALTER TABLE `co_tasktemplateattachment`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'The PK of the TABLE ', AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `co_topic`
--
ALTER TABLE `co_topic`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'The PK of the TABLE ', AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `co_topicconsultant`
--
ALTER TABLE `co_topicconsultant`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'The PK of the TABLE ', AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `co_translations`
--
ALTER TABLE `co_translations`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'The PK of the TABLE ', AUTO_INCREMENT=62;

--
-- AUTO_INCREMENT for table `co_userrole`
--
ALTER TABLE `co_userrole`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'The PK of the TABLE ', AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `co_companyconsultant`
--
ALTER TABLE `co_companyconsultant`
  ADD CONSTRAINT `co_companyconsultant_co_company_id_fk` FOREIGN KEY (`company_id`) REFERENCES `co_company` (`id`);

--
-- Constraints for table `co_companytopic`
--
ALTER TABLE `co_companytopic`
  ADD CONSTRAINT `co_companytopic_co_company_id_fk` FOREIGN KEY (`company_id`) REFERENCES `co_company` (`id`),
  ADD CONSTRAINT `co_companytopic_co_topic_id_fk` FOREIGN KEY (`topic_id`) REFERENCES `co_topic` (`id`);

--
-- Constraints for table `co_companyuser`
--
ALTER TABLE `co_companyuser`
  ADD CONSTRAINT `co_companyuser_co_company_id_fk` FOREIGN KEY (`company_id`) REFERENCES `co_company` (`id`),
  ADD CONSTRAINT `co_companyuser_co_user_username_fk` FOREIGN KEY (`username`) REFERENCES `co_user` (`username`);

--
-- Constraints for table `co_expiration`
--
ALTER TABLE `co_expiration`
  ADD CONSTRAINT `co_expiration_co_office_id_fk` FOREIGN KEY (`office_id`) REFERENCES `co_office` (`id`),
  ADD CONSTRAINT `co_expiration_co_task_id_fk` FOREIGN KEY (`task_id`) REFERENCES `co_task` (`id`),
  ADD CONSTRAINT `co_expiration_co_tasktemplate_id_fk` FOREIGN KEY (`tasktemplate_id`) REFERENCES `co_tasktemplate` (`id`);

--
-- Constraints for table `co_expirationactivity`
--
ALTER TABLE `co_expirationactivity`
  ADD CONSTRAINT `co_expirationactivity_co_expiration_id_fk` FOREIGN KEY (`expiration_id`) REFERENCES `co_expiration` (`id`);

--
-- Constraints for table `co_expirationactivityattachment`
--
ALTER TABLE `co_expirationactivityattachment`
  ADD CONSTRAINT `co_expirationactivityattachment_co_expirationactivity_id_fk` FOREIGN KEY (`expirationactivity_id`) REFERENCES `co_expirationactivity` (`id`);

--
-- Constraints for table `co_office`
--
ALTER TABLE `co_office`
  ADD CONSTRAINT `co_office_co_company_id_fk` FOREIGN KEY (`company_id`) REFERENCES `co_company` (`id`);

--
-- Constraints for table `co_task`
--
ALTER TABLE `co_task`
  ADD CONSTRAINT `co_task_co_tasktemplate_id_fk` FOREIGN KEY (`tasktemplate_id`) REFERENCES `co_tasktemplate` (`id`);

--
-- Constraints for table `co_taskoffice`
--
ALTER TABLE `co_taskoffice`
  ADD CONSTRAINT `co_taskoffice_co_office_id_fk` FOREIGN KEY (`office_id`) REFERENCES `co_office` (`id`),
  ADD CONSTRAINT `co_taskoffice_co_task_id_fk` FOREIGN KEY (`task_id`) REFERENCES `co_task` (`id`),
  ADD CONSTRAINT `co_taskoffice_co_tasktemplate_id_fk` FOREIGN KEY (`tasktemplate_id`) REFERENCES `co_tasktemplate` (`id`);

--
-- Constraints for table `co_taskofficerelations`
--
ALTER TABLE `co_taskofficerelations`
  ADD CONSTRAINT `co_taskofficerelations_co_taskoffice_id_fk` FOREIGN KEY (`taskoffice_id`) REFERENCES `co_taskoffice` (`id`);

--
-- Constraints for table `co_tasktemplate`
--
ALTER TABLE `co_tasktemplate`
  ADD CONSTRAINT `co_tasktemplate_co_topic_id_fk` FOREIGN KEY (`topic_id`) REFERENCES `co_topic` (`id`);

--
-- Constraints for table `co_tasktemplateattachment`
--
ALTER TABLE `co_tasktemplateattachment`
  ADD CONSTRAINT `co_tasktemplateattachment_co_tasktemplate_id_fk` FOREIGN KEY (`tasktemplate_id`) REFERENCES `co_tasktemplate` (`id`);

--
-- Constraints for table `co_topicconsultant`
--
ALTER TABLE `co_topicconsultant`
  ADD CONSTRAINT `co_topicconsultant_co_companyconsultant_id_fk` FOREIGN KEY (`consultantcompany_id`) REFERENCES `co_companyconsultant` (`id`),
  ADD CONSTRAINT `co_topicconsultant_co_topic_id_fk` FOREIGN KEY (`topic_id`) REFERENCES `co_topic` (`id`);

--
-- Constraints for table `co_userrole`
--
ALTER TABLE `co_userrole`
  ADD CONSTRAINT `co_userrole_co_user_username_fk` FOREIGN KEY (`username`) REFERENCES `co_user` (`username`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
