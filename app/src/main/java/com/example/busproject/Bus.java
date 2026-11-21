package com.example.busproject;

import java.util.ArrayList;


public class Bus {
    private String currentST;
    private String currentUser;
    private int totalpeople;
    private int functions;
    private ArrayList<Boolean> places;
    private Boolean available;
    private String live;
    private int temp;
    private Boolean goingdown;

    public Bus(){}

    public Bus(String Live){
        this.live =Live;

        this.currentST="";
        this.currentUser="";
        this.totalpeople=0;
        this.functions=0;
        ArrayList<Boolean> list =new ArrayList<Boolean>();
        for(int i=0;i<15;i++){
            list.add(false);
        }
        this.places=list;
        this.available=true;
        this.temp=0;
        this.goingdown=false;
    }

    public String getCurrentST() {
        return this.currentST;
    }

    public void setCurrentST(String currentST) {
        this.currentST = currentST;
    }

    public String getCurrentUser() {
        return this.currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public int getTotalpeople() {
        return this.totalpeople;
    }

    public void setTotalpeople(int totalpeople) {
        this.totalpeople = totalpeople;
    }

    public int getFunctions() {
        return this.functions;
    }

    public void setFunctions(int functions) {
        this.functions = functions;
    }

    public ArrayList<Boolean> getPlaces() {
        return this.places;
    }

    public void setPlaces(ArrayList<Boolean> places) {
        this.places = places;
    }

    public Boolean getAvailable() {
        return this.available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getLive() {
        return this.live;
    }

    public void setLive(String live) {
        this.live = live;
    }

    public int getTemp() {
        return this.temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public Boolean getGoingdown() {
        return this.goingdown;
    }

    public void setGoingdown(Boolean goingdown) {
        this.goingdown = goingdown;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "CurrentST='" + currentST + '\'' +
                ", CurrentUser='" + currentUser + '\'' +
                ", Totalpeople=" + totalpeople +
                ", Functions=" + functions +
                ", Places=" + places.toString() +
                ", Available=" + available +
                ", Live='" + live + '\'' +
                ", Temp=" + temp +
                ", goingdown=" + goingdown +
                '}';
    }
}
