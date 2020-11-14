const express = require("express"),
      app     = express(),
      stationsR = require("./routes/stationsR");

app.use((req, res, next) => {
    res.header("Access-Control-Allow-Origin", "*"); 
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
});
app.use(express.json());


app.use('/stations', stationsR);


app.get("/", (req, res) => {
    console.log("elo");
    res.send("howdy");
})


app.listen(3000, () => {
    console.log("Listening..!");
})
