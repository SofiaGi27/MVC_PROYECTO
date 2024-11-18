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
    `id` int NOT NULL, -- Columna 'id' de tipo entero, no puede ser nula
    `titulo` varchar(255) DEFAULT NULL, -- Columna 'titulo' de tipo cadena de hasta 255 caracteres, puede ser nula
    `director` varchar(255) DEFAULT NULL, -- Columna 'director' de tipo cadena de hasta 255 caracteres, puede ser nula
    `anio` int DEFAULT NULL, -- Columna 'anio' de tipo entero, puede ser nula
    `genero` varchar(100) DEFAULT NULL, -- Columna 'genero' de tipo cadena de hasta 100 caracteres, puede ser nula
    PRIMARY KEY (`id`) -- Establece la columna 'id' como la clave primaria de la tabla
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci; -- Especifica el motor de almacenamiento InnoDB y el conjunto de caracteres utf8mb4
