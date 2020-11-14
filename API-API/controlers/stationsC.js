const { response } = require("express");
const axios = require("axios");

exports.getNames = (req, res) => {
    // axios.get("http://localhost:3001/stations/names").then(data => {
    //     res.send(data);
    // }).catch(err => {
    //     console.log(err);
    // });
    var tab = [];
    tab.push(axios.get("http://localhost:3001/stations/names").catch(err => {
        return "Serwer LKA nie odpowiada";
    }));
    tab.push(axios.get("http://localhost:3002/stations/names").catch(err => {
        return "Serwer REGIO nie odpowiada";
    }));

    Promise.all(tab).then(holder => {
        var errors, names;
        errors = "";
        console.log(typeof(holder[0]));
        console.log(typeof(holder[1]));
        if(typeof(holder[0]) != "string"){
            var namesLKA = holder[0].data.map(i => i.Name);
        } else {
            errors = holder[0];
            namesLKA = [];
        }
        if(typeof(holder[1]) != "string"){
            var namesREGIO = holder[1].data.map(i => i.Name);
        } else {
            errors = holder[1];
            namesREGIO = [];
        }
        var names = Array.from(new Set([...namesLKA,...namesREGIO]));
        res.send({
            'Message':names,
            'ErrorMessage':errors 
        });

    }).catch(
        console.log("ERROR")
    )


}
