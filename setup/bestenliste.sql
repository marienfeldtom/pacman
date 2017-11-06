-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 28. Jun 2017 um 21:34
-- Server-Version: 10.1.21-MariaDB
-- PHP-Version: 7.1.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `pacman`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `bestenliste`
--

CREATE TABLE `bestenliste` (
  `id` int(11) NOT NULL,
  `name` varchar(11) NOT NULL,
  `wert` int(11) NOT NULL,
  `level` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `bestenliste`
--

INSERT INTO `bestenliste` (`id`, `name`, `wert`, `level`) VALUES
(1, 'Tom', 400, 'Default'),
(2, 'Tom', 680, 'Default'),
(3, 'Tom', 950, 'Default'),
(4, 'Tom', 130, 'Gitter'),
(5, 'Tom', 190, 'Gitter'),
(6, 'Tom', 120, 'Gitter'),
(7, 'Tom', 150, 'Gitter'),
(8, 'Tom', 270, 'Gitter');

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `bestenliste`
--
ALTER TABLE `bestenliste`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `bestenliste`
--
ALTER TABLE `bestenliste`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
