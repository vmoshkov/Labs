connect 'jdbc:derby:/OpenStorage;create=true';

CREATE SCHEMA UNO;

SET SCHEMA UNO;

CREATE TABLE manufacturer (
  id INTEGER PRIMARY KEY,
  name VARCHAR(255) UNIQUE NOT NULL,
  country VARCHAR(255) NOT NULL
);

CREATE TABLE car4sale (
  id INTEGER PRIMARY KEY,
  entry_date DATE NOT NULL,
  manufacturer_id INTEGER NOT NULL,
  model VARCHAR(255) NOT NULL,
  car_year INTEGER NOT NULL,
  description VARCHAR(4000) NOT NULL,
  prise DOUBLE NOT NULL,
  contact_person VARCHAR(255) NOT NULL,
  contact_phone VARCHAR(255) NOT NULL
);

CREATE INDEX idx_car__manufacturer_id ON car4sale (manufacturer_id);

ALTER TABLE car4sale ADD CONSTRAINT fk_car__manufacturer_id FOREIGN KEY (manufacturer_id) REFERENCES manufacturer (id);

CREATE TABLE image (
  id INTEGER PRIMARY KEY,
  filename VARCHAR(255) NOT NULL,
  data blob(1M) NOT NULL,
  car_id INTEGER NOT NULL
);

CREATE INDEX idx_image__car_id ON image (car_id);

ALTER TABLE image ADD CONSTRAINT fk_image__car_id FOREIGN KEY (car_id) REFERENCES car4sale (id);

CREATE TABLE account(                          
  id INTEGER PRIMARY KEY,                          
  alias VARCHAR(30) UNIQUE NOT NULL,
  name VARCHAR(100),
  lastname VARCHAR(100),
  password VARCHAR(255) NOT NULL,                          
  email VARCHAR(100) UNIQUE,                          
  active BOOLEAN NOT NULL                     
);          
                                     
CREATE TABLE token(                          
  id INTEGER PRIMARY KEY,                          
  account_id INTEGER REFERENCES account(id),                          
  token_hash VARCHAR(255) NOT NULL,                          
  token_TYPE VARCHAR(100),                          
  ip_address VARCHAR(100),                          
  description VARCHAR(255),                          
  created TIMESTAMP,                          
  expiration TIMESTAMP                       
);

CREATE TABLE roles (
   name VARCHAR(255) UNIQUE NOT NULL,
   code VARCHAR(30) UNIQUE NOT NULL 
);


CREATE TABLE account2roles (
   account_alias VARCHAR(30) NOT NULL,
   role_code VARCHAR(30) NOT NULL 
);

ALTER TABLE account2roles ADD CONSTRAINT ref_unique UNIQUE (account_alias, role_code);

INSERT INTO roles VALUES ('Administrator', 'Admin');
INSERT INTO roles VALUES ('Regular User', 'User');

INSERT INTO account (ID, alias, name, password, active) VALUES (1, 'vm', 'Vladimir Vladimirovich', 'Qwerty2014', true);

INSERT INTO account2roles VALUES ('vm', 'Admin');
INSERT INTO account2roles VALUES ('vm', 'User');

INSERT INTO manufacturer VALUES(1, 'BMW', 'Germany');
INSERT INTO manufacturer VALUES(2, 'Toyota', 'Japan');
INSERT INTO manufacturer VALUES(3, 'Ford', 'USA');





