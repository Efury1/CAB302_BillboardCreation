CREATE DATABASE IF NOT EXISTS billboard_database;

USE billboard_database;

DROP TABLE IF EXISTS `billboard_database`.`Users`;

CREATE TABLE  IF NOT EXISTS `billboard_database`.`Users` (
  `Username` varchar(45) NOT NULL UNIQUE,
  `Password` char(45) NOT NULL,
  `PermissionEditUsers` int(1) NOT NULL,
  `PermissionCreateBillboards` int(1) NOT NULL,
  `PermissionEditBillboards` int(1) NOT NULL,
  `PermissionSchedule` int(1) NOT NULL,
  PRIMARY KEY  (`Username`)
)


DROP TABLE IF EXISTS `billboard_database`.`Billboards`;

CREATE TABLE  IF NOT EXISTS `billboard_database`.`Billboards` (
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

DROP TABLE IF EXISTS `billboard_database`.`Schedules`;
CREATE TABLE  IF NOT EXISTS `billboard_database`.`Schedules` (
  `ScheduleID` int(10) NOT NULL AUTO_INCREMENT,
  `StartTime` datetime NOT NULL,
  `EndTime` datetime NOT NULL,
  `BillboardID` int(10) NOT NULL,
  PRIMARY KEY  (`Username`),
  FOREIGN KEY (BillboardID) REFERENCES Billboards(BillboardID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
