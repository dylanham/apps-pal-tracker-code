DROP DATABASE IF EXISTS continuum_dev;
DROP DATABASE IF EXISTS continuum_test;

CREATE USER 'continuum'@'localhost'
  IDENTIFIED BY '';
GRANT ALL PRIVILEGES ON *.* TO 'continuum' @'localhost';

CREATE DATABASE continuum_dev;
CREATE DATABASE continuum_test;
