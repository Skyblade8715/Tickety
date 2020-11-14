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

exports.getNames = () => {
    return mssql.connect(config).then(pool => {
        return pool.request()
            .query('select StationID, Name from LKA.Stations')
    });
}