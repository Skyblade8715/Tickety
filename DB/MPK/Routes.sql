USE [MPK]
GO

INSERT INTO [MPK].[Route] ([RouteID],[Seq_no],[StopID],[DriveTime])
     VALUES
           ('15' ,'0', 'Pol-Ob', 0),
           ('15' ,'1', 'Pol-Skr', 2),
           ('15' ,'2', 'Pol-Wrb', 3),
           ('15' ,'3', 'Pol-TarLdz', 4),
           ('15' ,'4', 'Pol-Rad', 2),
           ('15' ,'5', 'Zer-Mic', 3),
           ('15' ,'6', 'Zer-Kop', 3),
           ('15' ,'7', 'Gd-Sk', 4),

           ('15R' ,'0', 'Gd-Sk', 0),
           ('15R' ,'1', 'Zer-Kop', 4),
           ('15R' ,'2', 'Zer-Mic', 3),
           ('15R' ,'3', 'Pol-Rad', 3),
           ('15R' ,'4', 'Pol-TarLdz', 2),
           ('15R' ,'5', 'Pol-Wrb', 4),
           ('15R' ,'6', 'Pol-Skr', 3),
           ('15R' ,'7', 'Pol-Ob', 2),

           ('17' ,'0', 'Pab-rLot', 0),
           ('17' ,'1', 'Pol-Ob', 2),
           ('17' ,'2', 'Pol-Skr', 4),
           ('17' ,'3', 'Pol-Wrb', 2),
           ('17' ,'4', 'Pol-TarLdz', 3),
           ('17' ,'5', 'Pol-Rad', 2),
           ('17' ,'6', 'Zer-Mic', 3),
           ('17' ,'7', 'Zer-Kop', 2),
           ('17' ,'8', 'Gd-Sk', 4),
           ('17' ,'9', 'Gd-Zi', 5),
		   
           ('17R' ,'0', 'Gd-Zi', 0),
           ('17R' ,'1', 'Gd-Sk', 5),
           ('17R' ,'2', 'Zer-Kop', 4),
           ('17R' ,'3', 'Zer-Mic', 2),
           ('17R' ,'4', 'Pol-Rad', 3),
           ('17R' ,'5', 'Pol-TarLdz', 2),
           ('17R' ,'6', 'Pol-Wrb', 3),
           ('17R' ,'7', 'Pol-Skr', 2),
           ('17R' ,'8', 'Pol-Ob', 4),
           ('17R' ,'9', 'Pab-rLot', 2)
GO