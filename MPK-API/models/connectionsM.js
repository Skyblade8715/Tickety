const mssql   = require("mssql");
// config for your database
const config = {
    user: 'MPK_DB',
    password: 'MPK1234',
    server: 'Sky', 
    database: 'MPK',
    options: { 
        enableArithAbort: false
    }
};

mssql.on('error', err => {
    // ... error handler
})

exports.getConnection = (start, end) => {
    return mssql.connect(config).then(pool => {
        return pool.request()
            .query(
                `with ids as(
                    select RouteID from MPK.Route where StopID in(Select StopID from MPK.Stops where Name in\n` +
                    `('${start}','${end}')) group by RouteID having count(*) > 1
                    )
                    select * from MPK.Timetables where Timetables.RouteId in (select RouteID from ids);
                    Select RouteID, Seq_no, DriveTime, Route.StopID, Name from MPK.Route Inner join MPK.Stops on Route.StopID=Stops.StopID where RouteID in(select RouteID from MPK.Route where StopID in(Select StopID from MPK.Stops\n` +
                    `where Name in('${start}','${end}')) group by RouteID having count(*) > 1)`
            )
    });
}