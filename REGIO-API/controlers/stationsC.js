const { response } = require("express");
const Stations = require("../models/stationsM");

exports.getNames = (req, res) => {
    Stations.getNames().then((query) => {
        res.status(200)
           .json(query.recordset);
    }).catch((err) => {
        res.status(404)
           .json(err);
    })
}