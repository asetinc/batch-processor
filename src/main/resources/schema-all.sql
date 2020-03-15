--DROP TABLE people IF EXISTS;
--
--CREATE TABLE people  (
--    id BIGINT IDENTITY NOT NULL PRIMARY KEY,
--    first_name VARCHAR(20),
--    last_name VARCHAR(20),
--    age VARCHAR(20)
--);

DROP TABLE IF EXISTS people;

CREATE TABLE people (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  first_name VARCHAR(25),
  last_name VARCHAR(25),
  age VARCHAR(20)
);

--INSERT INTO billionaires (first_name, last_name, age) VALUES
--  ('Aliko', 'Dangote', 'Billionaire Industrialist'),
--  ('Bill', 'Gates', 'Billionaire Tech Entrepreneur'),
--  ('Folrunsho', 'Alakija', 'Billionaire Oil Magnate');