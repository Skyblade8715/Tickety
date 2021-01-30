const { response } = require("express");
const Connections = require("../models/connectionsM");


exports.getConnection = (req, res) => {
    Connections.getConnection(req.params.start, req.params.end).then((query) => {
        console.log("getRoute: " + req.params.start + " - " + req.params.end); 
        res.status(200)
           .json(query.recordsets);
    }).catch((err) => {
        console.log("MPK connectionC error!")
        // console.log(res)
        res.status(404)
           .json(err);
    })
}