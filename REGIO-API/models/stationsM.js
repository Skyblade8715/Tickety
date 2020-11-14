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

exports.getNames = () => {
    return mssql.connect(config).then(pool => {
        return pool.request()
            .query('select StationID, Name from REGIO.Stations')
    });
}