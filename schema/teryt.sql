-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Wersja serwera:               5.6.20 - MySQL Community Server (GPL)
-- Serwer OS:                    Win32
-- HeidiSQL Wersja:              9.1.0.4867
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Zrzut struktury bazy danych teryt
CREATE DATABASE IF NOT EXISTS `teryt` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_polish_ci */;
USE `teryt`;


-- Zrzut struktury tabela teryt.location_city
DROP TABLE IF EXISTS `location_city`;
CREATE TABLE IF NOT EXISTS `location_city` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idCommune` int(10) unsigned NOT NULL,
  `idDistrict` int(10) unsigned NOT NULL,
  `idProvince` int(10) unsigned NOT NULL,
  `idCountry` int(10) unsigned NOT NULL,
  `tidCity` int(10) unsigned NOT NULL,
  `tidCommune` int(10) unsigned NOT NULL,
  `tidDistrict` int(10) unsigned NOT NULL,
  `tidProvince` int(10) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Indeks 3` (`tidCity`),
  KEY `location_city_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Zrzut struktury tabela teryt.location_commune
DROP TABLE IF EXISTS `location_commune`;
CREATE TABLE IF NOT EXISTS `location_commune` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idDistrict` int(10) unsigned NOT NULL,
  `idProvince` int(10) unsigned NOT NULL,
  `idCountry` int(10) unsigned NOT NULL,
  `tidCommune` int(10) unsigned NOT NULL,
  `tidDistrict` int(10) unsigned NOT NULL,
  `tidProvince` int(10) unsigned NOT NULL,
  `name` varchar(255) COLLATE utf8_polish_ci NOT NULL,
  `type` varchar(50) COLLATE utf8_polish_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Indeks 2` (`tidCommune`,`tidDistrict`,`tidProvince`,`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

-- Data exporting was unselected.


-- Zrzut struktury tabela teryt.location_country
DROP TABLE IF EXISTS `location_country`;
CREATE TABLE IF NOT EXISTS `location_country` (
  `id` int(10) unsigned NOT NULL,
  `name` varchar(255) COLLATE utf8_polish_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

-- Data exporting was unselected.


-- Zrzut struktury tabela teryt.location_district
DROP TABLE IF EXISTS `location_district`;
CREATE TABLE IF NOT EXISTS `location_district` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idProvince` int(10) unsigned NOT NULL,
  `idCountry` int(10) unsigned NOT NULL,
  `tidDistrict` int(10) unsigned NOT NULL,
  `tidProvince` int(10) unsigned NOT NULL,
  `name` varchar(255) COLLATE utf8_polish_ci NOT NULL,
  `type` varchar(50) COLLATE utf8_polish_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `FK_location_district_location_province` (`tidProvince`,`tidDistrict`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

-- Data exporting was unselected.


-- Zrzut struktury tabela teryt.location_precinct
DROP TABLE IF EXISTS `location_precinct`;
CREATE TABLE IF NOT EXISTS `location_precinct` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idCity` int(10) unsigned NOT NULL,
  `idCommune` int(10) unsigned NOT NULL,
  `idDistrict` int(10) unsigned NOT NULL,
  `idProvince` int(10) unsigned NOT NULL,
  `idCountry` int(10) unsigned NOT NULL,
  `tidPrecinct` int(10) unsigned NOT NULL,
  `tidCity` int(10) unsigned NOT NULL,
  `tidCommune` int(10) unsigned NOT NULL,
  `tidDistrict` int(10) unsigned NOT NULL,
  `tidProvince` int(10) unsigned NOT NULL,
  `name` varchar(255) COLLATE utf8_polish_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Indeks 2` (`tidPrecinct`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

-- Data exporting was unselected.


-- Zrzut struktury tabela teryt.location_province
DROP TABLE IF EXISTS `location_province`;
CREATE TABLE IF NOT EXISTS `location_province` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idCountry` int(10) unsigned NOT NULL,
  `tidProvince` int(10) unsigned NOT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Indeks 2` (`tidProvince`),
  KEY `location_province_FKIndex1` (`idCountry`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Zrzut struktury tabela teryt.location_street
DROP TABLE IF EXISTS `location_street`;
CREATE TABLE IF NOT EXISTS `location_street` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idCity` int(10) unsigned NOT NULL,
  `idCommune` int(10) unsigned NOT NULL,
  `idDistrict` int(10) unsigned NOT NULL,
  `idProvince` int(10) unsigned NOT NULL,
  `idCountry` int(10) unsigned NOT NULL,
  `tidStreet` int(10) unsigned NOT NULL,
  `tidCity` int(10) unsigned NOT NULL,
  `tidCommune` int(10) unsigned NOT NULL,
  `tidDistrict` int(10) unsigned NOT NULL,
  `tidProvince` int(10) unsigned NOT NULL,
  `name` varchar(255) COLLATE utf8_polish_ci NOT NULL,
  `type` varchar(50) COLLATE utf8_polish_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Indeks 2` (`tidCity`,`tidStreet`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

-- Data exporting was unselected.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
