const stationsC = require("../controlers/stationsC");
const express = require("express");
const router = express.Router();

router.get("/names", stationsC.getNames);

module.exports = router;