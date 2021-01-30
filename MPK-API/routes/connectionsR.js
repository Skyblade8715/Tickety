const connectionsC = require("../controlers/connectionsC.js");
const express = require("express");
const router = express.Router();

router.get("/conn/:start/:end", connectionsC.getConnection);
// router.post("", routesC.buyTicket)

module.exports = router;