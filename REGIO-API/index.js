const express = require("express"),
      mssql   = require("mssql"),
      app     = express(),
      stationsR = require("./routes/stationsR"),
      routesR = require("./routes/routesR"),
      discountsR = require("./routes/discountsR");

app.use((req, res, next) => {
    res.header("Access-Control-Allow-Origin", "*"); 
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
});
// config for your database
const config = {
    user: 'REGIO',
    password: 'REGIO1234',
    server: 'Sky', 
    database: 'REGIO'
};

app.use(express.json());

app.use('/stations', stationsR);

app.use('/discounts', discountsR);

app.use('/routes', routesR);

app.get("/", (req, res) => {
    console.log("ELOOO");
    res.send("Kappa");
})


app.listen(3002, () => {
    console.log("Listening..!");
})