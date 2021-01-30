USE REGIO 
GO 
--Select RouteID from REGIO.Routes where RouteID IN 
--(Select RouteID from REGIO.Stations where Name In('Chech³o','£ódŸ Kaliska'));

--Select RouteID from REGIO.Routes where NowStationID IN 
--(Select StationID from REGIO.Stations where Name In('Chech³o','£ódŸ Kaliska')) group by RouteID having count(*) > 1;

--With ids as (
--	Select RouteID from REGIO.Routes where NowStationID IN 
--	(Select StationID from REGIO.Stations where Name In('Chech³o','£ódŸ Kaliska')) group by RouteID having count(*) > 1
--)
Select * from REGIO.Trains where RouteID IN(
	Select RouteID from REGIO.Routes where NowStationID IN 
	(Select StationID from REGIO.Stations where Name In('Chech³o','£ódŸ Kaliska')) group by RouteID having count(*) > 1)

Select * from REGIO.Routes where RouteID IN(
	Select RouteID from REGIO.Routes where NowStationID IN 
	(Select StationID from REGIO.Stations where Name In('Chech³o','£ódŸ Kaliska')) group by RouteID having count(*) > 1)

Select * from REGIO.Prices