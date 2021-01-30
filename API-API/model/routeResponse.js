const e = require("express");
const { raw } = require("express");

class routeResponse{
    constructor(rawData, provider){
        if(provider == "LKA"){
            // console.log(rawData.Table5);
            this.loadLKA(rawData);
        } else if(provider == "REGIO"){
            this.loadREGIO(rawData);
        } else if(provider == "MPK"){
            this.loadMPK(rawData);
        }
    } 

    loadMPK(rawData){
        // console.log(rawData)
        let routing = rawData.Table2.filter((row) => row.RouteID == rawData.Table1.RouteID);
        let startIndx = routing.findIndex(row => row.Name == rawData.Table3[0])
        let endIndx = routing.findIndex(row => row.Name == rawData.Table3[1])
        let myTab;
        if(startIndx < endIndx){
            myTab = routing.slice(startIndx, endIndx + 1)
        } else {
            myTab = routing.slice(endIndx, startIndx + 1)
        }
        this.time = myTab.reduce((sum, row) => sum + row.DriveTime, 0);
        this.startTime = new Date(rawData.Table1.StartTime)
        this.endTime = new Date(new Date(rawData.Table1.StartTime).getTime() + 60000 * this.time)
        this.line = rawData.Table1.RouteID;
        this.type = rawData.Table1.Type;
        this.startStopName = rawData.Table3[0]
        this.endStopName = rawData.Table3[1]
        this.subStations = routing.map((row, index) => ({
            "name": row.Name,
            "time": new Date(this.startTime.getTime() + 60000 *  routing.slice(0, index + 1).reduce((sum, v) => sum + v.DriveTime, 0))
        }));
        this.startTime = new Date(this.subStations.find((v) => v.name == this.startStopName).time)
        this.endTime = new Date(this.subStations.find((v) => v.name == this.endStopName).time)
        this.time = new Date(this.endTime.getTime() - this.startTime.getTime());
        // console.log(this);
    }

    loadLKA(rawData){
        // console.log(rawData.Table5);
        let Table2 = rawData.Table2.filter((row) => row.RouteID == rawData.Table1.RouteID);
        let Table3 = Table2.slice(0, -1).map((row, index) => 
            rawData.Table3.find((v) => 
                row.StationID == v.Station1ID &&
                    Table2[index + 1].StationID == v.Station2ID ||
                Table2[index + 1].StationID == v.Station1ID &&
                    row.StationID == v.Station2ID
            ));

        let desiredTab3;
        let myTab;
        let startIndx = Table2.findIndex(row => row.StationID == rawData.Table6[0])
        let endIndx = Table2.findIndex(row => row.StationID == rawData.Table6[1])
        if(startIndx < endIndx){
            myTab = Table2.slice(startIndx, endIndx + 1)
            desiredTab3 = myTab.slice(0, -1).map((row, index) => Table3.find((v) => 
                row.StationID == v.Station1ID &&
                myTab[index + 1].StationID == v.Station2ID ||
                myTab[index + 1].StationID == v.Station1ID &&
                row.StationID == v.Station2ID)
            )
        } else {
            myTab = Table2.slice(endIndx, startIndx + 1)
            desiredTab3 = myTab.slice(0, -1).map((row, index) => Table3.find((v) => 
                row.StationID == v.Station1ID &&
                myTab[index + 1].StationID == v.Station2ID ||
                myTab[index + 1].StationID == v.Station1ID &&
                row.StationID == v.Station2ID)
            )
        }
        this.trainID = rawData.Table1.TrainID;
        this.startTime = new Date(rawData.Table1.StartTime);
        this.endTime = this.calculateEndTime(myTab);
        this.provider = "LKA";
        this.duration = new Date(this.endTime.getTime() - this.startTime.getTime());
        this.distance = Table3.reduce((sum, row) => sum + row.Distance, 0);
        
        
        let tempPrice = desiredTab3.reduce((sum, row) => sum + row.Price, 0);
        this.prices = rawData.Table4.map((row) => ({
            "id": row.DiscountID,
            "name": row.Name,
            "price": Math.round(row.Percentage * tempPrice * 100)/100
        }));
    
        this.wifi = rawData.Table1.WiFi;
        this.disabilitySupp = rawData.Table1.DisabilitySupp;
        this.ac = rawData.Table1.AC;
        this.machine = rawData.Table1.TicketMachine;
        // console.log(Table2);
        this.subStations = Table2.map((row, index) => ({
            "name": rawData.Table5.find((v) => v.ID == row.StationID).name,
            "arrivalTime": this.calculateEndTime(Table2.slice(0, index + 1)),
            "departureTime": new Date(this.calculateEndTime(Table2.slice(0, index + 1)).getTime() + 60000 * row.WaitTime),
            "platform": row.Platform,
            "track": row.Track,
            "dist": row.Distance
        }))
        
        let startStationName = rawData.Table5.find((row) => row.ID == rawData.Table6[0]).name
        let endStationName = rawData.Table5.find((row) => row.ID == rawData.Table6[1]).name
        this.startTime = new Date(this.subStations.find((v) => v.name == startStationName).departureTime)
        this.endTime = new Date(this.subStations.find((v) => v.name == endStationName).arrivalTime)
        this.duration = new Date(this.endTime.getTime() - this.startTime.getTime());
        // console.log(rawData.Table5)
    }

    loadREGIO(rawData){
        
        let Table2 = rawData.Table2.filter((row) => row.RouteID == rawData.Table1.RouteID);

        this.trainID = rawData.Table1.TrainID;
        this.startTime = new Date(rawData.Table1.StartTime);        
        let myTab;
        let startIndx = Table2.findIndex(row => row.NowStationID == rawData.Table5[0])
        let endIndx = Table2.findIndex(row => row.NowStationID == rawData.Table5[1])
        if(startIndx < endIndx){
            myTab = Table2.slice(startIndx, endIndx + 1)
        } else {
            myTab = Table2.slice(endIndx, startIndx + 1)
        }

        this.endTime = this.calculateEndTime(myTab);
        this.duration = new Date(this.endTime.getTime() - this.startTime.getTime());
        this.provider = "REGIO";
        this.distance = Table2.reduce((sum, row) => sum + row.Distance, 0);
        this.prices = rawData.Table3.map((row) => ({
            "id": row.PriceID,
            "name": row.Name,
            "price": Math.round(row.PricePerKm * this.distance * 100)/100
        }));
        this.wifi = rawData.Table1.WiFi;
        this.disabilitySupp = rawData.Table1.DisabilitySupp;
        this.ac = rawData.Table1.AC;
        this.machine = false;
        this.subStations = Table2.map((row, index) => ({
            "name": rawData.Table4.find((v) => v.ID == row.NowStationID).name,
            "arrivalTime": this.calculateEndTime(Table2.slice(0, index + 1)),
            "departureTime": new Date(this.calculateEndTime(Table2.slice(0, index + 1)).getTime() + 60000 * row.TimeWait),
            "platform": row.Platform,
            "track": row.Track
        }))
    }

    calculateEndTime(Table2){
        return new Date(this.startTime.getTime() + 60000 * Table2.reduce((sum, row, index, arr) => {
            if(index == arr.length - 1){
                return sum + (typeof(row.ArrivalTime) == "number"?row.ArrivalTime:row.Time);
            }
            return sum + (typeof(row.ArrivalTime) == "number"?row.ArrivalTime:row.Time)
                + (typeof(row.WaitTime) == "number"?row.WaitTime:row.TimeWait);
        }, 0));
    }

}

module.exports = routeResponse;