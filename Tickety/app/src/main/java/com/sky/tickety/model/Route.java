package com.sky.tickety.model;

import java.util.List;

public class Route {

    public String trainID, startTime, endTime, provider, startStation, endStation, duration;
    public Integer distance;
    public List<List<String>> prices, subStations;
    public boolean wifi, disabilitySupp, ac, ticketMachine, isExpanded;

    public Route(String trainID, String startTime, String endTime,
                 String duration, String provider, Integer distance,
                 List<List<String>> prices, List<List<String>> subStations,
                 boolean wifi, boolean disabilitySupp,
                 boolean ac, boolean ticketMachine,
                 String startStation, String endStation) {
        this.trainID = trainID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.provider = provider;
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
        this.prices = prices;
        this.subStations = subStations;
        this.wifi = wifi;
        this.disabilitySupp = disabilitySupp;
        this.ac = ac;
        this.ticketMachine = ticketMachine;
    }

}
