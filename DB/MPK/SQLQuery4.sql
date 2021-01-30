USE MPK
GO

--select * from MPK.Discounts
--select * from MPK.Route
--select * from MPK.Stops;
--select * from MPK.TicketTypes
--select * from MPK.Tickets

--select TicketID, Name, (Price * (select Discounts.Percentage from MPK.Discounts where DiscountId = 'Ulg')/100) as Price, Time from MPK.TicketTypes;
--select * from MPK.Timetables;

with ids as(
select RouteID from MPK.Route where StopID in(Select StopID from MPK.Stops where Name in('¯eromskiego - Kopernika','Pabianicka - r. Lotników Lwowskich')) group by RouteID having count(*) > 1
)
select * from MPK.Timetables where Timetables.RouteId in (select RouteID from ids);

Select RouteID, Seq_no, DriveTime, Route.StopID, Name from MPK.Route Inner join MPK.Stops on Route.StopID=Stops.StopID where RouteID in(select RouteID from MPK.Route where StopID in(Select StopID from MPK.Stops where Name in('¯eromskiego - Kopernika','Pabianicka - r. Lotników Lwowskich')) group by RouteID having count(*) > 1)

--INSERT INTO [MPK].[Tickets]([DiscountName],[Time],[StartTime],[EndTime])
--     VALUES
--           ('Ulgowy', 20, null, null)
--GO

--UPDATE [MPK].[Tickets] SET [StartTime] = '2021-01-22 21:19',[EndTime] = DATEADD(minute, time, '2021-01-22 21:19') WHERE ID = '4628421'
GO

