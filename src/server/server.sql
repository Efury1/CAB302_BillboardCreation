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
)

CREATE TABLE  IF NOT EXISTS `cab302`.`Billboards` (
  `BillboardID` int(10) NOT NULL AUTO_INCREMENT,
  `MessageText` varchar(45),
  `MessageColor` varchar(45),
  `InformationText` varchar(45),
  `InformationColor` varchar(45),
  `BackgroundColor` varchar(45),
  `PictureURL` varchar(175),
  `PictureData` varchar(175),
  PRIMARY KEY  (`BillboardID`)
)

CREATE TABLE  IF NOT EXISTS `cab302`.`Schedules` (
  `ScheduleID` int(10) NOT NULL AUTO_INCREMENT,
  `StartTime` datetime NOT NULL,
  `EndTime` datetime NOT NULL,
  `BillboardID` int(10) NOT NULL,
  PRIMARY KEY  (`Username`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
