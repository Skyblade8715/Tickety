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

exports.getNames = () => {
    return mssql.connect(config).then(pool => {
        return pool.request()
            .query('select * from MPK.Stops')
    });
}