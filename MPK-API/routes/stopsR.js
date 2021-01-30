const stopsC = require("../controlers/stopsC.js");
const express = require("express");
const router = express.Router();

router.get("/names", stopsC.getNames);

module.exports = router;