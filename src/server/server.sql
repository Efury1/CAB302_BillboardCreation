CREATE DATABASE IF NOT EXISTS cab302;

USE cab302;

DROP TABLE IF EXISTS `cab302`.`names`;

CREATE TABLE  IF NOT EXISTS `cab302`.`Users` (
  `Username` varchar(45) NOT NULL UNIQUE,
  `Password` char(45) NOT NULL,
  `PermissionEditUsers` int(1) NOT NULL,
  `PermissionCreateBillboards` int(1) NOT NULL,
  `PermissionEditBillboards` int(1) NOT NULL,
  `PermissionSchedule` int(1) NOT NULL,
  PRIMARY KEY  (`Username`)
)

CREATE TABLE  IF NOT EXISTS `cab302`.`Users` (
  `Username` varchar(45) NOT NULL UNIQUE,
  `Password` char(45) NOT NULL,
  `PermissionEditUsers` int(1) NOT NULL,
  `PermissionCreateBillboards` int(1) NOT NULL,
  `PermissionEditBillboards` int(1) NOT NULL,
  `PermissionSchedule` int(1) NOT NULL,
  PRIMARY KEY  (`Username`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
