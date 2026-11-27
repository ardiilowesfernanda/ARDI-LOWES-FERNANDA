-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Nov 27, 2026 at 01:27 AM
-- Server version: 8.4.3
-- PHP Version: 8.3.36

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `fernanda`
--

-- --------------------------------------------------------

--
-- Table structure for table `antrean_servis`
--

CREATE TABLE `antrean_servis` (
  `id_antrean` int NOT NULL,
  `id_pelanggan` int NOT NULL,
  `id_mekanik` int DEFAULT NULL,
  `tanggal_servis` datetime DEFAULT CURRENT_TIMESTAMP,
  `status_servis` varchar(30) DEFAULT 'Menunggu'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `detail_servis`
--

CREATE TABLE `detail_servis` (
  `id_detail` int NOT NULL,
  `id_antrean` int NOT NULL,
  `id_layanan` int NOT NULL,
  `jumlah` int DEFAULT '1',
  `harga` decimal(12,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `layanan`
--

CREATE TABLE `layanan` (
  `id_layanan` int NOT NULL,
  `nama_layanan` varchar(150) NOT NULL,
  `harga` decimal(12,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `layanan`
--

INSERT INTO `layanan` (`id_layanan`, `nama_layanan`, `harga`) VALUES
(1, 'Ganti Oli Mesin', 50000.00),
(2, 'Ganti Oli Gardan', 25000.00),
(3, 'Pengecekan dan Pembersihan Air Filter', 20000.00),
(4, 'Pengecekan Sistem Pengereman', 15000.00),
(5, 'Pengecekan Aki dan Kelistrikan', 15000.00),
(6, 'Pengecekan Tekanan Angin Ban', 5000.00);

-- --------------------------------------------------------

--
-- Table structure for table `mekanik`
--

CREATE TABLE `mekanik` (
  `id_mekanik` int NOT NULL,
  `nama_mekanik` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `mekanik`
--

INSERT INTO `mekanik` (`id_mekanik`, `nama_mekanik`) VALUES
(1, 'Mekanik 1'),
(2, 'Mekanik 2');

-- --------------------------------------------------------

--
-- Table structure for table `pelanggan`
--

CREATE TABLE `pelanggan` (
  `id_pelanggan` int NOT NULL,
  `nama_pelanggan` varchar(100) NOT NULL,
  `no_hp` varchar(20) NOT NULL,
  `no_polisi` varchar(15) NOT NULL,
  `jenis_motor` varchar(50) NOT NULL,
  `tanggal` varchar(30) DEFAULT '-',
  `layanan_dipilih` text,
  `total_biaya` int DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `pelanggan`
--

INSERT INTO `pelanggan` (`id_pelanggan`, `nama_pelanggan`, `no_hp`, `no_polisi`, `jenis_motor`, `tanggal`, `layanan_dipilih`, `total_biaya`) VALUES
(4, 'Putra', '089512434532', 'B 990 KWP', 'NMAX', '26-11-2026 10:31:17', 'Ganti Oli Gardan, Pengecekan dan Pembersihan Air Filter, Pengecekan Sistem Pengereman, Pengecekan Tekanan Angin Ban', 65000),
(5, 'Hashim', '08986435412', 'B 765 PPL', 'AEROX', '26-11-2026 10:38:53', 'Ganti Oli Mesin, Ganti Oli Gardan, Pengecekan dan Pembersihan Air Filter, Pengecekan Sistem Pengereman, Pengecekan Aki dan Kelistrikan', 125000),
(6, 'Ardi Fernanda', '089512474533', 'B 8312 KHO', 'Vario 150', '26-11-2026 10:54:12', 'Ganti Oli Mesin, Ganti Oli Gardan, Pengecekan dan Pembersihan Air Filter, Pengecekan Sistem Pengereman, Pengecekan Aki dan Kelistrikan', 125000);

-- --------------------------------------------------------

--
-- Table structure for table `pengguna`
--

CREATE TABLE `pengguna` (
  `id` int NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `pengguna`
--

INSERT INTO `pengguna` (`id`, `username`, `password`) VALUES
(1, 'ardi', '12345'),
(2, '', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `antrean_servis`
--
ALTER TABLE `antrean_servis`
  ADD PRIMARY KEY (`id_antrean`),
  ADD KEY `id_pelanggan` (`id_pelanggan`),
  ADD KEY `id_mekanik` (`id_mekanik`);

--
-- Indexes for table `detail_servis`
--
ALTER TABLE `detail_servis`
  ADD PRIMARY KEY (`id_detail`),
  ADD KEY `id_antrean` (`id_antrean`),
  ADD KEY `id_layanan` (`id_layanan`);

--
-- Indexes for table `layanan`
--
ALTER TABLE `layanan`
  ADD PRIMARY KEY (`id_layanan`),
  ADD UNIQUE KEY `nama_layanan` (`nama_layanan`);

--
-- Indexes for table `mekanik`
--
ALTER TABLE `mekanik`
  ADD PRIMARY KEY (`id_mekanik`);

--
-- Indexes for table `pelanggan`
--
ALTER TABLE `pelanggan`
  ADD PRIMARY KEY (`id_pelanggan`);

--
-- Indexes for table `pengguna`
--
ALTER TABLE `pengguna`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `antrean_servis`
--
ALTER TABLE `antrean_servis`
  MODIFY `id_antrean` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `detail_servis`
--
ALTER TABLE `detail_servis`
  MODIFY `id_detail` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `layanan`
--
ALTER TABLE `layanan`
  MODIFY `id_layanan` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `mekanik`
--
ALTER TABLE `mekanik`
  MODIFY `id_mekanik` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `pelanggan`
--
ALTER TABLE `pelanggan`
  MODIFY `id_pelanggan` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `pengguna`
--
ALTER TABLE `pengguna`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `antrean_servis`
--
ALTER TABLE `antrean_servis`
  ADD CONSTRAINT `antrean_servis_ibfk_1` FOREIGN KEY (`id_pelanggan`) REFERENCES `pelanggan` (`id_pelanggan`),
  ADD CONSTRAINT `antrean_servis_ibfk_2` FOREIGN KEY (`id_mekanik`) REFERENCES `mekanik` (`id_mekanik`);

--
-- Constraints for table `detail_servis`
--
ALTER TABLE `detail_servis`
  ADD CONSTRAINT `detail_servis_ibfk_1` FOREIGN KEY (`id_antrean`) REFERENCES `antrean_servis` (`id_antrean`),
  ADD CONSTRAINT `detail_servis_ibfk_2` FOREIGN KEY (`id_layanan`) REFERENCES `layanan` (`id_layanan`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
