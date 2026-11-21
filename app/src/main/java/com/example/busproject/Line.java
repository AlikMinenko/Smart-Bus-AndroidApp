package com.example.busproject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class Line {
    private String number;
    private String description;
    private String bus;
    private ArrayList<String> stations;
    private String totalstations;
    private String prices;
    private String deptime;
    private String endtime;
    private String totaltime;

    public Line(){}


    public Line(String number, String description, String bus, ArrayList<String> stations, String prices, String deptime, String totaltime) {
        this.number = number;
        this.description = description;
        this.bus = bus;
        this.stations = stations;
        this.totalstations=String.valueOf(stations.size());
        this.prices = prices;
        this.deptime = deptime;
        this.totaltime = totaltime;


        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date d = null;
        try {
            d = df.parse(this.getDeptime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, parseInt(totaltime));
        this.setEndtime(df.format(cal.getTime()));
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }

    public ArrayList<String> getStations() {
        return stations;
    }

    public void setStations(ArrayList<String> stations) {
        this.stations = stations;
    }

    public String getTotalstations() {
        return totalstations;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getDeptime() {
        return deptime;
    }

    public void setDeptime(String deptime) {
        this.deptime = deptime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(String totaltime) {
        this.totaltime = totaltime;
    }

    @Override
    public String toString() {
        return "Line{" +
                "number='" + number + '\'' +
                ", description='" + description + '\'' +
                ", buses=" + bus +
                ", stations=" + stations +
                ", totalstations='" + totalstations + '\'' +
                ", prices='" + prices + '\'' +
                ", deptime='" + deptime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", totaltime='" + totaltime + '\'' +
                '}';
    }
}
