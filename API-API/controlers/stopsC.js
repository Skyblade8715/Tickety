const { response } = require("express");
const axios = require("axios");

exports.getNames = (req, res) => {
    axios.get("http://localhost:3003/stops/names").catch(err => {
        return "Serwer MPK nie odpowiada";
    }).then(holder => {
        // console.log(holder.data)
        var namesMPK = holder.data.map(v => v.Name);
        res.send({
            'Message': namesMPK
        });
    });
}
