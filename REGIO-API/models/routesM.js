const mssql   = require("mssql");
// config for your database
const config = {
    user: 'REGIO_DB',
    password: 'REGIO1234',
    server: 'Sky', 
    database: 'REGIO',
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
        .query(`INSERT INTO [REGIO].[Tickets]([TrainID],[StartStationID],[EndStationID],[StartTime],[EndTime],[PriceID],[Name],[Surname],[Dog],[Bike],[Bag],[Price])\nOUTPUT INSERTED.TicketID\nVALUES('${req.body.trainID}','${req.body.startStation}','${req.body.endStation}','${start}','${end}','${req.body.discountName}','${req.body.name}','${req.body.surname}','${req.body.dog}','${req.body.bike}','${req.body.bag}','${req.body.price}')`);
    });  
}; 

exports.getRoute = (start, end) => {
    return mssql.connect(config).then(pool => {
        return pool.request()
            .query(
                "Select * from REGIO.Trains where RouteID IN(" +
                "	Select RouteID from REGIO.Routes where NowStationID IN " +
                "	(Select StationID from REGIO.Stations where Name In('" + start + "','" + end + "')) group by RouteID having count(*) > 1)\n" +
                "Select * from REGIO.Routes where RouteID IN(" +
                "	Select RouteID from REGIO.Routes where NowStationID IN " +
                "	(Select StationID from REGIO.Stations where Name In('" + start + "','" + end + "')) group by RouteID having count(*) > 1)\n" +
                "Select * from REGIO.Prices\n" +
                "Select StationID, Name from REGIO.Stations" 
            )
    });
}