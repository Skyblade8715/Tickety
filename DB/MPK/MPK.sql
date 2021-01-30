--CREATE DATABASE MPK
--GO
--CREATE SCHEMA MPK
--GO

USE MPK
GO

--drop table MPK.Vehicles
--drop table MPK.Stops
--drop table MPK.Timetables
--drop table MPK.Route
--drop table MPK.TicketTypes

--CREATE TABLE [MPK].[Vehicles]
--(
-- [VehicleID]           nvarchar(100) NOT NULL ,
-- [RouteID]		nvarchar(100) NOT NULL ,
-- [Offset]		int NOT NULL ,
-- [Type]			nvarchar(100) NOT NULL ,

-- CONSTRAINT [PK_vehicles] PRIMARY KEY CLUSTERED ([VehicleID] ASC)
--);
--GO

--CREATE TABLE [MPK].Stops
--(
-- StopID		nvarchar(100) NOT NULL ,
-- [Name]     nvarchar(100) NOT NULL ,


-- CONSTRAINT [PK_stops] PRIMARY KEY CLUSTERED (StopID ASC)
--);
--GO

--CREATE TABLE MPK.[Timetables]
--(
-- TimetableID    nvarchar(100) NOT NULL ,
-- RouteID		nvarchar(100) NOT NULL ,
-- Type			nvarchar(100) NOT NULL ,
-- [StartTime]	time(7) NOT NULL ,


-- CONSTRAINT [PK_timetables] PRIMARY KEY CLUSTERED ([TimetableID] ASC)
--);
--GO

--CREATE TABLE MPK.Route
--(
-- RouteID    nvarchar(100) NOT NULL ,
-- Seq_no		nvarchar(100) NOT NULL ,
-- StopID		nvarchar(100) NOT NULL ,
-- DriveTime  int not null

--);
--GO


--CREATE TABLE MPK.[TicketTypes]
--(
-- TicketID   nvarchar(100) NOT NULL ,
-- Name		nvarchar(100) NOT NULL ,
-- Price		money NOT NULL ,
-- Time		int	not null,


-- CONSTRAINT [PK_tickettypes] PRIMARY KEY CLUSTERED (TicketID ASC)
--);
--GO


--CREATE TABLE MPK.Discounts
--(
-- DiscountId		nvarchar(100) NOT NULL ,
-- Name			nvarchar (100) NOT NULL ,
-- Percentage		int NOT NULL ,


-- CONSTRAINT [PK_discounts] PRIMARY KEY CLUSTERED (DiscountId ASC)
--);
--GO

--drop table MPK.Tickets

--CREATE TABLE MPK.Tickets
--(
-- ID				INT NOT NULL IDENTITY (4628421, 1),
-- DiscountName	nvarchar(100) not null,
-- Time			int NOT NULL ,
-- StartTime		datetime,
-- EndTime		datetime,


-- CONSTRAINT [PK_tickets] PRIMARY KEY CLUSTERED (ID ASC)
--);
--GO
