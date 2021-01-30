const routesC = require("../controlers/routesC");
const express = require("express");
const router = express.Router();

router.get("/:start-:end", routesC.getRoute);
router.post("", routesC.buyTicket)

module.exports = router;