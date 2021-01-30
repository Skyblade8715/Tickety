const { response } = require("express");
const Tickets = require("../models/ticketsM.js");

exports.updateTicket = (req, res) => {
    Tickets.updateTicket(req).then((query) => {
        res.sendStatus(200);
    }).catch((err) => {
        console.log("MPK updateTicket error!")
        res.status(404)
           .json(err);
    });
};

exports.buyTicket = (req, res) => {
    Tickets.buyTicket(req).then((query) => {
        console.log(query.recordset[0])
        res.status(200).json(query.recordset[0].ID);
    }).catch((err) => {
        console.log("MPK buyTicket error!")
        // console.log(res)
        res.status(404)
           .json(err);
    });
};

exports.getTicketTypes = (req, res) => {
    Tickets.getTicketTypes(req.params.discount).then((query) => {
        // console.log(query.recordset[0].TicketID)     
        out = query.recordsets[0].sort((x, y) => x.Time - y.Time);
        res.status(200).json(out);
    }).catch((err) => {
        console.log("MPK ticketTypesC error!")
        // console.log(res)
        res.status(404)
           .json(err);
    });
};

exports.getDiscounts = (req, res) => {
    Tickets.getDiscounts().then((query) => {
        // console.log(query.recordset[0].TicketID)
        res.status(200).json(query.recordsets[0]);
    }).catch((err) => {
        console.log("MPK getDiscounts error!")
        // console.log(res)
        res.status(404)
           .json(err);
    });
};