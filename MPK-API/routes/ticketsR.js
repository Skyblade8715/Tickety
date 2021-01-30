const ticketsC = require("../controlers/ticketsC.js");
const express = require("express");
const router = express.Router();

router.get("/:discount", ticketsC.getTicketTypes);
router.get("/", ticketsC.getDiscounts);
router.post("", ticketsC.buyTicket);
router.put("/ticket/:id", ticketsC.updateTicket);

module.exports = router;