const discountsC = require("../controlers/discountsC");
const express = require("express");
const router = express.Router();

router.get("/all", discountsC.getAll);

module.exports = router;