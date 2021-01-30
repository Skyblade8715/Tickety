package com.sky.tickety.model;

import android.text.BoringLayout;

import java.util.List;

public class Conn {

    public String line, startTime, endTime, type, startStation, endStation, duration;
    public List<List<String>> subStations;
    public Boolean isExpanded = false;

    public Conn(String line, String startTime, String endTime,
                String duration, String type,
                List<List<String>> subStations,
                String startStation, String endStation) {
            this.line = line;
            this.startTime = startTime;
            this.endTime = endTime;
            this.duration = duration;
            this.type = type;
            this.startStation = startStation;
            this.endStation = endStation;
            this.subStations = subStations;
    }
}
