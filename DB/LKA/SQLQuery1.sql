use LKA
GO
--select * from LKA.Route
--select * from LKA.Stations
--select * from LKA.Map;


WITH ids as (
	select RouteID 
	from LKA.Route 
	where StationID 
	IN (Select StationID from LKA.Stations where Name ='Pabianice' OR Name ='£Ûdü Kaliska') 
	group by RouteID 
	having count(*)=2
)
select DISTINCT Timetable.RouteID, Timetable.TimetableID, Timetable.StartTime, Trains.* 
from LKA.Route 
INNER JOIN LKA.Timetable ON Route.RouteID=Timetable.RouteID
INNER JOIN LKA.Trains ON Timetable.TrainID=Trains.TrainID
Where Route.RouteID IN (Select RouteID from ids);

select * from LKA.Route where RouteID in (select RouteID from LKA.Route 
where StationID 
IN (Select StationID from LKA.Stations where Name ='Pabianice' OR Name ='£Ûdü Kaliska') 
group by RouteID 
having count(*)=2);

WITH ids as (
	select RouteID 
	from LKA.Route 
	where StationID 
	IN (Select StationID from LKA.Stations where Name ='Pabianice' OR Name ='£Ûdü Kaliska') 
	group by RouteID 
	having count(*)=2
)
select * from LKA.Map 
where (
(Station1ID in (select distinct StationID from LKA.Route where RouteID in (select RouteID from ids))
and 
(Station2ID in (select distinct StationID from LKA.Route where RouteID in (select RouteID from ids))
)));
select * from LKA.Discounts
