const axios = require("axios");
const routeResponse = require("../model/routeResponse");
const util = require("util");
const e = require("express");

// app.use(e.bodyParser());

exports.updateTicket = (req, res) => {
    var MPK = "http://localhost:3003/tickets/ticket/" + req.body.id;
    axios.put(encodeURI(MPK), req.body).catch(err => {
        return "Serwer MPK nie odpowiada";
    }).then(holder => {
        res.sendStatus(200);
    });
}

exports.buyTicket = (req, res) => {
    if(req.body.provider == "LKA"){
        delete req.body.provider;
        var LKA = "http://localhost:3001/routes";    
        axios.post(LKA, req.body).catch(err => {
            return "Serwer LKA nie odpowiada";
        }).then(holder => {
            console.log(holder.data)
            res.send({
                "data": holder.data
            });
        });

    } else if(req.body.provider == "REGIO"){

        delete req.body.provider;
        var REGIO = "http://localhost:3002/routes/";
        axios.post(REGIO, req.body).catch(err => {
            return "Serwer REGIO nie odpowiada";
        }).then(holder => {
            console.log(holder.data)
            res.send({
                "data": holder.data
            });
        });
    } else if(req.body.provider == "MPK"){

        delete req.body.provider;
        var MPK = "http://localhost:3003/tickets/";
        axios.post(MPK, req.body).catch(err => {
            return "Serwer MPK nie odpowiada";
        }).then(holder => {
            console.log(holder.data)
            res.send({
                "data": holder.data
            });
        });
    }
};

exports.getMPKRoute = (req, res) => {
    var start = req.params.start;
    var end = req.params.end;
    var MPK = "http://localhost:3003/connections/conn/" + start + "/" + end;
    console.log(MPK);
    axios.get(encodeURI(MPK)).catch(err => {
        return "Serwer LKA nie odpowiada";
    }).then(holder => {    
        
        let MPKData = {
            "Table1": holder.data[0],
            "Table2": holder.data[1],
        }
        let routeResponseMPK = MPKData.Table1.map((row) => 
            new routeResponse({
                "Table1": row,
                "Table2": MPKData.Table2,
                "Table3": [start, end]
            }, "MPK")
        )

        res.send({
            'Message': routeResponseMPK
        })

    })
};



exports.getRoute = (req, res) => {
    var start = req.params.start;
    var end = req.params.end;
    // axios.get("http://localhost:3001/stations/names").then(data => {
    //     res.send(data);
    // }).catch(err => {
    //     console.log(err);
    // });
    var tab = [];
    var LKA = "http://localhost:3001/routes/route/" + start + "-" + end;
    var REGIO = "http://localhost:3002/routes/" + start + "-" + end;
    
    tab.push(axios.get(encodeURI(LKA)).catch(err => {
        return "Serwer LKA nie odpowiada";
    }));
    tab.push(axios.get(encodeURI(REGIO)).catch(err => {
        return "Serwer REGIO nie odpowiada";
    }));

    Promise.all(tab).then(holder => {
        console.log("getRoute: " + start + " - " + end);
        var errors, names;
        errors = "";
        if(typeof(holder[0]) != "string"){
            var namesLKA = holder[0].data[4].map(i => ({"ID": i.StationID, "name": i.Name}));
        } else {
            errors = holder[0];
            namesLKA = [];
        }
        if(typeof(holder[1]) != "string"){
            var namesREGIO = holder[1].data[3].map(i => ({"ID": i.StationID, "name": i.Name}));
        } else {
            errors = holder[1];
            namesREGIO = [];
        }
        let startID = namesLKA.find(row => row.name == start).ID;
        let endID = namesLKA.find(row => row.name == end).ID;

        let LKAData = {
            "Table1": holder[0].data[0],
            "Table2": holder[0].data[1],
            "Table3": holder[0].data[2],
            "Table4": holder[0].data[3]
        }
        let REGIOData = {
            "Table1": holder[1].data[0],
            "Table2": holder[1].data[1],
            "Table3": holder[1].data[2]
        }
        
        // console.log(namesLKA);
        let routeResponsesLKA = LKAData.Table1.map((row) => 
            new routeResponse({
                "Table1": row,
                "Table2": LKAData.Table2,
                "Table3": LKAData.Table3,
                "Table4": LKAData.Table4,
                "Table5": [...namesLKA],
                "Table6": [startID, endID]
            }, "LKA")
        )
        
        startID = namesREGIO.find(row => row.name == start).ID;
        endID = namesREGIO.find(row => row.name == end).ID;

        let routeResponsesREGIO = REGIOData.Table1.map((row) => 
            new routeResponse({
                "Table1": row,
                "Table2": REGIOData.Table2,
                "Table3": REGIOData.Table3,
                "Table4": namesREGIO,
                "Table5": [startID, endID]
            }, "REGIO")
        )

        // console.log(util.inspect(routeResponsesLKA, {depth: 4}));
        // console.log(util.inspect(routeResponsesREGIO, {depth: 4}));

        let out = Array.from(new Set([...routeResponsesLKA, ...routeResponsesREGIO]));
        out = out.sort((x, y) => x.startTime.getTime() - y.startTime.getTime());

        res.send({
            'Message': out
            // 'ErrorMessage':errors 
        });

    }).catch(
        // console.log("ERROR")
    )
    
}

