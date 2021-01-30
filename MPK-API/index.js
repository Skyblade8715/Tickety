const express = require("express"),
      mssql   = require("mssql"),
      app     = express(),
      connectionsR = require("./routes/connectionsR.js"),
      stopsR = require("./routes/stopsR.js"),
      ticketsR = require("./routes/ticketsR.js")

app.use((req, res, next) => {
    res.header("Access-Control-Allow-Origin", "*"); 
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
});
// config for your database
const config = {
    user: 'MPK_DB',
    password: 'MPK1234',
    server: 'Sky', 
    database: 'MPK'
};

app.use(express.json());

app.use('/connections', connectionsR);
app.use('/stops', stopsR);
app.use('/tickets', ticketsR);


app.get("/", (req, res) => {
    console.log("ELOOO");
    res.send("Yellow");
})


app.listen(3003, () => {
    console.log("Listening..!");
})