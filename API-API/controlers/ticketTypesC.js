const { response } = require("express");
const axios = require("axios");

exports.getTypes = (req, res) => {
    
    var discount = req.params.discount;
    var MPK = "http://localhost:3003/tickets/" + discount;
    
    axios.get(encodeURI(MPK)).catch(err => {
        return "Serwer MPK nie odpowiada";
    }).then(holder => {
        res.send({
            'Message': holder.data
        });
    });
}
