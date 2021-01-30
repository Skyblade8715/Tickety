const express = require("express"),
      app     = express(),
      stationsR = require("./routes/stationsR"),
      stopsR = require("./routes/stopsR"),
      routesR = require("./routes/routesR"),
      ticketTypesR = require("./routes/ticketTypesR"),
      discountsR = require("./routes/discountsR");

app.use((req, res, next) => {
    res.header("Access-Control-Allow-Origin", "*"); 
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
});
app.use(express.json());


app.use('/stations', stationsR);

app.use('/stops', stopsR);

app.use('/routes/', routesR);

app.use('/discounts/', discountsR);

app.use('/ticketTypes/', ticketTypesR);

app.get("/", (req, res) => {
    console.log("elo");
    res.send("howdy");
})


app.listen(3000, () => {
    console.log("Listening..!");
})
