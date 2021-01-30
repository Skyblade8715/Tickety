--CREATE DATABASE LKA
--GO

--CREATE SCHEMA LKA
--GO

USE LKA
GO

CREATE TABLE [LKA].[Timetable]
(
 [RouteID]     nvarchar(100) NOT NULL ,
 [TimetableID] nvarchar(100) NOT NULL ,
 [StartTime]   time(7) NOT NULL ,
 [TrainID]     nvarchar(100) NOT NULL ,


 CONSTRAINT [PK_timetable] PRIMARY KEY CLUSTERED ([TimetableID] ASC)
);
GO
--CREATE TABLE [LKA].[Trains]
--(
-- [TrainID]        nvarchar(100) NOT NULL ,
-- [WiFi]           bit NOT NULL ,
-- [DisabilitySupp] bit NOT NULL ,
-- [AC]             bit NOT NULL ,
-- [TicketMachine]  bit NOT NULL ,


-- CONSTRAINT [PK_pkp_trains] PRIMARY KEY CLUSTERED ([TrainID] ASC)
--);
--GO


--CREATE TABLE [LKA].[Stations]
--(
-- [StationID] nvarchar(100) NOT NULL ,
-- [Platforms] text NOT NULL ,
-- [Tracks]    int NOT NULL ,
-- [Name]      text NOT NULL ,


-- CONSTRAINT [PK_stations] PRIMARY KEY CLUSTERED ([StationID] ASC)
--);
--GO

--CREATE TABLE [LKA].[Route]
--(
-- [RouteID]     nvarchar(100) NOT NULL ,
-- [seq_no]      int NOT NULL ,
-- [StationID]   nvarchar(100) NOT NULL ,
-- [Platform]    text NOT NULL ,
-- [Track]       int NOT NULL ,
-- [ArrivalTime] int NOT NULL ,
-- [WaitTime]    int NOT NULL 

--);
--GO

--CREATE TABLE [LKA].[Discounts]
--(
-- [DiscountID] nvarchar(100) NOT NULL ,
-- [Name]       text NOT NULL ,
-- [Percentage] float NOT NULL ,


-- CONSTRAINT [PK_lka_discounts] PRIMARY KEY CLUSTERED ([DiscountID] ASC)
--);
--GO

--CREATE TABLE [LKA].[Map]
--(
-- [MapID]      nvarchar(100) NOT NULL ,
-- [Distance]   float NOT NULL ,
-- [Price]      float NOT NULL ,
-- [Station1ID] nvarchar(100) NOT NULL ,
-- [Station2ID] nvarchar(100) NOT NULL ,


-- CONSTRAINT [PK_lka_map] PRIMARY KEY CLUSTERED ([MapID] ASC)
--);

--CREATE TABLE [LKA].[Tickets]
--(
-- [TicketID]     nvarchar(100) NOT NULL ,
-- [RouteID]      nvarchar(100) NOT NULL ,
-- [StartStation] nvarchar(100) NOT NULL ,
-- [EndStation]   nvarchar(100) NOT NULL ,
-- [Barcode]      text NOT NULL ,
-- [DiscountID]   nvarchar(100) NOT NULL ,
-- [OwnerName]    text NOT NULL ,


-- CONSTRAINT [PK_lka_tickets] PRIMARY KEY CLUSTERED ([TicketID] ASC)
--);
--GO
