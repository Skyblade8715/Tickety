USE MPK
Go



INSERT INTO [MPK].[TicketTypes]([TicketID],[Name],[Price],[Time])
     VALUES
           ('20Min',  '20 minutowy', '3.40', '20'),
           ('50Min',  '50 minutowy', '4.60', '50'),
           ('90Min',  '90 minutowy', '6.00', '90'),
           ('24h',  '24 godzinny', '15.00', '1440'),
           ('48h',  '48 godzinny', '28.00', '2880'),
           ('72h',  '72 godzinny', '42.00', '4320'),
           ('7d',  '7 dniowy', '56.00', '10080')
GO

