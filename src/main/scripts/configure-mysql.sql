CREATE DATABASE hrmsdev;

#Create database service accounts
CREATE USER 'hrmsdev'@'%' IDENTIFIED BY 'Abc1230g';
CREATE USER 'hrmssuperemp'@'%' IDENTIFIED BY 'Sup1230g';
CREATE USER 'hrmsemp'@'%' IDENTIFIED BY 'Emp1230g';

#Database grants
GRANT SELECT ON hrmsdev.* to 'hrmsdev'@'%';
GRANT INSERT ON hrmsdev.* to 'hrmsdev'@'%';
GRANT DELETE ON hrmsdev.* to 'hrmsdev'@'%';
GRANT UPDATE ON hrmsdev.* to 'hrmsdev'@'%';
GRANT CREATE ON hrmsdev.* to 'hrmsdev'@'%';

GRANT SELECT ON hrmsdev.* to 'hrmssuperemp'@'%';
GRANT INSERT ON hrmsdev.* to 'hrmssuperemp'@'%';
GRANT DELETE ON hrmsdev.* to 'hrmssuperemp'@'%';
GRANT UPDATE ON hrmsdev.* to 'hrmssuperemp'@'%';

GRANT SELECT ON hrmsdev.project to 'hrmsemp'@'%';
GRANT SELECT ON hrmsdev.department to 'hrmsemp'@'%';
GRANT SELECT ON hrmsdev.position to 'hrmsemp'@'%';
GRANT SELECT ON hrmsdev.role to 'hrmsemp'@'%';
GRANT SELECT ON hrmsdev.leave_type to 'hrmsemp'@'%';
GRANT SELECT ON hrmsdev.insurance_type to 'hrmsemp'@'%';
GRANT SELECT ON hrmsdev.task to 'hrmsemp'@'%';
GRANT SELECT ON hrmsdev.attendance to 'hrmsemp'@'%';
GRANT SELECT ON hrmsdev.payment to 'hrmsemp'@'%';
GRANT SELECT ON hrmsdev.leaves to 'hrmsemp'@'%';
GRANT SELECT ON hrmsdev.insurance to 'hrmsemp'@'%';
GRANT SELECT ON hrmsdev.works_on to 'hrmsemp'@'%';
GRANT SELECT ON hrmsdev.manage to 'hrmsemp'@'%';
GRANT SELECT ON hrmsdev.works_in to 'hrmsemp'@'%';
GRANT SELECT ON hrmsdev.works_as to 'hrmsemp'@'%';
GRANT SELECT ON hrmsdev.has to 'hrmsemp'@'%';
GRANT SELECT ON hrmsdev.performs to 'hrmsemp'@'%';
GRANT INSERT ON hrmsdev.task to 'hrmsemp'@'%';
GRANT INSERT ON hrmsdev.attendance to 'hrmsemp'@'%';
GRANT INSERT ON hrmsdev.leaves to 'hrmsemp'@'%';
GRANT INSERT ON hrmsdev.performs to 'hrmsemp'@'%';
GRANT UPDATE ON hrmsdev.task to 'hrmsemp'@'%';
GRANT UPDATE ON hrmsdev.attendance to 'hrmsemp'@'%';
GRANT UPDATE ON hrmsdev.leaves to 'hrmsemp'@'%';
GRANT UPDATE ON hrmsdev.performs to 'hrmsemp'@'%';
GRANT DELETE ON hrmsdev.task to 'hrmsemp'@'%';
GRANT DELETE ON hrmsdev.leaves to 'hrmsemp'@'%';
GRANT DELETE ON hrmsdev.performs to 'hrmsemp'@'%';