const { response } = require("express");
const axios = require("axios");

exports.getAll = (req, res) => {
    // axios.get("http://localhost:3001/stations/names").then(data => {
    //     res.send(data);
    // }).catch(err => {
    //     console.log(err);
    // });
    var tab = [];
    tab.push(axios.get("http://localhost:3001/discounts/getAll").catch(err => {
        return "Serwer LKA nie odpowiada";
    }));
    tab.push(axios.get("http://localhost:3002/discounts/all").catch(err => {
        return "Serwer REGIO nie odpowiada";
    }));
    tab.push(axios.get("http://localhost:3003/tickets").catch(err => {
        return "Serwer MPK nie odpowiada";
    }));


    Promise.all(tab).then(holder => {
    
        var lkaDiscount = holder[0].data.map(row => {
            return {   
                "provider": "LKA",
                "id": row.DiscountID,
                "name": row.Name,
                "percentage": (row.Percentage * 100)
            }
        })
        let normal = holder[1].data[0].PricePerKm;
        var regioDiscount = holder[1].data.map(row => {
            return {   
                "provider": "REGIO",
                "id": row.PriceID,
                "name": row.Name,
                "percentage": (Math.round(row.PricePerKm / normal * 100 * 100)/100)
            }
        })
        var mpkDiscount = holder[2].data.map(row => {
            return {   
                "provider": "MPK",
                "id": row.DiscountId,
                "name": row.Name,
                "percentage": row.Percentage
            }
        })

        let out = Array.from(new Set([...lkaDiscount, ...regioDiscount, ...mpkDiscount]));
        
        res.send({
            'Message': out
            // 'ErrorMessage':errors 
        });

    }).catch(
    )
}

