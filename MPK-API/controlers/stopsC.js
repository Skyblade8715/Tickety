const { response } = require("express");
const Stops = require("../models/stopsM.js");

exports.getNames = (req, res) => {
    Stops.getNames().then((query) => {
        res.status(200)
           .json(query.recordset);
    }).catch((err) => {
        res.status(404)
           .json(err);
    })
}