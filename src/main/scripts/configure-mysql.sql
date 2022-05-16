CREATE DATABASE hrmsdev;
CREATE DATABASE hrms_prod;

#Create database service accounts
CREATE USER 'hrmsdev'@'localhost' IDENTIFIED BY 'Abc1230g';
CREATE USER 'hrms_prod_user'@'localhost' IDENTIFIED BY 'Abc1230g';
CREATE USER 'hrmsdev'@'%' IDENTIFIED BY 'Abc1230g';
CREATE USER 'hrms_prod_user'@'%' IDENTIFIED BY 'Abc1230g';

#Database grants
GRANT SELECT ON hrmsdev.* to 'hrmsdev'@'localhost';
GRANT INSERT ON hrmsdev.* to 'hrmsdev'@'localhost';
GRANT DELETE ON hrmsdev.* to 'hrmsdev'@'localhost';
GRANT UPDATE ON hrmsdev.* to 'hrmsdev'@'localhost';
GRANT CREATE ON hrmsdev.* to 'hrmsdev'@'localhost';
GRANT SELECT ON hrms_prod.* to 'hrms_prod_user'@'localhost';
GRANT INSERT ON hrms_prod.* to 'hrms_prod_user'@'localhost';
GRANT DELETE ON hrms_prod.* to 'hrms_prod_user'@'localhost';
GRANT UPDATE ON hrms_prod.* to 'hrms_prod_user'@'localhost';
GRANT SELECT ON hrmsdev.* to 'hrmsdev'@'%';
GRANT INSERT ON hrmsdev.* to 'hrmsdev'@'%';
GRANT DELETE ON hrmsdev.* to 'hrmsdev'@'%';
GRANT UPDATE ON hrmsdev.* to 'hrmsdev'@'%';
GRANT CREATE ON hrmsdev.* to 'hrmsdev'@'%';
GRANT SELECT ON hrms_prod.* to 'hrms_prod_user'@'%';
GRANT INSERT ON hrms_prod.* to 'hrms_prod_user'@'%';
GRANT DELETE ON hrms_prod.* to 'hrms_prod_user'@'%';
GRANT UPDATE ON hrms_prod.* to 'hrms_prod_user'@'%';