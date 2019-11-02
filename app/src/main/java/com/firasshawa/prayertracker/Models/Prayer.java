package com.firasshawa.prayertracker.Models;

public class Prayer {
    private String name;
    private boolean state ;
    private String date ;
    private String time ;

    public Prayer(String name, String date) {
        this.name = name;
        this.state = false;
        this.date = date;
        this.time = "00:00:00";
    }

    public Prayer() {
        this.name = "Prayer";
        this.state = false;
        this.date = "00/00/0000";
        this.time = "00:00:00";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Prayer{" +
                "name='" + name + '\'' +
                ", state=" + state +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
