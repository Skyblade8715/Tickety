const ticketTypesC = require("../controlers/ticketTypesC");
const express = require("express");
const router = express.Router();

router.get("/:discount", ticketTypesC.getTypes);

module.exports = router;