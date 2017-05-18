DROP DATABASE IF EXISTS continuum_dev;
DROP DATABASE IF EXISTS continuum_integration_test;
DROP DATABASE IF EXISTS continuum_application_test;

CREATE USER 'continuum'@'localhost'
  IDENTIFIED BY '';
GRANT ALL PRIVILEGES ON *.* TO 'continuum' @'localhost';

CREATE DATABASE continuum_dev;
CREATE DATABASE continuum_integration_test;
CREATE DATABASE continuum_application_test;
