-- liquibase formatted sql
-- changeset 202010081301:1 failOnError:true logicalFilePath: 202010081301.sql

# DROP DATABASE IF EXISTS university;
#
# CREATE DATABASE university;
#
# USE university;

DROP TABLE IF EXISTS lector_department;
DROP TABLE IF EXISTS department;
DROP TABLE IF EXISTS lector;
DROP TABLE IF EXISTS degree;

CREATE TABLE degree(
    degree_id INT AUTO_INCREMENT,
    degree VARCHAR(19) NOT NULL,
    PRIMARY KEY(degree_id)
);

INSERT INTO degree(degree)
VALUES
    ('assistant'),
    ('associate professor'),
    ('professor');

CREATE TABLE lector(
    lector_id INT AUTO_INCREMENT,
    first_name VARCHAR(30) NOT NULL,
    last_name VARCHAR(40) NOT NULL,
    degree_id INT NOT NULL,
    salary INT NOT NULL,
    PRIMARY KEY(lector_id),
    FOREIGN KEY(degree_id) REFERENCES degree(degree_id) ON DELETE NO ACTION
);

CREATE TABLE department(
    department_name VARCHAR(40) UNIQUE NOT NULL,
	head_id INT UNIQUE,
	PRIMARY KEY(department_name),
	FOREIGN KEY(head_id) REFERENCES lector(lector_id) ON DELETE NO ACTION
                                                         );

CREATE TABLE lector_department(
     lector_id INT,
     department_name VARCHAR(40),
     FOREIGN KEY(lector_id) REFERENCES lector(lector_id) ON DELETE NO ACTION,
     FOREIGN KEY(department_name) REFERENCES department(department_name) ON DELETE NO ACTION
);

INSERT INTO lector(first_name, last_name, degree_id, salary)
VALUES
    ('Maryna', 'Yurchenko', 2, 15000),
    ('Ivan', 'Petrenko', 3, 20000),
    ('Petro', 'Ivanov', 2, 15000),
    ('Viktor', 'Ivanchenko', 3, 20000),
    ('Sofia', 'Sydorenko', 1, 10000),
    ('Pavlo', 'Zlepko', 3, 20000),
    ('Bohdan', 'Bondarenko', 3, 20000),
    ('Natalka', 'Poltavka', 3, 20000),
    ('Oksana', 'Ponomarenko', 2, 15000),
    ('Iryna', 'Kuzmenko', 3, 20000),
    ('Mykola', 'Lysenko', 1, 10000),
    ('Vakula', 'Kurylo', 3, 20000);

INSERT INTO department(department_name, head_id)
VALUES
    ('Computer Science', 2),
    ('Mathematics', 4),
    ('Engineering', 6),
    ('Physics', 8);

INSERT INTO lector_department(lector_id, department_name)
VALUES
    (2, 'Computer Science'),
    (4, 'Computer Science'),
    (8, 'Computer Science'),
    (7, 'Computer Science'),
    (11, 'Computer Science'),
    (4, 'Mathematics'),
    (1, 'Mathematics'),
    (3, 'Mathematics'),
    (7, 'Mathematics'),
    (10, 'Mathematics'),
    (6, 'Engineering'),
    (4, 'Engineering'),
    (8, 'Engineering'),
    (9, 'Engineering'),
    (12, 'Engineering'),
    (8, 'Physics'),
    (4, 'Physics'),
    (1, 'Physics'),
    (5, 'Physics'),
    (10, 'Physics');
