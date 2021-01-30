const mssql   = require("mssql");
// config for your database
const config = {
    user: 'LKA_DB',
    password: 'LKA1234',
    server: 'Sky', 
    database: 'LKA',
    options: { 
        enableArithAbort: false
    }
};

mssql.on('error', err => {
    // ... error handler
})

exports.buyTicket = (req) => {
    let start = req.body.chosenStartTime.split(" ")[0] + " " + req.body.startTime;
    let end = req.body.chosenStartTime.split(" ")[0] + " " + req.body.endTime;
    return mssql.connect(config).then(pool => {
        return pool.request()
            .query(`INSERT INTO [LKA].[Tickets]([TrainID],[StartStation],[EndStation],[StartTime],[EndTime],[DiscountID],[Name],[Surname],[Dog],[Bike],[Bag],[Price])\nOUTPUT INSERTED.TicketID\nVALUES ('${req.body.trainID}','${req.body.startStation}','${req.body.endStation}','${start}','${end}','${req.body.discountName}','${req.body.name}','${req.body.surname}','${req.body.dog}','${req.body.bike}','${req.body.bag}','${req.body.price}')`)
        });
    
};

exports.getRoute = (start, end) => {
    return mssql.connect(config).then(pool => {
        return pool.request()
            .query(
                "WITH ids as " +
                "( select RouteID from LKA.Route " +
                "where StationID IN (Select StationID from LKA.Stations where Name ='" + start + "' OR Name ='" + end + "') " +
                "group by RouteID having count(*)=" + 2 + ")\n" +
                "select DISTINCT Timetable.RouteID, Timetable.TimetableID, Timetable.StartTime, Trains.* " +
                "from LKA.Route INNER JOIN LKA.Timetable ON Route.RouteID=Timetable.RouteID " +
                "INNER JOIN LKA.Trains ON Timetable.TrainID=Trains.TrainID " + 
                "Where Route.RouteID IN (Select RouteID from ids);\n" +
                "select * from LKA.Route where RouteID in (select RouteID from LKA.Route " +
                "where StationID " +
                "IN (Select StationID from LKA.Stations where Name ='" + start + "' OR Name ='" + end + "') " +
                "group by RouteID " +
                "having count(*)=2);\n" +
                "WITH ids as (" +
                "	select RouteID " +
                "	from LKA.Route " +
                "	where StationID " +
                "	IN (Select StationID from LKA.Stations where Name ='" + start + "' OR Name ='" + end + "') " +
                "	group by RouteID " +
                "	having count(*)=2" +
                ")\n" +
                "select * from LKA.Map " +
                "where (" +
                "(Station1ID in (select distinct StationID from LKA.Route where RouteID in (select RouteID from ids))" +
                "and " +
                "(Station2ID in (select distinct StationID from LKA.Route where RouteID in (select RouteID from ids))" +
                ")));\n" +
                "select * from LKA.Discounts\n" + 
                "select StationID, Name from LKA.Stations"
            )
    });
}