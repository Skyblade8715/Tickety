const { response } = require("express");
const Discounts = require("../models/discountsM");

exports.getAll = (req, res) => {
    Discounts.getAll().then((query) => {
        res.status(200)
           .json(query.recordset);
    }).catch((err) => {
        res.status(404)
           .json(err);
    })
}