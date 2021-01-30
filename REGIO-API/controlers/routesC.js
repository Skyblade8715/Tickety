const { response } = require("express");
const Routes = require("../models/routesM");

exports.buyTicket = (req, res) => {
    Routes.buyTicket(req).then((query) => {
        // console.log(query.recordset[0].TicketID)
        res.status(200).json(query.recordset[0].TicketID);
    }).catch((err) => {
        console.log("REGIO routesC error!")
        // console.log(res)
        res.status(404)
           .json(err);
    });
};


exports.getRoute = (req, res) => {
    Routes.getRoute(req.params.start, req.params.end).then((query) => {
        console.log("getRoute: " + req.params.start + " - " + req.params.end); 
        res.status(200)
           .json(query.recordsets);
    }).catch((err) => {
        console.log("Regio routesC error")
        // console.log(res)
        res.status(404)
           .json(err);
    })
}