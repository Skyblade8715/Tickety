CREATE DATABASE REGIO
GO

CREATE SCHEMA REGIO
GO

CREATE TABLE [REGIO].[Prices]
(
 [PriceID]    nvarchar(100) NOT NULL ,
 [Name]       text NOT NULL ,
 [PricePerKm] float NOT NULL ,


 CONSTRAINT [PK_prices] PRIMARY KEY CLUSTERED ([PriceID] ASC)
);
GO

CREATE TABLE [REGIO].[Routes]
(
 [RouteID]           nvarchar(100) NOT NULL ,
 [PreviousStationID] nvarchar(100) NOT NULL ,
 [NowStationID]      nvarchar(100) NOT NULL ,
 [NextStationID]     nvarchar(100) NOT NULL ,
 [Distance]          float NOT NULL ,
 [Time]              int NOT NULL ,
 [TimeWait]          int NOT NULL ,
 [Platform]          text NOT NULL ,
 [Track]             int NOT NULL ,


 CONSTRAINT [PK_routes] PRIMARY KEY CLUSTERED ([RouteID] ASC)
);
GO

CREATE TABLE [REGIO].[Stations]
(
 [StationID] nvarchar(100) NOT NULL ,
 [Name]      text NOT NULL ,


 CONSTRAINT [PK_stations] PRIMARY KEY CLUSTERED ([StationID] ASC)
);
GO


CREATE TABLE [REGIO].[Tickets]
(
 [TicketID]       nvarchar(100) NOT NULL ,
 [PriceID]        nvarchar(100) NOT NULL ,
 [TrainID]        nvarchar(100) NOT NULL ,
 [StartStationID] nvarchar(100) NOT NULL ,
 [EndStationID]   nvarchar(100) NOT NULL ,
 [StartTime]      datetime NOT NULL ,


 CONSTRAINT [PK_tickets] PRIMARY KEY CLUSTERED ([TicketID] ASC)
);
GO

CREATE TABLE [REGIO].[Trains]
(
 [TrainID]   nvarchar(100) NOT NULL ,
 [Name]      text NOT NULL ,
 [RouteID]   nvarchar(100) NOT NULL ,
 [StartTime] datetime NOT NULL ,


 CONSTRAINT [PK_trains] PRIMARY KEY CLUSTERED ([TrainID] ASC)
);
GO
