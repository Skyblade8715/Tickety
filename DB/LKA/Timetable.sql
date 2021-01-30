USE LKA 
GO

Select * from LKA.Route
Select * from LKA.Trains
Select * from LKA.Timetable

--INSERT INTO LKA.Timetable(RouteID, TimetableID, StartTime, TrainID)
--Values 
--('LD-SI1', 'LD-ST1-1', '08:20', 'L 120333'),
--('LD-SI1', 'LD-ST1-1', '09:20', 'L 120354'),
--('LD-SI1', 'LD-ST1-1', '10:20', 'L 122453'),
--('LD-SI1', 'LD-ST1-1', '11:20', 'L 120123'),
--('LD-SI1', 'LD-ST1-1', '12:20', 'L 121423'),
--('LD-SI1', 'LD-ST1-1', '13:20', 'L 121243'),
--('LD-SI1', 'LD-ST1-1', '14:20', 'L 127643'),
--('LD-SI1', 'LD-ST1-1', '15:20', 'L 125243'),
--('LD-SI1', 'LD-ST1-1', '16:20', 'L 122523'),
--('SI-LD1', 'ST-LD1-1', '08:50', 'L 122523'),
--('SI-LD1', 'ST-LD1-1', '09:50', 'L 125243'),
--('SI-LD1', 'ST-LD1-1', '10:50', 'L 127643'),
--('SI-LD1', 'ST-LD1-1', '11:50', 'L 121243'),
--('SI-LD1', 'ST-LD1-1', '12:50', 'L 121423'),
--('SI-LD1', 'ST-LD1-1', '13:50', 'L 120123'),
--('SI-LD1', 'ST-LD1-1', '14:50', 'L 122453'),
--('SI-LD1', 'ST-LD1-1', '16:50', 'L 120333'),
--('SI-LD1', 'ST-LD1-1', '15:50', 'L 120354')