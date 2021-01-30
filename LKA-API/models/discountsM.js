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

exports.getAll = () => {
    return mssql.connect(config).then(pool => {
        return pool.request()
            .query('Select * from LKA.Discounts')
    });
}