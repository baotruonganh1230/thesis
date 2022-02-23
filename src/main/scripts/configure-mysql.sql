CREATE DATABASE hrms_dev;
CREATE DATABASE hrms_prod;

#Create database service accounts
CREATE USER 'hrms_dev_user'@'localhost' IDENTIFIED BY 'Abc1230g';
CREATE USER 'hrms_prod_user'@'localhost' IDENTIFIED BY 'Abc1230g';
CREATE USER 'hrms_dev_user'@'%' IDENTIFIED BY 'Abc1230g';
CREATE USER 'hrms_prod_user'@'%' IDENTIFIED BY 'Abc1230g';

#Database grants
GRANT ALTER ON hrms_dev.* to 'hrms_dev_user'@'localhost';
GRANT CREATE ON hrms_dev.* to 'hrms_dev_user'@'localhost';
GRANT REFERENCES ON hrms_dev.* to 'hrms_dev_user'@'localhost';

GRANT SELECT ON hrms_dev.* to 'hrms_dev_user'@'localhost';
GRANT INSERT ON hrms_dev.* to 'hrms_dev_user'@'localhost';
GRANT DELETE ON hrms_dev.* to 'hrms_dev_user'@'localhost';
GRANT UPDATE ON hrms_dev.* to 'hrms_dev_user'@'localhost';
GRANT SELECT ON hrms_prod.* to 'hrms_prod_user'@'localhost';
GRANT INSERT ON hrms_prod.* to 'hrms_prod_user'@'localhost';
GRANT DELETE ON hrms_prod.* to 'hrms_prod_user'@'localhost';
GRANT UPDATE ON hrms_prod.* to 'hrms_prod_user'@'localhost';
GRANT SELECT ON hrms_dev.* to 'hrms_dev_user'@'%';
GRANT INSERT ON hrms_dev.* to 'hrms_dev_user'@'%';
GRANT DELETE ON hrms_dev.* to 'hrms_dev_user'@'%';
GRANT UPDATE ON hrms_dev.* to 'hrms_dev_user'@'%';
GRANT SELECT ON hrms_prod.* to 'hrms_prod_user'@'%';
GRANT INSERT ON hrms_prod.* to 'hrms_prod_user'@'%';
GRANT DELETE ON hrms_prod.* to 'hrms_prod_user'@'%';
GRANT UPDATE ON hrms_prod.* to 'hrms_prod_user'@'%';