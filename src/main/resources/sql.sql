CREATE DATABASE studymeeting;

CREATE USER 'studymeeting'@'localhost' IDENTIFIED BY '1646';
GRANT ALL PRIVILEGES ON studymeeting.* TO 'studymeeting'@'%' IDENTIFIED BY '1646';
