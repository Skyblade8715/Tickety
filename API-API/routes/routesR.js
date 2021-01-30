const routesC = require("../controlers/routesC");
const express = require("express");
const router = express.Router();

router.get("/route/:start-:end", routesC.getRoute);
router.post("", routesC.buyTicket);
router.put("/tickets/:id", routesC.updateTicket);
router.get("/conn/:start/:end", routesC.getMPKRoute);

module.exports = router;