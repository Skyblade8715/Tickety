USE REGIO
GO

Select * from REGIO.Stations
Select * from REGIO.Routes
--INSERT INTO REGIO.Routes(RouteID, PreviousStationID, NowStationID, NextStationID, Distance, TimeWait, Time, Platform, Track)
--Values 
--('CH-LDKL', 'CH', 'CH', 'PA', 0, 0, 0, 'I', 1),
--('CH-LDKL', 'CH', 'PA', 'LD-LB', 3, 2, 5, 'I', 1),
--('CH-LDKL', 'PA', 'LD-LB', 'LD-KL', 7, 2, 7, 'I', 1),
--('CH-LDKL', 'LD-LB', 'LD-KL', 'LD-KL', 7, 2, 11, 'III', 9),
--('LDKL-CH', 'LD-KL', 'LD-KL', 'LD-LB', 0, 0, 0, 'II', 3),
--('LDKL-CH', 'LD-KL', 'LD-LB', 'PA', 7, 2, 11, 'I', 2),
--('LDKL-CH', 'LD-LB', 'PA', 'CH', 7, 2, 7, 'I', 2),
--('LDKL-CH', 'PA', 'CH', 'CH', 3, 2, 5, 'I', 2)


--NOT USED YET!
--('KL-AN', 'LD-KL', 'LD-KL', 'LD-PA', 2, 0, 5, 'I', 1),
--('KL-AN', 'LD-KL', 'LD-PA', 'LD-CH', 2, 0, 3, 'I', 1),
--('KL-AN', 'LD-PA', 'LD-CH', 'LD-DB', 3, 1, 3, 'I', 1),
--('KL-AN', 'LD-CH', 'LD-DB', 'LD-WD', 5, 0, 5, 'I', 1),
--('KL-AN', 'LD-DB', 'LD-WD', 'LD-AN', 4, 1, 4, 'II', 3),
--('KL-AN', 'LD-WD', 'LD-AN', 'LD-AN', 3, 0, 0, 'I', 1),
--('KL-CH', 'LD-KL', 'LD-KL', 'LD-LB', 7, 0, 9, 'II', 4),
--('KL-CH', 'LD-KL', 'LD-LB', 'LD-PA', 4, 0, 6, 'I', 2),
--('KL-CH', 'LD-LB', 'LD-PA', 'CH', 3, 1, 4, 'I', 2),
