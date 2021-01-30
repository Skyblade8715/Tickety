USE LKA
GO

Select * from LKA.Stations order BY StationID
Select * from LKA.Route

--INSERT INTO LKA.Route(RouteID, seq_no, StationID, Platform, Track, ArrivalTime, WaitTime)
--Values 
--('LD-SI1', 0, 'CHECH', 'I', 1, 0, 0),
--('LD-SI1', 1, 'PAB', 'I', 1, 4, 1),
--('LD-SI1', 2, 'LDZ-LUB', 'I', 1, 6, 0),
--('LD-SI1', 3, 'LDZ-KAL', 'III', 9, 9, 1),
--('SI-LD1', 0, 'LDZ-KAL', 'II', 3, 0, 0),
--('SI-LD1', 1, 'LDZ-LUB', 'I', 2, 9, 1),
--('SI-LD1', 2, 'PAB', 'I', 2, 6, 0),
--	('SI-LD1', 3, 'CHECH', 'I', 2, 4, 1),
--('LD-SK-WA1', 0, 'LDZ-FAB', 'II', 1, 0, 0),
--('LD-SK-WA1', 1, 'LDZ-NIC', 'I', 1, 3, 0),
--('LD-SK-WA1', 2, 'LDZ-WIDZ', 'II', 1, 3, 1),
--('LD-SK-WA1', 3, 'LDZ-AND', 'I', 1, 3, 0),
--('LD-ZG1', 0, 'LDZ-WIDZ', 'II', 3, 0, 0),
--('LD-ZG1', 1, 'LDZ-DAB', 'I', 1, 4, 1),
--('LD-ZG1', 2, 'LDZ-CHJ', 'II', 3, 2, 1),
--('LD-ZG1', 3, 'LDZ-PAB', 'I', 2, 2, 1),
--('LD-ZG1', 4, 'LDZ-KAL', 'II', 3, 4, 1),
--('LD-ZG1', 5, 'LDZ-ZAB', 'I', 2, 5, 1),
--('LD-ZG1', 6, 'LDZ-RAD-ZAH', 'I', 2, 8, 0),
--('LD-ZG1', 7, 'ZGI', 'II', 3, 6, 0),
--('LD-ZG2', 0, 'LDZ-FAB', 'II', 1, 0, 0),
--('LD-ZG2', 1, 'LDZ-NIC', 'I', 1, 3, 0),
--('LD-ZG2', 2, 'LDZ-WIDZ', 'II', 3, 3, 10),
--('LD-ZG2', 3, 'LDZ-STK', 'I', 1, 6, 0),
--('LD-ZG2', 4, 'LDZ-MAR', 'II', 3, 6, 0),
--('LD-ZG2', 5, 'LDZ-ART', 'I', 1, 3, 0),
--('LD-ZG2', 6, 'ZGI', 'I', 5, 5, 0),
--('LD-TM', 0, 'LDZ-FAB', 'II', 2, 0, 0),
--('LD-TM', 1, 'LDZ-NIC', 'I', 1, 3, 0),
--('LD-TM', 2, 'LDZ-WIDZ', 'III', 5, 3, 1),
--('LD-TM', 3, 'LDZ-AND', 'II', 3, 4, 0),
--('LD-PT-RAD', 0, 'LDZ-FAB', 'II', 2, 0, 0),
--('LD-PT-RAD', 1, 'LDZ-NIC', 'I', 1, 3, 0),
--('LD-PT-RAD', 2, 'LDZ-WIDZ', 'III', 5, 3, 1),
--('LD-PT-RAD', 3, 'LDZ-AND', 'II', 3, 4, 0)