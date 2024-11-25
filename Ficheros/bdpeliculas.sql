-- Verifica si la base de datos 'peliculasdb' ya existe y, si es así, la elimina
DROP DATABASE IF EXISTS peliculasdb;

-- Crea una nueva base de datos llamada 'peliculasdb'
CREATE DATABASE peliculasdb;

-- Selecciona la base de datos 'peliculasdb' para realizar operaciones en ella
USE peliculasdb;

-- Elimina la tabla 'peliculas' si ya existe para evitar errores al crearla de nuevo
DROP TABLE IF EXISTS `peliculas`;

-- Crea una nueva tabla llamada 'peliculas' con las siguientes columnas
CREATE TABLE `peliculas` (
    `id` int NOT NULL,
    `titulo` varchar(255) DEFAULT NULL, 
    `director` varchar(255) DEFAULT NULL, 
    `anio` int DEFAULT NULL, 
    `genero` varchar(100) DEFAULT NULL, 
    PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci; 