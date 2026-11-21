package com.example.busproject;

import java.util.ArrayList;

public class Ticket {
    private String bus;
    private String firststation;
    private String laststation;
    private String totalsits;
    private String linenum;
    private ArrayList<String> sitpositions;
    private Boolean allowed;

    public Ticket() {

    }


    public Ticket(String bus, String firststation, String laststation, String totalsits, String linenum, ArrayList<String> sitpositions, Boolean allowed) {
        this.bus = bus;
        this.firststation = firststation;
        this.laststation = laststation;
        this.totalsits = totalsits;
        this.linenum = linenum;
        this.sitpositions = sitpositions;
        this.allowed = allowed;
    }

    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }

    public String getFirststation() {
        return firststation;
    }

    public void setFirststation(String firststation) {
        this.firststation = firststation;
    }

    public String getLaststation() {
        return laststation;
    }

    public void setLaststation(String laststation) {
        this.laststation = laststation;
    }

    public String getTotalsits() {
        return totalsits;
    }

    public void setTotalsits(String totalsits) {
        this.totalsits = totalsits;
    }

    public String getLinenum() {
        return linenum;
    }

    public void setLinenum(String linenum) {
        this.linenum = linenum;
    }

    public ArrayList<String> getSitpositions() {
        return sitpositions;
    }

    public void setSitpositions(ArrayList<String> sitpositions) {
        this.sitpositions = sitpositions;
    }

    public Boolean getAllowed() {
        return allowed;
    }

    public void setAllowed(Boolean allowed) {
        this.allowed = allowed;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "bus='" + bus + '\'' +
                ", firststation='" + firststation + '\'' +
                ", laststation='" + laststation + '\'' +
                ", totalsits='" + totalsits + '\'' +
                ", linenum='" + linenum + '\'' +
                ", sitpositions=" + sitpositions +
                ", allowed=" + allowed +
                '}';
    }
}
