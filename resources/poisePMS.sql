-- Create the database
CREATE DATABASE PoisePMS;

-- Use the database
USE PoisePMS;

-- Create the StructuralEngineers table
CREATE TABLE StructuralEngineers (
    engineer_id INT AUTO_INCREMENT PRIMARY KEY,
    engineer_name VARCHAR(255) NOT NULL,
    contact_info VARCHAR(255) NOT NULL
);

-- Create the ProjectManagers table
CREATE TABLE ProjectManagers (
    manager_id INT AUTO_INCREMENT PRIMARY KEY,
    manager_name VARCHAR(255) NOT NULL,
    contact_info VARCHAR(255) NOT NULL
);

-- Create the Architects table
CREATE TABLE Architects (
    architect_id INT AUTO_INCREMENT PRIMARY KEY,
    architect_name VARCHAR(255) NOT NULL,
    contact_info VARCHAR(255) NOT NULL
);

-- Create the Contractors table
CREATE TABLE Contractors (
    contractor_id INT AUTO_INCREMENT PRIMARY KEY,
    contractor_name VARCHAR(255) NOT NULL,
    contact_info VARCHAR(255) NOT NULL
);

-- Create the Customers table
CREATE TABLE Customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    customer_surname VARCHAR(255),
    contact_info VARCHAR(255) NOT NULL,
    business_name VARCHAR(255), 
    is_business TINYINT(1)
);

-- Create the Projects table
CREATE TABLE Projects (
    project_id INT AUTO_INCREMENT PRIMARY KEY,
    project_name VARCHAR(255) NOT NULL,
    building_type VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    erf_number VARCHAR(255) NOT NULL,
    total_fee DECIMAL(10, 2) NOT NULL,
    amount_paid DECIMAL(10, 2) NOT NULL,
    due_date DATE NOT NULL,
    status VARCHAR(50),
    completion_date DATE,
    structural_engineer_id INT,
    project_manager_id INT,
    architect_id INT,
    contractor_id INT,
    customer_id INT,
    FOREIGN KEY (structural_engineer_id) REFERENCES StructuralEngineers(engineer_id),
    FOREIGN KEY (project_manager_id) REFERENCES ProjectManagers(manager_id),
    FOREIGN KEY (architect_id) REFERENCES Architects(architect_id),
    FOREIGN KEY (contractor_id) REFERENCES Contractors(contractor_id),
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id)
);

-- Insert sample data into StructuralEngineers table
INSERT INTO StructuralEngineers (engineer_name, contact_info) VALUES
('Sibongile Ndlovu', 'sibongile.ndlovu@example.com'),
('Pieter van der Merwe', 'pieter.vdm@example.com'),
('Neelam Naidoo', 'neelam.naidoo@example.com'),
('Alex McFarlane', 'Alex.McFarlane@example.com');

-- Insert sample data into ProjectManagers table
INSERT INTO ProjectManagers (manager_name, contact_info) VALUES
('Priya Singh', 'priya.singh@example.com'),
('Lerato Mthembu', 'lerato.mthembu@example.com'),
('Gerhard Botha', 'gerhard.botha@example.com'),
('Mpumi Zulu', 'mpumi.zulu@example.com');

-- Insert sample data into Architects table
INSERT INTO Architects (architect_name, contact_info) VALUES
('Nomvula Dlamini', 'nomvula.dlamini@example.com'),
('Anand Pillay', 'anand.pillay@example.com'),
('Vusi Khumalo', 'vusi.khumalo@example.com'),
('Annelize du Toit', 'annelize.dutoit@example.com');

-- Insert sample data into Contractors table
INSERT INTO Contractors (contractor_name, contact_info) VALUES
('Mandla Construction', 'mandla.construction@example.com'),
('Dube Builders', 'dube.builders@example.com'),
('Khumo Contractors', 'khumo.contractors@example.com'),
('Smith & Sons', 'smith.sons@example.com');

-- Insert sample data into Customers table
INSERT INTO Customers (customer_name, customer_surname, contact_info, business_name, is_business) VALUES
('Sasol', '', 'contact@sasol.com', 'Sasol Ltd', 1),
('MTN Group', '', 'info@mtn.com', 'MTN Group Ltd', 1),
('Naspers', '', 'support@naspers.com', 'Naspers Ltd', 1),
('Shoprite Holdings', '', 'contact@shoprite.com', 'Shoprite Holdings Ltd', 1),
('Ernst', 'Knacke', 'Ernst.Knacke@example.com', '', 0),
('Devon', 'Hannibol', 'Devon.Hannibol@example.com', '', 0);

-- Insert sample data into Projects table
INSERT INTO Projects (project_name, building_type, address, erf_number, total_fee, amount_paid, due_date, status, structural_engineer_id, project_manager_id, architect_id, contractor_id, customer_id) VALUES
('ChemPlant', 'Industrial', '123 ChemPlant St', 'ERF123', 1000000.00, 500000.00, '2024-12-31', 'open', 1, 1, 1, 1, 1),
('Towers', 'Commercial', '456 Tower Rd', 'ERF456', 2000000.00, 1500000.00, '2025-06-30', 'open', 2, 2, 2, 2, 2),
('DataCenter', 'Commercial', '789 Data Dr', 'ERF789', 1500000.00, 750000.00, '2024-09-30', 'open', 3, 3, 3, 3, 3),
('Warehouse', 'Industrial', '101 Warehouse Ave', 'ERF101', 800000.00, 400000.00, '2024-03-31', 'open', 4, 4, 4, 4, 4);
