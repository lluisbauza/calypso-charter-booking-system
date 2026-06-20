-- ============================================
-- Project Calipso
-- Boat Charter Booking System
-- Author: Lluís Bauzá
-- ============================================

CREATE DATABASE calipso;
USE calipso;

-- 1- security_questions table
CREATE TABLE security_questions (
	id_security_question INT PRIMARY KEY AUTO_INCREMENT,
    security_question VARCHAR(50) NOT NULL
);

-- 2- users table
CREATE TABLE users (
	id_user INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(25) NOT NULL ,
    last_name_1 VARCHAR(25) NOT NULL,
	last_name_2 VARCHAR(25),
    must_change_password boolean NOT NULL,
    current_password_hash VARCHAR (255) NOT NULL,
	id_security_question INT NOT NULL,
    security_answer VARCHAR(50) NOT NULL,
    mail VARCHAR(50) NOT NULL UNIQUE,
    username VARCHAR(25) NOT NULL UNIQUE,
	FOREIGN KEY (id_security_question) REFERENCES security_questions (id_security_question)
);

-- 3- password_history table
CREATE TABLE password_history (
	id_password_history INT PRIMARY KEY AUTO_INCREMENT,
    id_user INT NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    FOREIGN KEY (id_user) REFERENCES users (id_user),
    UNIQUE (id_user, password_hash)
);

-- 4- boats table
CREATE TABLE boats (
	id_boat INT PRIMARY KEY AUTO_INCREMENT,
	registration VARCHAR (15) UNIQUE NOT NULL,
    boat_name VARCHAR (10) UNIQUE NOT NULL,
    capacity INT NOT NULL
);

-- 5- trip_types table
CREATE TABLE trip_types (
	id_trip_type INT PRIMARY KEY AUTO_INCREMENT,
    id_boat INT NOT NULL,
    trip_option VARCHAR(15) NOT NULL,
    duration_hours INT NOT NULL,
    departure_time TIME NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_boat) REFERENCES boats (id_boat)
);

-- 6- clients table
CREATE TABLE clients (
	id_client INT PRIMARY KEY AUTO_INCREMENT,
    mail VARCHAR(50) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL,
    name VARCHAR(50) NOT NULL
);

-- 7- agencies table
CREATE TABLE agencies (
	id_agency INT PRIMARY KEY AUTO_INCREMENT,
    cif VARCHAR(10) UNIQUE NOT NULL,
	name VARCHAR(20) NOT NULL,
    affiliate_code VARCHAR(10) NOT NULL UNIQUE,
    discount DECIMAL(5,2) NOT NULL
);

-- 8- reservations table
CREATE TABLE reservations (
	id_reservation INT PRIMARY KEY AUTO_INCREMENT,
    reservation_code VARCHAR(20) NOT NULL UNIQUE,
    id_client INT NOT NULL,
    id_trip_type INT NOT NULL,
    reservation_date DATE NOT NULL,
    pax INT NOT NULL,
    allergies BOOLEAN NOT NULL,
    final_price DECIMAL(10,2) NOT NULL,
    id_agency INT,
    observations VARCHAR(255),
    FOREIGN KEY (id_client) REFERENCES clients (id_client),
	FOREIGN KEY (id_trip_type) REFERENCES trip_types (id_trip_type),
	FOREIGN KEY (id_agency) REFERENCES agencies (id_agency)
);