CREATE TABLE customer(id INT NOT NULL PRIMARY KEY, fullname VARCHAR(255), email VARCHAR(255));
ALTER TABLE customer ADD SUPPLEMENTAL LOG DATA (ALL) COLUMNS;
CREATE TABLE car_model(id INT NOT NULL PRIMARY KEY, model VARCHAR(255), brand VARCHAR(255));
ALTER TABLE car_model ADD SUPPLEMENTAL LOG DATA (ALL) COLUMNS;
exit;
