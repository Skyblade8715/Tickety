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

exports.updateTicket = (req) => {
    return mssql.connect(config).then(pool => {
        return pool.request()
            .query(`UPDATE [MPK].[Tickets] SET [StartTime] = '${req.body.startTime}',[EndTime] = DATEADD(minute, time, '${req.body.startTime}') WHERE ID = '${req.body.id}'`)
    });
}
    

exports.buyTicket = (req) => {
    return mssql.connect(config).then(pool => {
        return pool.request()
            .query(`INSERT INTO [MPK].[Tickets]([DiscountName],[Time],[StartTime],[EndTime])\nOUTPUT INSERTED.ID\nVALUES('${req.body.discountName}', ${req.body.time}, null, null)`)
        });
    
};

exports.getTicketTypes = (discount) => {
    return mssql.connect(config).then(pool => {
        return pool.request()
            .query(
                `select TicketID, Name, (Price * (select Discounts.Percentage from MPK.Discounts where Name = '${discount}')/100) as Price, Time from MPK.TicketTypes`
            )
    });
}

exports.getDiscounts = () => {
    return mssql.connect(config).then(pool => {
        return pool.request()
            .query(
                `select * from MPK.Discounts`
            )
    });
}